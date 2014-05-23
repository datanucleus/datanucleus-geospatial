/**********************************************************************
Copyright (c) 2012 Andy Jefferson and others. All rights reserved.
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

import java.awt.geom.Rectangle2D;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.types.converters.TypeConverter;
import org.datanucleus.util.Localiser;

/**
 * Class to handle the conversion between java.awt.geom.Rectangle2D.Float and a String form. The String form
 * is
 * 
 * <pre>
 * (x,y,width,height)
 * </pre>
 */
public class Rectangle2dFloatStringConverter implements TypeConverter<Rectangle2D.Float, String>
{
    public Rectangle2D.Float toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        Rectangle2D.Float r = new Rectangle2D.Float();
        StringTokenizer tokeniser = new StringTokenizer(str.substring(1, str.length() - 1), ",");

        float x = 0;
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            try
            {
                x = Float.valueOf(token);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(Localiser.msg("016002", str, Rectangle2D.Float.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        float y = 0;
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            try
            {
                y = Float.valueOf(token);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(Localiser.msg("016002", str, Rectangle2D.Float.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        float width = 0;
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            try
            {
                width = Float.valueOf(token);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(Localiser.msg("016002", str, Rectangle2D.Float.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        float height = 0;
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            try
            {
                height = Float.valueOf(token);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(Localiser.msg("016002", str, Rectangle2D.Float.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        r.setRect(x, y, width, height);
        return r;
    }

    public String toDatastoreType(Rectangle2D.Float rect)
    {
        return rect != null ? "(" + rect.getX() + "," + rect.getY() + "," + rect.getWidth() + "," + rect.getHeight() + ")" : null;
    }
}