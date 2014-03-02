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
package org.datanucleus.store.rdbms.mapping.jgeom2mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.spatial.geometry.JGeometry;
import oracle.spatial.util.ByteOrder;
import oracle.spatial.util.GeometryExceptionWithContext;
import oracle.spatial.util.WKB;

import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.mapping.mysql.MySQLSpatialRDBMSMapping;
import org.datanucleus.store.rdbms.schema.MySQLSpatialTypeInfo;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.rdbms.RDBMSStoreManager;

/**
 * Mapping for a JGeometry object to MySQL.
 */
public class JGeometryRDBMSMapping extends MySQLSpatialRDBMSMapping
{
    private WKB wkbConverter = new WKB(ByteOrder.LITTLE_ENDIAN);

    private static final SQLTypeInfo typeInfo;
    static
    {
        typeInfo = (SQLTypeInfo) MySQLSpatialTypeInfo.TYPEINFO_PROTOTYPE.clone();
        typeInfo.setTypeName("GEOMETRY");
        typeInfo.setLocalTypeName("GEOMETRY");
    }

    public JGeometryRDBMSMapping(JavaTypeMapping mapping, RDBMSStoreManager storeMgr, Column col)
    {
        super(mapping, storeMgr, col);
    }

    public SQLTypeInfo getTypeInfo()
    {
        return typeInfo;
    }

    public Object getObject(Object rs, int exprIndex)
    {
        Object value;

        try
        {
            byte[] mysqlBinary = ((ResultSet) rs).getBytes(exprIndex);
            if (((ResultSet) rs).wasNull() || mysqlBinary == null)
            {
                value = null;
            }
            else
            {
                value = wkbConverter.toJGeometry(mysqlBinaryToWkb(mysqlBinary));
                ((JGeometry) value).setSRID(mysqlBinaryToSrid(mysqlBinary));
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("getObject", exprIndex, e), e);
        }
        catch (GeometryExceptionWithContext e)
        {
            throw new NucleusDataStoreException(failureMessage("getObject", exprIndex, e), e);
        }

        return value;
    }

    public void setObject(Object ps, int exprIndex, Object value)
    {
        try
        {
            if (value == null)
            {
                ((PreparedStatement) ps).setNull(exprIndex, getTypeInfo().getDataType(), getTypeInfo().getTypeName());
            }
            else
            {
                byte[] wkb = wkbConverter.fromJGeometry((JGeometry) value);
                int srid = ((JGeometry) value).getSRID();
                ((PreparedStatement) ps).setBytes(exprIndex, wkbToMysqlBinary(wkb, srid));
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("setObject", value, e), e);
        }
        catch (GeometryExceptionWithContext e)
        {
            throw new NucleusDataStoreException(failureMessage("setObject", value, e), e);
        }
    }
}