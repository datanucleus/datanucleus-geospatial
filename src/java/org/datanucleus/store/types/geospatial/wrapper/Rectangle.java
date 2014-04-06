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

import java.awt.Dimension;
import java.awt.Point;

import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.state.FetchPlanState;
import org.datanucleus.state.ObjectProvider;
import org.datanucleus.store.types.SCO;

/**
 * A mutable second-class java.awt.Rectangle object.
 */
public class Rectangle extends java.awt.Rectangle implements SCO
{
    protected transient ObjectProvider ownerOP;

    protected transient String fieldName;

    /**
     * Creates a <tt>Point</tt> object. Assigns owning object and field name.
     * @param ownerSM the owning object
     * @param mmd Metadata for the member
     */
    public Rectangle(ObjectProvider ownerSM, AbstractMemberMetaData mmd)
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
        super.setRect((java.awt.Rectangle) value);
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
        return new java.awt.Rectangle(this);
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
        return new java.awt.Rectangle(this);
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.types.sco.SCO#attachCopy(java.lang.Object)
     */
    public void attachCopy(Object value)
    {
        double oldX = getX();
        double oldY = getY();
        double oldWidth = getWidth();
        double oldHeight = getHeight();
        initialise(value, false, true);

        // Check if the field has changed, and set the owner field as dirty if necessary
        Rectangle rect = (Rectangle) value;
        double newX = rect.getX();
        double newY = rect.getY();
        double newWidth = rect.getWidth();
        double newHeight = rect.getHeight();
        if (oldX != newX || oldY != newY || oldWidth != newWidth || oldHeight != newHeight)
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
        ((Rectangle) obj).unsetOwner();
        return obj;
    }

    /*
     * (non-Javadoc)
     * @see java.awt.Rectangle#setRect(double, double, double, double)
     */
    @Override
    public void setRect(double x, double y, double width, double height)
    {
        super.setRect(x, y, width, height);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.Rectangle#setLocation(java.awt.Point)
     */
    @Override
    public void setLocation(Point p)
    {
        super.setLocation(p);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.Rectangle#setLocation(int, int)
     */
    @Override
    public void setLocation(int x, int y)
    {
        super.setLocation(x, y);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.Rectangle#setSize(java.awt.Dimension)
     */
    @Override
    public void setSize(Dimension d)
    {
        super.setSize(d);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.Rectangle#setSize(int, int)
     */
    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        makeDirty();
    }
}