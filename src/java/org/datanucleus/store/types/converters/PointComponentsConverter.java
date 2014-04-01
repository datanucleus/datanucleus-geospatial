/**********************************************************************
Copyright (c) 2014 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.store.types.converters;

import java.awt.Point;

/**
 * Class to handle the conversion between java.awt.Point and (X,Y).
 */
public class PointComponentsConverter implements TypeConverter<Point, double[]>, MultiColumnConverter
{
    public Point toMemberType(double[] values)
    {
        if (values == null)
        {
            return null;
        }

        Point p = new Point();
        p.setLocation(values[0], values[1]);
        return p;
    }

    public double[] toDatastoreType(Point p)
    {
        if (p == null)
        {
            return null;
        }
        return new double[]{p.getX(), p.getY()};
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.types.converters.MultiColumnConverter#getDatastoreColumnTypes()
     */
    @Override
    public Class[] getDatastoreColumnTypes()
    {
        return new Class[]{double.class, double.class};
    }
}