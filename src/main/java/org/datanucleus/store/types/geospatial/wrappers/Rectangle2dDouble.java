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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.state.FetchPlanState;
import org.datanucleus.state.ObjectProvider;
import org.datanucleus.store.types.SCO;

/**
 * A mutable second-class java.awt.geom.Rectangle2D.Double object.
 */
public class Rectangle2dDouble extends java.awt.geom.Rectangle2D.Double implements SCO<java.awt.geom.Rectangle2D.Double>
{
    private static final long serialVersionUID = -348539619563292726L;

    protected transient ObjectProvider ownerOP;

    protected transient String fieldName;

    /**
     * Assigns owning object and field name.
     * @param ownerSM the owning object
     * @param mmd Metadata for the member
     */
    public Rectangle2dDouble(ObjectProvider ownerSM, AbstractMemberMetaData mmd)
    {
        super();

        this.ownerOP = ownerSM;
        this.fieldName = mmd.getName();
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.types.SCO#initialise(java.lang.Object, java.lang.Object)
     */
    public void initialise(java.awt.geom.Rectangle2D.Double newValue, Object oldValue)
    {
        super.setRect(newValue);
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#initialise(java.lang.Object)
     */
    public void initialise(java.awt.geom.Rectangle2D.Double value)
    {
        super.setRect(value);
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
    public java.awt.geom.Rectangle2D.Double getValue()
    {
        return new java.awt.geom.Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
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
    public java.awt.geom.Rectangle2D.Double detachCopy(FetchPlanState state)
    {
        return new java.awt.geom.Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#attachCopy(java.lang.Object)
     */
    public void attachCopy(java.awt.geom.Rectangle2D.Double value)
    {
        double oldX = getX();
        double oldY = getY();
        double oldW = getWidth();
        double oldH = getHeight();
        initialise(value, null);

        // Check if the field has changed, and set the owner field as dirty if necessary
        Rectangle2dDouble rect = (Rectangle2dDouble) value;
        double newX = rect.getX();
        double newY = rect.getY();
        double newW = rect.getWidth();
        double newH = rect.getHeight();
        if (oldX != newX || oldY != newY || oldW != newW || oldH != newH)
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
        ((Rectangle2dDouble) obj).unsetOwner();
        return obj;
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Rectangle2D.Double#setRect(double, double, double, double)
     */
    @Override
    public void setRect(double x, double y, double w, double h)
    {
        super.setRect(x, y, w, h);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Rectangle2D.Double#setRect(java.awt.geom.Rectangle2D)
     */
    @Override
    public void setRect(Rectangle2D r)
    {
        super.setRect(r);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Rectangle2D#setFrame(double, double, double, double)
     */
    @Override
    public void setFrame(double x, double y, double w, double h)
    {
        super.setFrame(x, y, w, h);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Rectangle2D#add(double, double)
     */
    @Override
    public void add(double newx, double newy)
    {
        super.add(newx, newy);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Rectangle2D#add(java.awt.geom.Point2D)
     */
    @Override
    public void add(Point2D pt)
    {
        super.add(pt);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Rectangle2D#add(java.awt.geom.Rectangle2D)
     */
    @Override
    public void add(Rectangle2D r)
    {
        super.add(r);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.RectangularShape#setFrame(java.awt.geom.Point2D, java.awt.geom.Dimension2D)
     */
    @Override
    public void setFrame(Point2D loc, Dimension2D size)
    {
        super.setFrame(loc, size);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.RectangularShape#setFrame(java.awt.geom.Rectangle2D)
     */
    @Override
    public void setFrame(Rectangle2D r)
    {
        super.setFrame(r);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.RectangularShape#setFrameFromDiagonal(double, double, double, double)
     */
    @Override
    public void setFrameFromDiagonal(double x1, double y1, double x2, double y2)
    {
        super.setFrameFromDiagonal(x1, y1, x2, y2);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.RectangularShape#setFrameFromDiagonal(java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public void setFrameFromDiagonal(Point2D p1, Point2D p2)
    {
        super.setFrameFromDiagonal(p1, p2);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.RectangularShape#setFrameFromCenter(double, double, double, double)
     */
    @Override
    public void setFrameFromCenter(double centerX, double centerY, double cornerX, double cornerY)
    {
        super.setFrameFromCenter(centerX, centerY, cornerX, cornerY);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.RectangularShape#setFrameFromCenter(java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public void setFrameFromCenter(Point2D center, Point2D corner)
    {
        super.setFrameFromCenter(center, corner);
        makeDirty();
    }
}