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
 * Mapping for java.awt.geom.QuadCurve2D.Double, maps the x1, y1, ctrlx, ctrly, x2 and y2 values to double-precision
 * datastore fields.
 */
public class QuadCurve2dDoubleMapping extends SingleFieldMultiMapping
{
    /* (non-Javadoc)
     * @see org.datanucleus.store.rdbms.mapping.JavaTypeMapping#initialize(AbstractMemberMetaData, DatastoreContainerObject, ClassLoaderResolver)
     */
    public void initialize(AbstractMemberMetaData fmd, Table table, ClassLoaderResolver clr)
    {
        super.initialize(fmd, table, clr);
        addColumns();
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.rdbms.mapping.JavaTypeMapping#initialize(RDBMSStoreManager, java.lang.String)
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
        addColumns(ClassNameConstants.DOUBLE); // CtrlX
        addColumns(ClassNameConstants.DOUBLE); // CtrlY
        addColumns(ClassNameConstants.DOUBLE); // X2
        addColumns(ClassNameConstants.DOUBLE); // Y2
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getJavaType()
     */
    public Class getJavaType()
    {
        return QuadCurve2D.Double.class;
    }

    /**
     * Method to return the value to be stored in the specified datastore index given the overall
     * value for this java type.
     * @param index The datastore index
     * @param value The overall value for this java type
     * @return The value for this datastore index
     */
    public Object getValueForDatastoreMapping(NucleusContext nucleusCtx, int index, Object value)
    {
        QuadCurve2D.Double qc = (QuadCurve2D.Double)value;
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

    /* (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#setObject(org.datanucleus.ExecutionContext, java.lang.Object, int[], java.lang.Object)
     */
    public void setObject(ExecutionContext ec, PreparedStatement ps, int[] exprIndex, Object value)
    {
    	QuadCurve2D line = (QuadCurve2D)value;
        if (line == null)
        {
    		for (int i = 0; i < exprIndex.length; i++) 
    		{
    			getDatastoreMapping(i).setObject(ps, exprIndex[i], null);					
			}
        }
        else
        {
            getDatastoreMapping(0).setDouble(ps,exprIndex[0],line.getX1());
            getDatastoreMapping(1).setDouble(ps,exprIndex[1],line.getY1());
            getDatastoreMapping(2).setDouble(ps,exprIndex[2],line.getCtrlX());
            getDatastoreMapping(3).setDouble(ps,exprIndex[3],line.getCtrlY());
            getDatastoreMapping(4).setDouble(ps,exprIndex[4],line.getX2());
            getDatastoreMapping(5).setDouble(ps,exprIndex[5],line.getY2());
        }
    }
    
    /* (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getObject(org.datanucleus.ExecutionContext, java.lang.Object, int[])
     */
    public Object getObject(ExecutionContext ec, ResultSet resultSet, int[] exprIndex)
    {
        // Check for null entries
        if (getDatastoreMapping(0).getObject(resultSet, exprIndex[0]) == null)
        {
            return null;
        }

        double x1 = getDatastoreMapping(0).getDouble(resultSet,exprIndex[0]); 
        double y1 = getDatastoreMapping(1).getDouble(resultSet,exprIndex[1]); 
        double ctrlx = getDatastoreMapping(2).getDouble(resultSet,exprIndex[2]); 
        double ctrly = getDatastoreMapping(3).getDouble(resultSet,exprIndex[3]);
        double x2 = getDatastoreMapping(4).getDouble(resultSet,exprIndex[5]); 
        double y2 = getDatastoreMapping(5).getDouble(resultSet,exprIndex[6]);
        return new QuadCurve2D.Double(x1, y1, ctrlx, ctrly, x2, y2);
    }
}