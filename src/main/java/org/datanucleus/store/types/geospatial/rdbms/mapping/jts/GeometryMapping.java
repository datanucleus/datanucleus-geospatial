/**********************************************************************
 Copyright (c) 2006 Thomas Marti, Stefan Schmid and others. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Contributors:
 ...
 **********************************************************************/
package org.datanucleus.store.types.geospatial.rdbms.mapping.jts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.ClassNameConstants;
import org.datanucleus.ExecutionContext;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaDataUtils;
import org.datanucleus.state.ObjectProvider;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.mapping.MappingCallbacks;
import org.datanucleus.store.rdbms.mapping.datastore.DatastoreMapping;
import org.datanucleus.store.rdbms.mapping.datastore.AbstractDatastoreMapping;
import org.datanucleus.store.rdbms.mapping.datastore.OracleBlobRDBMSMapping;
import org.datanucleus.store.rdbms.mapping.java.SingleFieldMultiMapping;
import org.datanucleus.store.rdbms.table.Table;
import org.datanucleus.util.NucleusLogger;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Mapping for com.vividsolutions.jts.geom.Geometry to its datastore representation.
 */
public class GeometryMapping extends SingleFieldMultiMapping implements MappingCallbacks
{
    private static WKTReader wktReader;

    private boolean mapUserdataObject = false;

    protected static final Geometry createGeom(String wkt)
    {
        if (wktReader == null)
        {
            wktReader = new WKTReader();
        }

        try
        {
            return wktReader.read(wkt);
        }
        catch (ParseException e)
        {
            throw new Error("AssertionError: Illegal sampleValue", e);
        }
    }

    public String getJavaTypeForDatastoreMapping(int index)
    {
        return getJavaType().getName();
    }

    /**
     * Any usage of this type as a parameter cannot be used as a String in SQL.
     * @return false
     */
    public boolean representableAsStringLiteralInStatement()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.mapping.JavaTypeMapping#initialize(org.datanucleus.store.rdbms.
     * RDBMSStoreManager, java.lang.String)
     */
    public void initialize(RDBMSStoreManager storeMgr, String type)
    {
        super.initialize(storeMgr, type);
        addColumns(getJavaType().getName()); // Geometry
    }

    public void initialize(AbstractMemberMetaData mmd, Table table, ClassLoaderResolver clr)
    {
        super.initialize(mmd, table, clr);
        addColumns(getJavaType().getName()); // Geometry

        String mappingExtension = MetaDataUtils.getValueForExtensionRecursively(mmd, "mapping");
        if (mappingExtension != null && mappingExtension.equalsIgnoreCase("no-userdata"))
        {
            mapUserdataObject = false;
        }
        else
        {
            mapUserdataObject = true;
            addColumns(ClassNameConstants.JAVA_IO_SERIALIZABLE); // User Data Object
        }
    }

    public Class getJavaType()
    {
        return Geometry.class;
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.mapping.JavaTypeMapping#getObject(org.datanucleus.ExecutionContext,
     * java.lang.Object, int[], org.datanucleus.state.ObjectProvider, int)
     */
    public Object getObject(ExecutionContext ec, ResultSet rs, int[] exprIndex, ObjectProvider ownerSM, int ownerFieldNumber)
    {
        return getObject(ec, rs, exprIndex);
    }

    public Object getObject(ExecutionContext ec, ResultSet datastoreResults, int[] exprIndex)
    {
        Geometry geom = (Geometry) getDatastoreMapping(0).getObject(datastoreResults, exprIndex[0]);

        if (geom == null)
        {
            return null;
        }

        if (mapUserdataObject)
        {
            Object userData = getDatastoreMapping(1).getObject(datastoreResults, exprIndex[1]);
            geom.setUserData(userData);
        }

        return geom;
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.mapping.JavaTypeMapping#setObject(org.datanucleus.ExecutionContext,
     * java.lang.Object, int[], java.lang.Object, org.datanucleus.state.ObjectProvider, int)
     */
    public void setObject(ExecutionContext ec, PreparedStatement ps, int[] exprIndex, Object value, ObjectProvider ownerSM,
            int ownerFieldNumber)
    {
        setObject(ec, ps, exprIndex, value);
    }

    public void setObject(ExecutionContext ec, PreparedStatement ps, int[] exprIndex, Object value)
    {
        Geometry geom = (Geometry) value;
        if (geom == null)
        {
            getDatastoreMapping(0).setObject(ps, exprIndex[0], null);
            if (exprIndex.length == 2)
            {
                getDatastoreMapping(1).setObject(ps, exprIndex[1], null);
            }
        }
        else
        {
            getDatastoreMapping(0).setObject(ps, exprIndex[0], geom);
            if (exprIndex.length == 2)
            {
                DatastoreMapping mapping = getDatastoreMapping(1);
                if (mapping instanceof AbstractDatastoreMapping && !((AbstractDatastoreMapping) mapping).insertValuesOnInsert())
                {

                }
                else
                {
                    getDatastoreMapping(1).setObject(ps, exprIndex[1], geom.getUserData());
                }
            }
        }
    }

    /**
     * Some nasty stuff that's only needed for Oracle.
     */
    public void insertPostProcessing(ObjectProvider op)
    {
        if (!mapUserdataObject || !(getDatastoreMapping(1) instanceof OracleBlobRDBMSMapping))
            return;

        Object geom = op.provideField(mmd.getAbsoluteFieldNumber());
        if (geom == null || !(geom instanceof Geometry) || ((Geometry) geom).getUserData() == null)
        {
            return;
        }

        Object value = ((Geometry) geom).getUserData();

        byte[] bytes = new byte[0];
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            bytes = baos.toByteArray();
        }
        catch (IOException e1)
        {
            // Do Nothing
        }

        // Update the BLOB
        OracleBlobRDBMSMapping.updateBlobColumn(op, getTable(), getDatastoreMapping(1), bytes);
    }

    public void postInsert(ObjectProvider op)
    {
        // do nothing
    }

    public void postUpdate(ObjectProvider op)
    {
        insertPostProcessing(op);
    }

    /**
     * Convenience method to return a clone of this mapping but without any user-data geometry.
     * @return The cloned mapping without the user-data component
     */
    public GeometryMapping getMappingWithoutUserData()
    {
        GeometryMapping geomMapping;
        try
        {
            geomMapping = getClass().newInstance();
            geomMapping.storeMgr = this.storeMgr;
            geomMapping.type = this.type;
            geomMapping.mmd = this.mmd;
            geomMapping.mapUserdataObject = this.mapUserdataObject;
            geomMapping.table = this.table;
            geomMapping.datastoreMappings = new DatastoreMapping[1];
            geomMapping.datastoreMappings[0] = this.datastoreMappings[0]; // Geometry
            return geomMapping;
        }
        catch (Exception e)
        {
            NucleusLogger.PERSISTENCE.info("Exception creating copy of mapping without user-data", e);
        }
        return this;
    }

    public void deleteDependent(ObjectProvider op)
    {
        // do nothing
    }

    public void postFetch(ObjectProvider op)
    {
        // do nothing
    }

    public void preDelete(ObjectProvider op)
    {
        // do nothing
    }
}