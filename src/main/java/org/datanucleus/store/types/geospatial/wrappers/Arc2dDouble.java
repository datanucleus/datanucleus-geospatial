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

import java.awt.geom.Arc2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.state.FetchPlanState;
import org.datanucleus.state.ObjectProvider;
import org.datanucleus.store.types.SCO;

/**
 * A mutable second-class java.awt.geom.Arc2D.Double object.
 */
public class Arc2dDouble extends java.awt.geom.Arc2D.Double implements SCO
{
    private static final long serialVersionUID = 7104674022231272950L;

    protected transient ObjectProvider ownerOP;

    protected transient String fieldName;

    /**
     * Assigns owning object and field name.
     * @param ownerSM the owning object
     * @param mmd Metadata for the member
     */
    public Arc2dDouble(ObjectProvider ownerSM, AbstractMemberMetaData mmd)
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
        super.setArc((Arc2D.Double) value);
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
        return new java.awt.geom.Arc2D.Double(getX(), getY(), getWidth(), getHeight(), getAngleStart(), getAngleExtent(), getArcType());
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
        return new java.awt.geom.Arc2D.Double(getX(), getY(), getWidth(), getHeight(), getAngleStart(), getAngleExtent(), getArcType());
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#attachCopy(java.lang.Object)
     */
    public void attachCopy(Object value)
    {
        double oldX = getX();
        double oldY = getY();
        double oldW = getWidth();
        double oldH = getHeight();
        double oldS = getAngleStart();
        double oldE = getAngleExtent();
        double oldT = getArcType();
        initialise(value, false, true);

        // Check if the field has changed, and set the owner field as dirty if necessary
        Arc2dDouble rect = (Arc2dDouble) value;
        double newX = rect.getX();
        double newY = rect.getY();
        double newW = rect.getWidth();
        double newH = rect.getHeight();
        double newS = rect.getAngleStart();
        double newE = rect.getAngleExtent();
        double newT = rect.getArcType();
        if (oldX != newX || oldY != newY || oldW != newW || oldH != newH || oldS != newS || oldE != newE || oldT != newT)
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
        ((Arc2dDouble) obj).unsetOwner();
        return obj;
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D.Double#setArc(double, double, double, double, double, double, int)
     */
    @Override
    public void setArc(double x, double y, double w, double h, double angSt, double angExt, int closure)
    {
        super.setArc(x, y, w, h, angSt, angExt, closure);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D.Double#setAngleStart(double)
     */
    @Override
    public void setAngleStart(double angSt)
    {
        super.setAngleStart(angSt);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D.Double#setAngleExtent(double)
     */
    @Override
    public void setAngleExtent(double angExt)
    {
        super.setAngleExtent(angExt);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D#setArc(java.awt.geom.Point2D, java.awt.geom.Dimension2D, double, double, int)
     */
    @Override
    public void setArc(Point2D loc, Dimension2D size, double angSt, double angExt, int closure)
    {
        super.setArc(loc, size, angSt, angExt, closure);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D#setArc(java.awt.geom.Rectangle2D, double, double, int)
     */
    @Override
    public void setArc(Rectangle2D rect, double angSt, double angExt, int closure)
    {
        super.setArc(rect, angSt, angExt, closure);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D#setArc(java.awt.geom.Arc2D)
     */
    @Override
    public void setArc(Arc2D a)
    {
        super.setArc(a);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D#setArcByCenter(double, double, double, double, double, int)
     */
    @Override
    public void setArcByCenter(double x, double y, double radius, double angSt, double angExt, int closure)
    {
        super.setArcByCenter(x, y, radius, angSt, angExt, closure);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D#setArcByTangent(java.awt.geom.Point2D, java.awt.geom.Point2D,
     * java.awt.geom.Point2D, double)
     */
    @Override
    public void setArcByTangent(Point2D p1, Point2D p2, Point2D p3, double radius)
    {
        super.setArcByTangent(p1, p2, p3, radius);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D#setAngleStart(java.awt.geom.Point2D)
     */
    @Override
    public void setAngleStart(Point2D p)
    {
        super.setAngleStart(p);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D#setAngles(double, double, double, double)
     */
    @Override
    public void setAngles(double x1, double y1, double x2, double y2)
    {
        super.setAngles(x1, y1, x2, y2);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D#setAngles(java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public void setAngles(Point2D p1, Point2D p2)
    {
        super.setAngles(p1, p2);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D#setArcType(int)
     */
    @Override
    public void setArcType(int type)
    {
        super.setArcType(type);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.geom.Arc2D#setFrame(double, double, double, double)
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