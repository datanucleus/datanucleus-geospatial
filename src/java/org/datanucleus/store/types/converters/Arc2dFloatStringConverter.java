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

import java.awt.geom.Arc2D;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;

/**
 * Class to handle the conversion between java.awt.geom.Arc2D.Float and a String form.
 * The String form is <pre>x,y,width,height,start,extent</pre>
 */
public class Arc2dFloatStringConverter implements TypeConverter<Arc2D.Float, String>
{
    public Arc2D.Float toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        Arc2D.Float l = new Arc2D.Float();

        StringTokenizer tokeniser = new StringTokenizer(str, ",");

        String token = tokeniser.nextToken();
        float x = 0;
        try
        {
            x = Float.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        float y = 0;
        try
        {
            y = Float.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        float width = 0;
        try
        {
           width = Float.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        float height = 0;
        try
        {
            height = Float.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        float start = 0;
        try
        {
            start = Float.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        float extent = 0;
        try
        {
            extent = Float.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        l.setArc(x, y, width, height, start, extent, 0);
        return l;
    }

    public String toDatastoreType(Arc2D.Float arc)
    {
        if (arc == null)
        {
            return null;
        }

        // Create string form like "x,y,width,height,start,extent"
        StringBuffer str = new StringBuffer();
        str.append(arc.x).append(",").append(arc.y).append(",").append(arc.width).append(",").append(arc.height);
        str.append(",").append(arc.start).append(",").append(arc.extent);
        return str.toString();
    }
}