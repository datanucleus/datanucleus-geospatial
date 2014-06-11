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
package org.datanucleus.store.types.geospatial.converters;

import java.awt.Rectangle;
import org.datanucleus.store.types.converters.MultiColumnConverter;
import org.datanucleus.store.types.converters.TypeConverter;

/**
 * Class to handle the conversion between java.awt.Rectangle and (X,Y,Width,Height).
 */
public class RectangleComponentsConverter implements TypeConverter<Rectangle, int[]>, MultiColumnConverter
{
    private static final long serialVersionUID = -2340126869283906772L;

    public Rectangle toMemberType(int[] values)
    {
        if (values == null)
        {
            return null;
        }

        return new Rectangle(values[0], values[1], values[2], values[3]);
    }

    public int[] toDatastoreType(Rectangle r)
    {
        if (r == null)
        {
            return null;
        }
        return new int[]{r.x, r.y, r.width, r.height};
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.types.converters.MultiColumnConverter#getDatastoreColumnTypes()
     */
    @Override
    public Class[] getDatastoreColumnTypes()
    {
        return new Class[]{int.class, int.class, int.class, int.class};
    }
}