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
package org.datanucleus.store.rdbms.mapping.pg2postgis;

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
import org.postgis.Geometry;
import org.postgis.PGgeometry;

/**
 * Mapping for a PostGIS-JDBC Geometry object to PostGIS.
 */
public class GeometryRDBMSMapping extends AbstractDatastoreMapping
{
    private static final SQLTypeInfo typeInfo;
    static
    {
        typeInfo = (SQLTypeInfo)PostGISTypeInfo.TYPEINFO_PROTOTYPE.clone();
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

    public Object getObject(ResultSet rs, int exprIndex)
    {
        Object value;

        try
        {
            Object result = ((ResultSet) rs).getObject(exprIndex);
            if (((ResultSet)rs).wasNull() || result == null)
            {
                value = null;
            }
            else
            {
                value = ((PGgeometry)result).getGeometry();
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
                ((PreparedStatement)ps).setNull(exprIndex, getTypeInfo().getDataType(), getTypeInfo().getTypeName());
            }
            else
            {
                Object obj = new PGgeometry((Geometry)value);
                ((PreparedStatement)ps).setObject(exprIndex, obj);
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("setObject", value, e), e);
        }
    }
}