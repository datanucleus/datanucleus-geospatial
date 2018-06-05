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
package org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.mapping.column.AbstractColumnMapping;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.store.types.geospatial.rdbms.adapter.PostGISTypeInfo;
import org.postgis.PGgeometry;
import org.postgis.jts.JtsGeometry;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Mapping for a JTS Geometry object to PostGIS.
 */
public class GeometryColumnMapping extends AbstractColumnMapping
{
    private static final SQLTypeInfo typeInfo;
    static
    {
        typeInfo = (SQLTypeInfo) PostGISTypeInfo.TYPEINFO_PROTOTYPE.clone();
        typeInfo.setLocalTypeName("GEOMETRY");
    }

    public GeometryColumnMapping(JavaTypeMapping mapping, RDBMSStoreManager storeMgr, Column col)
    {
        super(storeMgr, mapping);
        column = col;
        initialize();
    }

    private void initialize()
    {
        initTypeInfo();
    }

    public int getJDBCType()
    {
        return typeInfo.getDataType();
    }

    public SQLTypeInfo getTypeInfo()
    {
        return typeInfo;
    }

    public Object getObject(ResultSet rs, int exprIndex)
    {
        Object value = null;

        try
        {
            Object result = rs.getObject(exprIndex);
            if (!rs.wasNull() && result != null)
            {
                if (result instanceof JtsGeometry)
                {
                    value = ((JtsGeometry) result).getGeometry();
                }
                else if (result instanceof PGgeometry)
                {
                    try
                    {
                        value = convertGeometry((PGgeometry) result);
                    }
                    catch (ParseException e)
                    {
                        throw new NucleusDataStoreException(failureMessage("getObject", result, e), e);
                    }
                }

            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("getObject", exprIndex, e), e);
        }

        return value;
    }

    public void setObject(PreparedStatement ps, int exprIndex, Object value)
    {
        try
        {
            if (value == null)
            {
                ps.setNull(exprIndex, getTypeInfo().getDataType(), getTypeInfo().getTypeName());
            }
            else
            {
                Object obj = new JtsGeometry((Geometry) value);
                ps.setObject(exprIndex, obj);
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("setObject", value, e), e);
        }
    }

    private static WKTReader wktReader;

    private Geometry convertGeometry(PGgeometry pg) throws SQLException, ParseException
    {
        String geometryString = pg.getGeometry().toString();
        if (wktReader == null)
        {
            wktReader = new WKTReader();
        }
        Geometry geom;
        if (geometryString.indexOf(';') != -1)
        {
            String[] temp = PGgeometry.splitSRID(geometryString);
            int srid = Integer.parseInt(temp[0].substring(5));
            geom = wktReader.read(temp[1]);
            geom.setSRID(srid);
        }
        else
        {
            geom = wktReader.read(geometryString);
        }
        return geom;
    }

}