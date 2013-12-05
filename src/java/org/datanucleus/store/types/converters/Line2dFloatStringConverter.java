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

import java.awt.geom.Line2D;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;

/**
 * Class to handle the conversion between java.awt.geom.Line2D.Float and a String form.
 * The String form is <pre>(x1,y1),(x2,y2)</pre>
 */
public class Line2dFloatStringConverter implements TypeConverter<Line2D.Float, String>
{
    public Line2D.Float toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        Line2D.Float l = new Line2D.Float();

        StringTokenizer tokeniser = new StringTokenizer(str, ")");

        String token = tokeniser.nextToken();
        token = token.substring(1); // x1,y1
        String xStr = token.substring(0, token.indexOf(","));
        String yStr = token.substring(token.indexOf(",")+1);
        float x1 = 0;
        try
        {
            x1 = Float.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Line2D.Float.class.getName()), nfe);
        }
        float y1 = 0;
        try
        {
            y1 = Float.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Line2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        token = token.substring(1); // x2,y2
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",")+1);
        float x2 = 0;
        try
        {
            x2 = Float.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Line2D.Float.class.getName()), nfe);
        }
        float y2 = 0;
        try
        {
            y2 = Float.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Line2D.Float.class.getName()), nfe);
        }

        l.setLine(x1, y1, x2, y2);
        return l;
    }

    public String toDatastoreType(Line2D.Float line)
    {
        if (line == null)
        {
            return null;
        }

        // Create string form like "(x1,y1),(x2,y2)"
        StringBuffer str = new StringBuffer();
        str.append("(").append(line.x1).append(",").append(line.y1).append("),");
        str.append("(").append(line.x2).append(",").append(line.y2).append(")");
        return str.toString();
    }
}