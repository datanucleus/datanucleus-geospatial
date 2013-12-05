/**********************************************************************
 Copyright (c) 2007 Roger Blum, Pascal N�esch and others. All rights reserved.
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
package org.datanucleus.store.rdbms.mapping.ao2postgis;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.datanucleus.store.rdbms.schema.PostGISTypeInfo;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.mapping.datastore.AbstractDatastoreMapping;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;

import com.esri.arcgis.geometry.GeometryEnvironment;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.SpatialReferenceEnvironment;
import com.esri.arcgis.geometry.esriSRGeoCSType;
import com.linar.jintegra.AutomationException;
import com.linar.jintegra.Variant;

/**
 * Mapping for a ESRI Geometry object to PostGIS.
 */
public abstract class GeometryRDBMSMapping extends AbstractDatastoreMapping
{
    private static final SQLTypeInfo typeInfo;
    static
    {
        typeInfo = (SQLTypeInfo) PostGISTypeInfo.TYPEINFO_PROTOTYPE.clone();
        typeInfo.setLocalTypeName("GEOMETRY");
    }

    public GeometryRDBMSMapping(JavaTypeMapping mapping, RDBMSStoreManager storeMgr, Column col)
    {
        super(storeMgr, mapping);
        column = col;
        initialize();
    }

    private void initialize()
    {
        initTypeInfo();
    }

    public SQLTypeInfo getTypeInfo()
    {
        return typeInfo;
    }

    public abstract Object getObject(ResultSet rs, int exprIndex);

    public void setObject(PreparedStatement ps, int exprIndex, Object value)
    {
        try
        {
            if (value == null)
            {
                ((PreparedStatement) ps).setNull(exprIndex, getTypeInfo().getDataType(), getTypeInfo().getTypeName());
            }
            else
            {
                byte[] wkb = convertToWkb(value);
                // int srid = getSRID(value);
                ((PreparedStatement) ps).setBytes(exprIndex, wkb);
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("setObject", value, e), e);
        }

    }

    public IGeometry getFromWkb(byte[] wkb)
    {
        IGeometry[] iGeom = new IGeometry[1];
        int[] byteCount = new int[1];

        byteCount[0] = wkb.length;
        GeometryEnvironment geoEnvironment = new GeometryEnvironment();
        geoEnvironment.createGeometryFromWkb(byteCount, wkb, iGeom);

        if (iGeom[0] != null)
        {
            int srid = esriSRGeoCSType.esriSRGeoCS_WGS1984;
            ISpatialReference sRef = new SpatialReferenceEnvironment().createSpatialReference(srid);
            iGeom[0].setSpatialReferenceByRef(sRef);
        }

        return iGeom[0];
    }

    public byte[] convertToWkb(Object geom)
    {
        GeometryEnvironment geoEnvironment = new GeometryEnvironment();
        Variant var = geoEnvironment.createWkbVariantFromGeometry((IGeometry) geom);
        if (var.getVT() == Variant.VT_I1)
        {
            return var.getI1Array();
        }
        else if (var.getVT() == Variant.VT_UI1)
        {
            return var.getUI1Array();
        }
        else
        {
            return null;
        }
    }

    public int getSRID(Object geom)
    {
        try
        {
            return ((IGeometry) geom).getSpatialReference().getSpatialReferenceImpl();
        }
        catch (AutomationException e)
        {
            return -1;
        }
    }

}