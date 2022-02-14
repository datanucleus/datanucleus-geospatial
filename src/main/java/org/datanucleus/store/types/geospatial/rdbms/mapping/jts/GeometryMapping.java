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
import org.datanucleus.state.DNStateManager;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.mapping.column.ColumnMapping;
import org.datanucleus.store.rdbms.mapping.column.ColumnMappingPostSet;
import org.datanucleus.store.rdbms.mapping.column.OracleBlobColumnMapping;
import org.datanucleus.store.rdbms.mapping.java.SingleFieldMultiMapping;
import org.datanucleus.store.rdbms.table.Table;
import org.datanucleus.util.NucleusLogger;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Mapping for JTS Geometry to its datastore representation.
 */
public class GeometryMapping extends SingleFieldMultiMapping
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

    public String getJavaTypeForColumnMapping(int index)
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

    @Override
    public Object getObject(ExecutionContext ec, ResultSet rs, int[] exprIndex, DNStateManager ownerSM, int ownerFieldNumber)
    {
        return getObject(ec, rs, exprIndex);
    }

    public Object getObject(ExecutionContext ec, ResultSet datastoreResults, int[] exprIndex)
    {
        Geometry geom = (Geometry) getColumnMapping(0).getObject(datastoreResults, exprIndex[0]);

        if (geom == null)
        {
            return null;
        }

        if (mapUserdataObject)
        {
            Object userData = getColumnMapping(1).getObject(datastoreResults, exprIndex[1]);
            geom.setUserData(userData);
        }

        return geom;
    }

    @Override
    public void setObject(ExecutionContext ec, PreparedStatement ps, int[] exprIndex, Object value, DNStateManager ownerSM,
            int ownerFieldNumber)
    {
        setObject(ec, ps, exprIndex, value);
    }

    public void setObject(ExecutionContext ec, PreparedStatement ps, int[] exprIndex, Object value)
    {
        Geometry geom = (Geometry) value;
        if (geom == null)
        {
            getColumnMapping(0).setObject(ps, exprIndex[0], null);
            if (exprIndex.length == 2)
            {
                getColumnMapping(1).setObject(ps, exprIndex[1], null);
            }
        }
        else
        {
            getColumnMapping(0).setObject(ps, exprIndex[0], geom);
            if (exprIndex.length == 2)
            {
                ColumnMapping mapping = getColumnMapping(1);
                if (mapping.insertValuesOnInsert())
                {
                    getColumnMapping(1).setObject(ps, exprIndex[1], geom.getUserData());
                }
            }
        }
    }

    /**
     * Oracle specific handling for BLOB/CLOBs (for the userdata), where it inserts an empty BLOB/CLOB and then you put the value in after.
     * @param sm StateManager
     */
    public void setValuePostProcessing(DNStateManager sm)
    {
        if (!mapUserdataObject || !(columnMappings[1] instanceof OracleBlobColumnMapping))
        {
            return;
        }
        Object geom = sm.provideField(mmd.getAbsoluteFieldNumber());
        if (geom == null || !(geom instanceof Geometry) || ((Geometry) geom).getUserData() == null)
        {
            return;
        }

        // Create the value to put into the BLOB
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
        ((ColumnMappingPostSet)columnMappings[1]).setPostProcessing(sm, bytes);
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
            geomMapping = getClass().getDeclaredConstructor().newInstance();
            geomMapping.storeMgr = this.storeMgr;
            geomMapping.type = this.type;
            geomMapping.mmd = this.mmd;
            geomMapping.mapUserdataObject = this.mapUserdataObject;
            geomMapping.table = this.table;
            geomMapping.columnMappings = new ColumnMapping[1];
            geomMapping.columnMappings[0] = this.columnMappings[0]; // Geometry
            return geomMapping;
        }
        catch (Exception e)
        {
            NucleusLogger.PERSISTENCE.info("Exception creating copy of mapping without user-data", e);
        }
        return this;
    }
}