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
package org.datanucleus.store.rdbms.mapping;

import java.awt.geom.QuadCurve2D;
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
 * Mapping for java.awt.geom.QuadCurve2D.Float, maps the x1, y1, ctrlx, ctrly, x2 and y2 values to
 * float-precision datastore fields.
 */
public class QuadCurve2dFloatMapping extends SingleFieldMultiMapping
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
        addColumns(ClassNameConstants.FLOAT); // X1
        addColumns(ClassNameConstants.FLOAT); // Y1
        addColumns(ClassNameConstants.FLOAT); // CtrlX
        addColumns(ClassNameConstants.FLOAT); // CtrlY
        addColumns(ClassNameConstants.FLOAT); // X2
        addColumns(ClassNameConstants.FLOAT); // Y2
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getJavaType()
     */
    public Class getJavaType()
    {
        return QuadCurve2D.Float.class;
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
        QuadCurve2D.Float qc = (QuadCurve2D.Float) value;
        if (index == 0)
        {
            return qc.getX1();
        }
        else if (index == 1)
        {
            return qc.getY1();
        }
        else if (index == 2)
        {
            return qc.getCtrlX();
        }
        else if (index == 3)
        {
            return qc.getCtrlY();
        }
        else if (index == 4)
        {
            return qc.getX2();
        }
        else if (index == 5)
        {
            return qc.getY2();
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
        QuadCurve2D.Float line = (QuadCurve2D.Float) value;
        if (line == null)
        {
            for (int i = 0; i < exprIndex.length; i++)
            {
                getDatastoreMapping(i).setObject(ps, exprIndex[i], null);
            }
        }
        else
        {
            getDatastoreMapping(0).setFloat(ps, exprIndex[0], line.x1);
            getDatastoreMapping(1).setFloat(ps, exprIndex[1], line.y1);
            getDatastoreMapping(2).setFloat(ps, exprIndex[2], line.ctrlx);
            getDatastoreMapping(3).setFloat(ps, exprIndex[3], line.ctrly);
            getDatastoreMapping(4).setFloat(ps, exprIndex[4], line.x2);
            getDatastoreMapping(5).setFloat(ps, exprIndex[5], line.y2);
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
        if (getDatastoreMapping(0).getObject(resultSet, exprIndex[0]) == null)
        {
            return null;
        }

        float x1 = getDatastoreMapping(0).getFloat(resultSet, exprIndex[0]);
        float y1 = getDatastoreMapping(1).getFloat(resultSet, exprIndex[1]);
        float ctrlx = getDatastoreMapping(2).getFloat(resultSet, exprIndex[2]);
        float ctrly = getDatastoreMapping(3).getFloat(resultSet, exprIndex[3]);
        float x2 = getDatastoreMapping(4).getFloat(resultSet, exprIndex[5]);
        float y2 = getDatastoreMapping(5).getFloat(resultSet, exprIndex[6]);
        return new QuadCurve2D.Float(x1, y1, ctrlx, ctrly, x2, y2);
    }
}