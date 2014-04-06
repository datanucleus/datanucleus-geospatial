/**********************************************************************
Copyright (c) 2011 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.store.types.geospatial.wrapper;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.state.FetchPlanState;
import org.datanucleus.state.ObjectProvider;
import org.datanucleus.store.types.SCO;

/**
 * A mutable second-class java.awt.geom.CubicCurve2D.Double object.
 */
public class CubicCurve2dDouble extends java.awt.geom.CubicCurve2D.Double implements SCO
{
    protected transient ObjectProvider ownerOP;

    protected transient String fieldName;

    /**
     * Assigns owning object and field name.
     * @param ownerSM the owning object
     * @param mmd Metadata for the member
     */
    public CubicCurve2dDouble(ObjectProvider ownerSM, AbstractMemberMetaData mmd)
    {
        super();

        this.ownerOP = ownerSM;
        this.fieldName = mmd.getName();
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#initialise(java.lang.Object, boolean, boolean)
     */
    public void initialise(Object value, boolean forInsert, boolean forUpdate) throws ClassCastException
    {
        super.setCurve((CubicCurve2D.Double) value);
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#initialise()
     */
    public void initialise()
    {
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#getValue()
     */
    public Object getValue()
    {
        return new java.awt.geom.CubicCurve2D.Double(getX1(), getY1(), getCtrlX1(), getCtrlY1(), getX2(), getY2(), getCtrlX2(), getCtrlY2());
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#unsetOwner()
     */
    public void unsetOwner()
    {
        ownerOP = null;
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#getOwner()
     */
    public Object getOwner()
    {
        return (ownerOP != null ? ownerOP.getObject() : null);
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#getFieldName()
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * Utility to mark the object as dirty
     */
    public void makeDirty()
    {
        if (ownerOP != null)
        {
            ownerOP.getExecutionContext().getApiAdapter().makeDirty(ownerOP.getObject(), fieldName);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#detachCopy(org.datanucleus.state.FetchPlanState)
     */
    public Object detachCopy(FetchPlanState state)
    {
        return new java.awt.geom.CubicCurve2D.Double(getX1(), getY1(), getCtrlX1(), getCtrlY1(), getX2(), getY2(), getCtrlX2(), getCtrlY2());
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#attachCopy(java.lang.Object)
     */
    public void attachCopy(Object value)
    {
        double oldX1 = getX1();
        double oldY1 = getY1();
        double oldCX1 = getCtrlX1();
        double oldCY1 = getCtrlY1();
        double oldX2 = getX2();
        double oldY2 = getY2();
        double oldCX2 = getCtrlX2();
        double oldCY2 = getCtrlY2();
        initialise(value, false, true);

        // Check if the field has changed, and set the owner field as dirty if necessary
        CubicCurve2dDouble rect = (CubicCurve2dDouble) value;
        double newX1 = rect.getX1();
        double newY1 = rect.getY1();
        double newCX1 = rect.getCtrlX1();
        double newCY1 = rect.getCtrlY1();
        double newX2 = rect.getX2();
        double newY2 = rect.getY2();
        double newCX2 = rect.getCtrlX2();
        double newCY2 = rect.getCtrlY2();
        if (oldX1 != newX1 || oldY1 != newY1 || oldCX1 != newCX1 || oldCY1 != newCY1 || oldX2 != newX2 || oldY2 != newY2 || oldCX2 != newCX2 || oldCY2 != newCY2)
        {
            makeDirty();
        }
    }

    /**
     * Creates and returns a copy of this object.
     * <p>
     * Mutable second-class Objects are required to provide a public clone method in order to allow for
     * copying PersistenceCapable objects. In contrast to Object.clone(), this method must not throw a
     * CloneNotSupportedException.
     * @return A clone of the object
     */
    public Object clone()
    {
        Object obj = super.clone();
        ((CubicCurve2dDouble) obj).unsetOwner();
        return obj;
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.CubicCurve2D.Double#setCurve(double, double, double, double, double, double, double,
     * double)
     */
    @Override
    public void setCurve(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2)
    {
        super.setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.CubicCurve2D#setCurve(double[], int)
     */
    @Override
    public void setCurve(double[] coords, int offset)
    {
        super.setCurve(coords, offset);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.CubicCurve2D#setCurve(java.awt.geom.Point2D, java.awt.geom.Point2D,
     * java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public void setCurve(Point2D p1, Point2D cp1, Point2D cp2, Point2D p2)
    {
        super.setCurve(p1, cp1, cp2, p2);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.CubicCurve2D#setCurve(java.awt.geom.Point2D[], int)
     */
    @Override
    public void setCurve(Point2D[] pts, int offset)
    {
        super.setCurve(pts, offset);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.CubicCurve2D#setCurve(java.awt.geom.CubicCurve2D)
     */
    @Override
    public void setCurve(CubicCurve2D c)
    {
        super.setCurve(c);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.CubicCurve2D#subdivide(java.awt.geom.CubicCurve2D, java.awt.geom.CubicCurve2D)
     */
    @Override
    public void subdivide(CubicCurve2D left, CubicCurve2D right)
    {
        super.subdivide(left, right);
        makeDirty();
    }
}