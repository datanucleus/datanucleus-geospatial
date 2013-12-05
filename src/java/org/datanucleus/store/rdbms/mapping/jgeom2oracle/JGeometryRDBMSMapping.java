/**********************************************************************
 Copyright (c) 2006 Erik Bengtson and others. All rights reserved.
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
 2006 Thomas Marti & Stefan Schmid - Adapted for new JPOX-Spatial plugin
 ...
 **********************************************************************/
package org.datanucleus.store.rdbms.mapping.jgeom2oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import org.datanucleus.store.rdbms.schema.OracleTypeInfo;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.mapping.datastore.AbstractDatastoreMapping;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;

/**
 * Mapping for a JGeometry object to Oracle.
 */
public class JGeometryRDBMSMapping extends AbstractDatastoreMapping
{
    public JGeometryRDBMSMapping(JavaTypeMapping mapping, RDBMSStoreManager storeMgr, Column col)
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
        return ((RDBMSStoreManager)storeMgr).getSQLTypeInfoForJDBCType(OracleTypeInfo.TYPES_SDO_GEOMETRY);
    }

    public Object getObject(ResultSet rs, int exprIndex)
    {
        Object value;

        try
        {
            Object st = ((ResultSet) rs).getObject(exprIndex);
            if (((ResultSet)rs).wasNull() || st == null)
            {
                value = null;
            }
            else
            {
                value = JGeometry.load((STRUCT)st);
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
                ((PreparedStatement) ps).setNull(exprIndex, getTypeInfo().getDataType(), getTypeInfo().getTypeName());
            }
            else
            {
                Object obj = JGeometry.store((JGeometry)value, ((PreparedStatement)ps).getConnection());
                ((PreparedStatement) ps).setObject(exprIndex, obj);
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("setObject", value, e),e );
        }
    }
}
