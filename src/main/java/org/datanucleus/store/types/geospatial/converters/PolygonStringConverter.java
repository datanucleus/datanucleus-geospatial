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

import java.awt.Polygon;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.types.converters.TypeConverter;
import org.datanucleus.util.Localiser;

/**
 * Class to handle the conversion between java.awt.Polygon and a String form. The String form is
 * 
 * <pre>
 * [(x1,y1),(x2,y2)[,(xn, yn)...])
 * </pre>
 */
public class PolygonStringConverter implements TypeConverter<Polygon, String>
{
    public Polygon toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        Polygon p = new Polygon();
        if (str.length() <= 2)
        {
            return p;
        }

        String tmpStr = str.substring(1, str.length() - 1); // Omit "[]"
        StringTokenizer tokeniser = new StringTokenizer(tmpStr.substring(1, tmpStr.length() - 1), "(");
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            token = token.substring(0, token.indexOf(")"));
            String xStr = token.substring(0, token.indexOf(","));
            String yStr = token.substring(token.indexOf(",") + 1);
            int x = 0;
            int y = 0;
            try
            {
                x = Integer.valueOf(xStr);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(Localiser.msg("016002", str, Polygon.class.getName()), nfe);
            }
            try
            {
                y = Integer.valueOf(yStr);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(Localiser.msg("016002", str, Polygon.class.getName()), nfe);
            }
            p.addPoint(x, y);
        }
        else
        {
            return null;
        }

        return p;
    }

    public String toDatastoreType(Polygon poly)
    {
        if (poly == null)
        {
            return null;
        }

        // Create string form like "[(x1,y1),(x2,y2),...]"
        StringBuilder str = new StringBuilder("[");
        for (int i = 0; i < poly.npoints; i++)
        {
            str.append("(").append(poly.xpoints[i]).append(",").append(poly.ypoints[i]).append(")");
            if (i < poly.npoints - 1)
            {
                str.append(",");
            }
        }
        str.append("]");
        return str.toString();
    }
}