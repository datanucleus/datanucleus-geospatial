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
package org.datanucleus.store.types.geospatial.rdbms.mapping.jts2oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleConnection;
import oracle.sql.STRUCT;

import org.datanucleus.store.types.geospatial.rdbms.schema.OracleSpatialTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.mapping.datastore.AbstractDatastoreMapping;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.oracle.OraReader;
import com.vividsolutions.jts.io.oracle.OraWriter;

/**
 * Mapping for a JTS Geometry object to Oracle.
 */
public class GeometryRDBMSMapping extends AbstractDatastoreMapping
{
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

    public int getJDBCType()
    {
        return OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY;
    }

    public Object getObject(ResultSet rs, int exprIndex)
    {
        Object value;

        try
        {
            Object st = rs.getObject(exprIndex);
            if (rs.wasNull() || st == null)
            {
                value = null;
            }
            else
            {
                value = new OraReader().read((STRUCT) st);
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
                ps.setNull(exprIndex, getJDBCType(), getTypeInfo().getTypeName());
            }
            else
            {
                OracleConnection conn = (OracleConnection) ps.getConnection();
                Object obj = new OraWriter(conn).write((Geometry) value);
                ps.setObject(exprIndex, obj);
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("setObject", value, e), e);
        }
    }
}