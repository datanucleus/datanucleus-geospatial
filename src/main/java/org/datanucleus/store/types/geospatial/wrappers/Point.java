/**********************************************************************
Copyright (c) 2005 Andy Jefferson and others. All rights reserved.
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
import java.io.ObjectStreamException;

import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.state.FetchPlanState;
import org.datanucleus.state.ObjectProvider;
import org.datanucleus.store.types.SCO;

/**
 * A mutable second-class java.awt.Point object.
 */
public class Point extends java.awt.Point implements SCO<java.awt.Point>
{
    protected transient ObjectProvider ownerOP;

    protected transient String fieldName;

    boolean initialising = false;

    /**
     * Creates a <tt>Point</tt> object. Assigns owning object and field name.
     * @param op ObjectProvider for the owning object
     * @param mmd Metadata for the member
     */
    public Point(ObjectProvider op, AbstractMemberMetaData mmd)
    {
        super();

        this.ownerOP = op;
        this.fieldName = mmd.getName();
    }

    /**
     * Method to initialise the SCO for use.
     */
    public void initialise()
    {
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.types.SCO#initialise(java.lang.Object, java.lang.Object)
     */
    public void initialise(java.awt.Point newValue, Object oldValue)
    {
        initialising = true;
        super.setLocation(newValue);
        initialising = false;
    }

    /**
     * Method to initialise the SCO from an existing value.
     * @param o The Object
     */
    public void initialise(java.awt.Point o)
    {
        initialising = true;
        super.setLocation(o);
        initialising = false;
    }

    /**
     * Accessor for the unwrapped value that we are wrapping.
     * @return The unwrapped value
     */
    public java.awt.Point getValue()
    {
        return new java.awt.Point(this);
    }

    /**
     * Utility to unset the owner.
     **/
    public void unsetOwner()
    {
        ownerOP = null;
    }

    /**
     * Accessor for the owner.
     * @return The owner
     */
    public Object getOwner()
    {
        return (ownerOP != null ? ownerOP.getObject() : null);
    }

    /**
     * Accessor for the field name
     * @return The field name
     */
    public String getFieldName()
    {
        return this.fieldName;
    }

    /**
     * Utility to mark the object as dirty
     */
    public void makeDirty()
    {
        if (ownerOP != null && !initialising)
        {
            ownerOP.getExecutionContext().getApiAdapter().makeDirty(ownerOP.getObject(), fieldName);
        }
    }

    /**
     * Method to detach a copy of this object.
     * @param state State for detachment process
     * @return The detached object
     */
    public java.awt.Point detachCopy(FetchPlanState state)
    {
        return new java.awt.Point(this);
    }

    /**
     * Method to attach the passed value.
     * @param value The new value
     */
    public void attachCopy(java.awt.Point value)
    {
        double oldX = getX();
        double oldY = getY();
        initialise(value, null);

        // Check if the field has changed, and set the owner field as dirty if necessary
        double newX = value.getX();
        double newY = value.getY();
        if (oldX != newX || oldY != newY)
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

        ((Point) obj).unsetOwner();

        return obj;
    }

    /**
     * Mutator for the location.
     * @param x The location x
     * @param y The location y
     **/
    public void setLocation(double x, double y)
    {
        super.setLocation(x, y);
        makeDirty();
    }

    /**
     * Mutator for the location.
     * @param x The location x
     * @param y The location y
     **/
    public void setLocation(int x, int y)
    {
        super.setLocation(x, y);
        makeDirty();
    }

    /**
     * Mutator for the location.
     * @param point The location
     **/
    public void setLocation(java.awt.Point point)
    {
        super.setLocation(point);
        makeDirty();
    }

    /**
     * Mutator for the location.
     * @param point The location
     **/
    public void setLocation(Point2D point)
    {
        super.setLocation(point);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.Point#move(int, int)
     */
    public void move(int x, int y)
    {
        super.move(x, y);
        makeDirty();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.Point#translate(int, int)
     */
    public void translate(int dx, int dy)
    {
        super.translate(dx, dy);
        makeDirty();
    }

    /**
     * The writeReplace method is called when ObjectOutputStream is preparing to write the object to the
     * stream. The ObjectOutputStream checks whether the class defines the writeReplace method. If the method
     * is defined, the writeReplace method is called to allow the object to designate its replacement in the
     * stream. The object returned should be either of the same type as the object passed in or an object that
     * when read and resolved will result in an object of a type that is compatible with all references to the
     * object.
     * @return the replaced object
     * @throws ObjectStreamException when problem occurs on serialization
     */
    protected Object writeReplace() throws ObjectStreamException
    {
        return new java.awt.Point(this.getLocation());
    }
}
