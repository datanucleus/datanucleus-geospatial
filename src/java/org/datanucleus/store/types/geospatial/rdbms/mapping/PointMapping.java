/**********************************************************************
Copyright (c) 2005 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.store.types.geospatial.rdbms.mapping;

import java.awt.Point;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.ClassNameConstants;
import org.datanucleus.ExecutionContext;
import org.datanucleus.NucleusContext;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.store.rdbms.mapping.java.SingleFieldMultiMapping;
import org.datanucleus.store.rdbms.table.Table;
import org.datanucleus.store.rdbms.RDBMSStoreManager;

/**
 * Mapping for java.awt.Point to its datastore representation.
 */
public class PointMapping extends SingleFieldMultiMapping
{
    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.mapping.JavaTypeMapping#initialize(AbstractMemberMetaData,
     * DatastoreContainerObject, ClassLoaderResolver)
     */
    public void initialize(AbstractMemberMetaData mmd, Table table, ClassLoaderResolver clr)
    {
        super.initialize(mmd, table, clr);
        addColumns();
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.mapping.JavaTypeMapping#initialize(RDBMSStoreManager,
     * java.lang.String)
     */
    public void initialize(RDBMSStoreManager storeMgr, String type)
    {
        super.initialize(storeMgr, type);
        addColumns();
    }

    protected void addColumns()
    {
        addColumns(ClassNameConstants.INT); // X
        addColumns(ClassNameConstants.INT); // Y
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getJavaType()
     */
    public Class getJavaType()
    {
        return Point.class;
    }

    /**
     * Method to return the value to be stored in the specified datastore index given the overall value for
     * this java type.
     * @param index The datastore index
     * @param value The overall value for this java type
     * @return The value for this datastore index
     */
    public Object getValueForDatastoreMapping(NucleusContext nucleusCtx, int index, Object value)
    {
        Point pt = (Point) value;
        if (index == 0)
        {
            return pt.getX();
        }
        else if (index == 1)
        {
            return pt.getY();
        }
        throw new IndexOutOfBoundsException();
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#setObject(org.datanucleus.ExecutionContext,
     * java.lang.Object, int[], java.lang.Object)
     */
    public void setObject(ExecutionContext ec, PreparedStatement ps, int[] exprIndex, Object value)
    {
        Point pt = (Point) value;
        if (pt == null)
        {
            getDatastoreMapping(0).setObject(ps, exprIndex[0], null);
            getDatastoreMapping(1).setObject(ps, exprIndex[1], null);
        }
        else
        {
            getDatastoreMapping(0).setInt(ps, exprIndex[0], (int) pt.getX());
            getDatastoreMapping(1).setInt(ps, exprIndex[1], (int) pt.getY());
        }
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getObject(org.datanucleus.ExecutionContext,
     * java.lang.Object, int[])
     */
    public Object getObject(ExecutionContext ec, ResultSet resultSet, int[] exprIndex)
    {
        try
        {
            // Check for null entries, just by checking first datastore mapping for null
            if (getDatastoreMapping(0).getObject(resultSet, exprIndex[0]) == null)
            {
                return null;
            }
        }
        catch (Exception e)
        {
            // Do nothing
        }

        int x = getDatastoreMapping(0).getInt(resultSet, exprIndex[0]);
        int y = getDatastoreMapping(1).getInt(resultSet, exprIndex[1]);
        return new Point(x, y);
    }
}