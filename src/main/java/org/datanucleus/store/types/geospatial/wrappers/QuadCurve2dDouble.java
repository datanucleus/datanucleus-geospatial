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
package org.datanucleus.store.types.geospatial.wrappers;

import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.FetchPlanState;
import org.datanucleus.state.DNStateManager;
import org.datanucleus.store.types.SCO;

/**
 * A mutable second-class java.awt.geom.QuadCurve2D.Double object.
 */
public class QuadCurve2dDouble extends java.awt.geom.QuadCurve2D.Double implements SCO<java.awt.geom.QuadCurve2D.Double>
{
    private static final long serialVersionUID = -7376523376935310375L;

    protected transient DNStateManager ownerOP;

    protected transient String fieldName;

    /**
     * Assigns owning object and field name.
     * @param ownerSM the owning object
     * @param mmd Metadata for the member
     */
    public QuadCurve2dDouble(DNStateManager ownerSM, AbstractMemberMetaData mmd)
    {
        super();

        this.ownerOP = ownerSM;
        this.fieldName = mmd.getName();
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.types.SCO#initialise(java.lang.Object, java.lang.Object)
     */
    public void initialise(java.awt.geom.QuadCurve2D.Double newValue, Object oldValue)
    {
        super.setCurve(newValue);
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#initialise(java.lang.Object)
     */
    public void initialise(java.awt.geom.QuadCurve2D.Double value)
    {
        super.setCurve(value);
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
    public java.awt.geom.QuadCurve2D.Double getValue()
    {
        return new java.awt.geom.QuadCurve2D.Double(getX1(), getY1(), getCtrlX(), getCtrlY(), getX2(), getY2());
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
    public java.awt.geom.QuadCurve2D.Double detachCopy(FetchPlanState state)
    {
        return new java.awt.geom.QuadCurve2D.Double(getX1(), getY1(), getCtrlX(), getCtrlY(), getX2(), getY2());
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#attachCopy(java.lang.Object)
     */
    public void attachCopy(java.awt.geom.QuadCurve2D.Double value)
    {
        double oldX1 = getX1();
        double oldY1 = getY1();
        double oldCX = getCtrlX();
        double oldCY = getCtrlY();
        double oldX2 = getX2();
        double oldY2 = getY2();
        initialise(value, null);

        // Check if the field has changed, and set the owner field as dirty if necessary
        QuadCurve2dDouble rect = (QuadCurve2dDouble) value;
        double newX1 = rect.getX1();
        double newY1 = rect.getY1();
        double newCX = rect.getCtrlX();
        double newCY = rect.getCtrlY();
        double newX2 = rect.getX2();
        double newY2 = rect.getY2();
        if (oldX1 != newX1 || oldY1 != newY1 || oldCX != newCX || oldCY != newCY || oldX2 != newX2 || oldY2 != newY2)
        {
            makeDirty();
        }
    }

    /**
     * Creates and returns a copy of this object.
     * <p>
     * Mutable second-class Objects are required to provide a public clone method in order to allow for
     * copying persistable objects. In contrast to Object.clone(), this method must not throw a
     * CloneNotSupportedException.
     * @return A clone of the object
     */
    public Object clone()
    {
        Object obj = super.clone();
        ((QuadCurve2dDouble) obj).unsetOwner();
        return obj;
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.QuadCurve2D.Double#setCurve(double, double, double, double, double, double)
     */
    @Override
    public void setCurve(double x1, double y1, double ctrlx, double ctrly, double x2, double y2)
    {
        super.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.QuadCurve2D#setCurve(double[], int)
     */
    @Override
    public void setCurve(double[] coords, int offset)
    {
        super.setCurve(coords, offset);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.QuadCurve2D#setCurve(java.awt.geom.Point2D, java.awt.geom.Point2D,
     * java.awt.geom.Point2D)
     */
    @Override
    public void setCurve(Point2D p1, Point2D cp, Point2D p2)
    {
        super.setCurve(p1, cp, p2);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.QuadCurve2D#setCurve(java.awt.geom.Point2D[], int)
     */
    @Override
    public void setCurve(Point2D[] pts, int offset)
    {
        super.setCurve(pts, offset);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.QuadCurve2D#setCurve(java.awt.geom.QuadCurve2D)
     */
    @Override
    public void setCurve(QuadCurve2D c)
    {
        super.setCurve(c);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.QuadCurve2D#subdivide(java.awt.geom.QuadCurve2D, java.awt.geom.QuadCurve2D)
     */
    @Override
    public void subdivide(QuadCurve2D left, QuadCurve2D right)
    {
        super.subdivide(left, right);
        makeDirty();
    }
}