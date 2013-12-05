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
package org.datanucleus.store.types.converters;

import java.awt.geom.Point2D;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;

/**
 * Class to handle the conversion between java.awt.Point and a String form.
 * The String form is <pre>(x,y)</pre>
 */
public class Point2dFloatStringConverter implements TypeConverter<Point2D.Float, String>
{
    public Point2D.Float toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        Point2D.Float p = new Point2D.Float();
        StringTokenizer tokeniser = new StringTokenizer(str.substring(1, str.length()-1), ",");

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
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Point2D.Float.class.getName()), nfe);
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
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Point2D.Float.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        p.setLocation(x, y);
        return p;
    }

    public String toDatastoreType(Point2D.Float p)
    {
        return p != null ? "(" + p.getX() + "," + p.getY() + ")" : null;
    }
}