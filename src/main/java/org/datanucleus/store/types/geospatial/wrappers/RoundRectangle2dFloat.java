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
import java.awt.geom.RoundRectangle2D;

import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.FetchPlanState;
import org.datanucleus.state.DNStateManager;
import org.datanucleus.store.types.SCO;

/**
 * A mutable second-class java.awt.geom.RoundRectangle2D.Float object.
 */
public class RoundRectangle2dFloat extends java.awt.geom.RoundRectangle2D.Float implements SCO<java.awt.geom.RoundRectangle2D.Float>
{
    private static final long serialVersionUID = -4033294320394338528L;

    protected transient DNStateManager ownerOP;

    protected transient String fieldName;

    /**
     * Assigns owning object and field name.
     * @param ownerSM the owning object
     * @param mmd Metadata for the member
     */
    public RoundRectangle2dFloat(DNStateManager ownerSM, AbstractMemberMetaData mmd)
    {
        super();
        this.ownerOP = ownerSM;
        this.fieldName = mmd.getName();
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.types.SCO#initialise(java.lang.Object, java.lang.Object)
     */
    public void initialise(java.awt.geom.RoundRectangle2D.Float newValue, Object oldValue)
    {
        super.setRoundRect(newValue);
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#initialise(java.lang.Object)
     */
    public void initialise(java.awt.geom.RoundRectangle2D.Float value)
    {
        super.setRoundRect(value);
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
    public java.awt.geom.RoundRectangle2D.Float getValue()
    {
        return new java.awt.geom.RoundRectangle2D.Float((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight(),
                (float) getArcWidth(), (float) getArcHeight());
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
    public java.awt.geom.RoundRectangle2D.Float detachCopy(FetchPlanState state)
    {
        return new java.awt.geom.RoundRectangle2D.Float((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight(), (float) getArcWidth(), (float) getArcHeight());
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#attachCopy(java.lang.Object)
     */
    public void attachCopy(java.awt.geom.RoundRectangle2D.Float value)
    {
        double oldX = getX();
        double oldY = getY();
        double oldW = getWidth();
        double oldH = getHeight();
        double oldAW = getArcWidth();
        double oldAH = getArcHeight();
        initialise(value, null);

        // Check if the field has changed, and set the owner field as dirty if necessary
        RoundRectangle2dFloat rect = (RoundRectangle2dFloat) value;
        double newX = rect.getX();
        double newY = rect.getY();
        double newW = rect.getWidth();
        double newH = rect.getHeight();
        double newAW = rect.getArcWidth();
        double newAH = rect.getArcHeight();
        if (oldX != newX || oldY != newY || oldW != newW || oldH != newH || oldAW != newAW || oldAH != newAH)
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
        ((RoundRectangle2dFloat) obj).unsetOwner();
        return obj;
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.RoundRectangle2D.Float#setRoundRect(double, double, double, double, double, double)
     */
    @Override
    public void setRoundRect(double x, double y, double w, double h, double arcw, double arch)
    {
        super.setRoundRect(x, y, w, h, arcw, arch);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.RoundRectangle2D.Float#setRoundRect(float, float, float, float, float, float)
     */
    @Override
    public void setRoundRect(float x, float y, float w, float h, float arcw, float arch)
    {
        super.setRoundRect(x, y, w, h, arcw, arch);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.RoundRectangle2D.Double#setRoundRect(java.awt.geom.RoundRectangle2D)
     */
    @Override
    public void setRoundRect(RoundRectangle2D rr)
    {
        super.setRoundRect(rr);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.RoundRectangle2D#setFrame(double, double, double, double)
     */
    @Override
    public void setFrame(double x, double y, double w, double h)
    {
        super.setFrame(x, y, w, h);
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