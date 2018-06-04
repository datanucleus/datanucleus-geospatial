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

import java.awt.geom.Arc2D;
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
 * Mapping for java.awt.geom.Arc2D.Double, maps the x, y, width, height, start and extent values to
 * double-precision datastore fields.
 */
public class Arc2dDoubleMapping extends SingleFieldMultiMapping
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
        addColumns(ClassNameConstants.INT); // Type
        addColumns(ClassNameConstants.DOUBLE); // X
        addColumns(ClassNameConstants.DOUBLE); // Y
        addColumns(ClassNameConstants.DOUBLE); // Width
        addColumns(ClassNameConstants.DOUBLE); // Height
        addColumns(ClassNameConstants.DOUBLE); // Start
        addColumns(ClassNameConstants.DOUBLE); // Extent
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getJavaType()
     */
    public Class getJavaType()
    {
        return Arc2D.Double.class;
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
        Arc2D.Double arc = (Arc2D.Double) value;
        if (index == 0)
        {
            return arc.getArcType();
        }
        else if (index == 1)
        {
            return arc.getX();
        }
        else if (index == 2)
        {
            return arc.getY();
        }
        else if (index == 3)
        {
            return arc.getWidth();
        }
        else if (index == 4)
        {
            return arc.getHeight();
        }
        else if (index == 5)
        {
            return arc.getAngleStart();
        }
        else if (index == 6)
        {
            return arc.getAngleExtent();
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
        Arc2D arc = (Arc2D) value;
        if (arc == null)
        {
            for (int i = 0; i < exprIndex.length; i++)
            {
                getColumnMapping(i).setObject(ps, exprIndex[i], null);
            }
        }
        else
        {
            getColumnMapping(0).setInt(ps, exprIndex[0], arc.getArcType());
            getColumnMapping(1).setDouble(ps, exprIndex[1], arc.getX());
            getColumnMapping(2).setDouble(ps, exprIndex[2], arc.getY());
            getColumnMapping(3).setDouble(ps, exprIndex[3], arc.getWidth());
            getColumnMapping(4).setDouble(ps, exprIndex[4], arc.getHeight());
            getColumnMapping(5).setDouble(ps, exprIndex[5], arc.getAngleStart());
            getColumnMapping(6).setDouble(ps, exprIndex[6], arc.getAngleExtent());
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

        int type = getColumnMapping(0).getInt(resultSet, exprIndex[0]);
        double x = getColumnMapping(1).getDouble(resultSet, exprIndex[1]);
        double y = getColumnMapping(2).getDouble(resultSet, exprIndex[2]);
        double width = getColumnMapping(3).getDouble(resultSet, exprIndex[3]);
        double height = getColumnMapping(4).getDouble(resultSet, exprIndex[4]);
        double start = getColumnMapping(5).getDouble(resultSet, exprIndex[5]);
        double extent = getColumnMapping(6).getDouble(resultSet, exprIndex[6]);
        return new Arc2D.Double(x, y, width, height, start, extent, type);
    }
}