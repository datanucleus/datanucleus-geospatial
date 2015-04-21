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
package org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.types.geospatial.rdbms.mapping.mysql.MySQLSpatialRDBMSMapping;
import org.datanucleus.store.types.geospatial.rdbms.schema.MySQLSpatialTypeInfo;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.rdbms.RDBMSStoreManager;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ByteOrderValues;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;

/**
 * Mapping for a JTS Geometry object to MySQL.
 */
public class GeometryRDBMSMapping extends MySQLSpatialRDBMSMapping
{
    private static final SQLTypeInfo typeInfo;
    static
    {
        typeInfo = (SQLTypeInfo) MySQLSpatialTypeInfo.TYPEINFO_PROTOTYPE.clone();
        typeInfo.setTypeName("GEOMETRY");
        typeInfo.setLocalTypeName("GEOMETRY");
    }

    public GeometryRDBMSMapping(JavaTypeMapping mapping, RDBMSStoreManager storeMgr, Column col)
    {
        super(mapping, storeMgr, col);
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
        Object value;

        try
        {
            byte[] mysqlBinary = rs.getBytes(exprIndex);
            if (rs.wasNull() || mysqlBinary == null)
            {
                value = null;
            }
            else
            {
                WKBReader wkbReader = new WKBReader();
                value = wkbReader.read(mysqlBinaryToWkb(mysqlBinary));
                ((Geometry) value).setSRID(mysqlBinaryToSrid(mysqlBinary));
            }
        }
        catch (Exception e)
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
                WKBWriter wkbWriter = new WKBWriter(2, ByteOrderValues.LITTLE_ENDIAN);
                byte[] wkb = wkbWriter.write((Geometry) value);
                int srid = ((Geometry) value).getSRID();
                ps.setBytes(exprIndex, wkbToMysqlBinary(wkb, srid));
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("setObject", value, e), e);
        }
    }
}