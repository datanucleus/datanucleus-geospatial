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

import java.awt.geom.CubicCurve2D;
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
 * Mapping for java.awt.geom.CubicCurve2D.Double, maps the x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2 and y2
 * values to double-precision datastore fields.
 */
public class CubicCurve2dDoubleMapping extends SingleFieldMultiMapping
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
        addColumns(ClassNameConstants.DOUBLE); // CtrlX1
        addColumns(ClassNameConstants.DOUBLE); // CtrlY1
        addColumns(ClassNameConstants.DOUBLE); // CtrlX2
        addColumns(ClassNameConstants.DOUBLE); // CtrlY2
        addColumns(ClassNameConstants.DOUBLE); // X2
        addColumns(ClassNameConstants.DOUBLE); // Y2
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getJavaType()
     */
    public Class getJavaType()
    {
        return CubicCurve2D.Double.class;
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
        CubicCurve2D.Double cc = (CubicCurve2D.Double) value;
        if (index == 0)
        {
            return cc.getX1();
        }
        else if (index == 1)
        {
            return cc.getY1();
        }
        else if (index == 2)
        {
            return cc.getCtrlX1();
        }
        else if (index == 3)
        {
            return cc.getCtrlY1();
        }
        else if (index == 4)
        {
            return cc.getCtrlX2();
        }
        else if (index == 5)
        {
            return cc.getCtrlY2();
        }
        else if (index == 6)
        {
            return cc.getX2();
        }
        else if (index == 7)
        {
            return cc.getY2();
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
        CubicCurve2D.Double cubicCurve = (CubicCurve2D.Double) value;
        if (cubicCurve == null)
        {
            for (int i = 0; i < exprIndex.length; i++)
            {
                getDatastoreMapping(i).setObject(ps, exprIndex[i], null);
            }
        }
        else
        {
            getDatastoreMapping(0).setDouble(ps, exprIndex[0], cubicCurve.getX1());
            getDatastoreMapping(1).setDouble(ps, exprIndex[1], cubicCurve.getY1());
            getDatastoreMapping(2).setDouble(ps, exprIndex[2], cubicCurve.getCtrlX1());
            getDatastoreMapping(3).setDouble(ps, exprIndex[3], cubicCurve.getCtrlY1());
            getDatastoreMapping(4).setDouble(ps, exprIndex[4], cubicCurve.getCtrlX2());
            getDatastoreMapping(5).setDouble(ps, exprIndex[5], cubicCurve.getCtrlY2());
            getDatastoreMapping(6).setDouble(ps, exprIndex[6], cubicCurve.getX2());
            getDatastoreMapping(7).setDouble(ps, exprIndex[7], cubicCurve.getY2());
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

        double x1 = getDatastoreMapping(0).getDouble(resultSet, exprIndex[0]);
        double y1 = getDatastoreMapping(1).getDouble(resultSet, exprIndex[1]);
        double ctrlx1 = getDatastoreMapping(2).getDouble(resultSet, exprIndex[2]);
        double ctrly1 = getDatastoreMapping(3).getDouble(resultSet, exprIndex[3]);
        double ctrlx2 = getDatastoreMapping(4).getDouble(resultSet, exprIndex[4]);
        double ctrly2 = getDatastoreMapping(5).getDouble(resultSet, exprIndex[5]);
        double x2 = getDatastoreMapping(6).getDouble(resultSet, exprIndex[6]);
        double y2 = getDatastoreMapping(7).getDouble(resultSet, exprIndex[7]);
        return new CubicCurve2D.Double(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
    }
}