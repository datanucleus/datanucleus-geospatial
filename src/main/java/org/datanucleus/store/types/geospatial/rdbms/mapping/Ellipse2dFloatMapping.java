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

import java.awt.geom.Ellipse2D;
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
 * Mapping for java.awt.geom.Ellipse2D.Float, maps the x, y, width and height values to float-precision
 * datastore fields.
 */
public class Ellipse2dFloatMapping extends SingleFieldMultiMapping
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
        addColumns(ClassNameConstants.FLOAT); // X
        addColumns(ClassNameConstants.FLOAT); // Y
        addColumns(ClassNameConstants.FLOAT); // Width
        addColumns(ClassNameConstants.FLOAT); // Height
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getJavaType()
     */
    public Class getJavaType()
    {
        return Ellipse2D.Float.class;
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
        Ellipse2D.Float el = (Ellipse2D.Float) value;
        if (index == 0)
        {
            return el.getX();
        }
        else if (index == 1)
        {
            return el.getY();
        }
        else if (index == 2)
        {
            return el.getWidth();
        }
        else if (index == 3)
        {
            return el.getHeight();
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
        Ellipse2D.Float ellipse = (Ellipse2D.Float) value;
        if (ellipse == null)
        {
            for (int i = 0; i < exprIndex.length; i++)
            {
                getColumnMapping(i).setObject(ps, exprIndex[i], null);
            }
        }
        else
        {
            getColumnMapping(0).setFloat(ps, exprIndex[0], ellipse.x);
            getColumnMapping(1).setFloat(ps, exprIndex[1], ellipse.y);
            getColumnMapping(2).setFloat(ps, exprIndex[2], ellipse.width);
            getColumnMapping(3).setFloat(ps, exprIndex[3], ellipse.height);
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

        float x = getColumnMapping(0).getFloat(resultSet, exprIndex[0]);
        float y = getColumnMapping(1).getFloat(resultSet, exprIndex[1]);
        float width = getColumnMapping(2).getFloat(resultSet, exprIndex[2]);
        float height = getColumnMapping(3).getFloat(resultSet, exprIndex[3]);
        return new Ellipse2D.Float(x, y, width, height);
    }
}