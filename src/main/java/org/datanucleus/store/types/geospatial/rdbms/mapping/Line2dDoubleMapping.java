/**********************************************************************
Copyright (c) 2007 Thomas Marti and others. All rights reserved.
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

import java.awt.geom.Line2D;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.ClassNameConstants;
import org.datanucleus.ExecutionContext;
import org.datanucleus.NucleusContext;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.mapping.java.SingleFieldMultiMapping;
import org.datanucleus.store.rdbms.table.Table;

/**
 * Mapping for java.awt.geom.Line2D.Double, maps the x1, y1, x2 and y2 values to double-precision datastore
 * fields.
 */
public class Line2dDoubleMapping extends SingleFieldMultiMapping
{
    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.mapping.JavaTypeMapping#initialize(AbstractMemberMetaData,
     * DatastoreContainerObject, ClassLoaderResolver)
     */
    public void initialize(AbstractMemberMetaData fmd, Table table, ClassLoaderResolver clr)
    {
        super.initialize(fmd, table, clr);
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
        addColumns(ClassNameConstants.DOUBLE); // X1
        addColumns(ClassNameConstants.DOUBLE); // Y1
        addColumns(ClassNameConstants.DOUBLE); // X2
        addColumns(ClassNameConstants.DOUBLE); // Y2
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getJavaType()
     */
    public Class getJavaType()
    {
        return Line2D.Double.class;
    }

    /**
     * Method to return the value to be stored in the specified datastore index given the overall value for
     * this java type.
     * @param index The datastore index
     * @param value The overall value for this java type
     * @return The value for this datastore index
     */
    public Object getValueForColumnMapping(NucleusContext nucleusCtx, int index, Object value)
    {
        Line2D.Double line = (Line2D.Double) value;
        if (index == 0)
        {
            return line.getX1();
        }
        else if (index == 1)
        {
            return line.getY1();
        }
        else if (index == 2)
        {
            return line.getX2();
        }
        else if (index == 3)
        {
            return line.getY2();
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
        Line2D line = (Line2D) value;
        if (line == null)
        {
            for (int i = 0; i < exprIndex.length; i++)
            {
                getColumnMapping(i).setObject(ps, exprIndex[i], null);
            }
        }
        else
        {
            getColumnMapping(0).setDouble(ps, exprIndex[0], line.getX1());
            getColumnMapping(1).setDouble(ps, exprIndex[1], line.getY1());
            getColumnMapping(2).setDouble(ps, exprIndex[2], line.getX2());
            getColumnMapping(3).setDouble(ps, exprIndex[3], line.getY2());
        }
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getObject(org.datanucleus.ExecutionContext,
     * java.lang.Object, int[])
     */
    public Object getObject(ExecutionContext ec, ResultSet resultSet, int[] exprIndex)
    {
        // Check for null entries
        if (getColumnMapping(0).getObject(resultSet, exprIndex[0]) == null)
        {
            return null;
        }

        double x1 = getColumnMapping(0).getDouble(resultSet, exprIndex[0]);
        double y1 = getColumnMapping(1).getDouble(resultSet, exprIndex[1]);
        double x2 = getColumnMapping(2).getDouble(resultSet, exprIndex[2]);
        double y2 = getColumnMapping(3).getDouble(resultSet, exprIndex[3]);
        return new Line2D.Double(x1, y1, x2, y2);
    }
}