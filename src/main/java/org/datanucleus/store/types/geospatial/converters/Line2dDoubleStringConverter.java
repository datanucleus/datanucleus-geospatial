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

import java.awt.geom.Line2D;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.types.converters.TypeConverter;
import org.datanucleus.util.Localiser;

/**
 * Class to handle the conversion between java.awt.geom.Line2D.Double and a String form. The String form is
 * 
 * <pre>
 * (x1,y1),(x2,y2)
 * </pre>
 */
public class Line2dDoubleStringConverter implements TypeConverter<Line2D.Double, String>
{
    public Line2D.Double toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        Line2D.Double l = new Line2D.Double();

        StringTokenizer tokeniser = new StringTokenizer(str, ")");

        String token = tokeniser.nextToken();
        token = token.substring(1); // x1,y1
        String xStr = token.substring(0, token.indexOf(","));
        String yStr = token.substring(token.indexOf(",") + 1);
        double x1 = 0;
        try
        {
            x1 = Double.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, Line2D.Double.class.getName()), nfe);
        }
        double y1 = 0;
        try
        {
            y1 = Double.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, Line2D.Double.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        token = token.substring(1); // x2,y2
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",") + 1);
        double x2 = 0;
        try
        {
            x2 = Double.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, Line2D.Double.class.getName()), nfe);
        }
        double y2 = 0;
        try
        {
            y2 = Double.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, Line2D.Double.class.getName()), nfe);
        }

        l.setLine(x1, y1, x2, y2);
        return l;
    }

    public String toDatastoreType(Line2D.Double line)
    {
        if (line == null)
        {
            return null;
        }

        // Create string form like "(x1,y1),(x2,y2)"
        StringBuilder str = new StringBuilder();
        str.append("(").append(line.x1).append(",").append(line.y1).append("),");
        str.append("(").append(line.x2).append(",").append(line.y2).append(")");
        return str.toString();
    }
}