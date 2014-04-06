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

import java.awt.geom.Arc2D;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.types.converters.TypeConverter;

/**
 * Class to handle the conversion between java.awt.geom.Arc2D.Float and a String form. The String form is
 * 
 * <pre>
 * x,y,width,height,start,extent
 * </pre>
 */
public class Arc2dDoubleStringConverter implements TypeConverter<Arc2D.Double, String>
{
    public Arc2D.Double toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        Arc2D.Double l = new Arc2D.Double();

        StringTokenizer tokeniser = new StringTokenizer(str, ",");

        String token = tokeniser.nextToken();
        double x = 0;
        try
        {
            x = Double.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        double y = 0;
        try
        {
            y = Double.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        double width = 0;
        try
        {
            width = Double.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        double height = 0;
        try
        {
            height = Double.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        double start = 0;
        try
        {
            start = Double.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        double extent = 0;
        try
        {
            extent = Double.valueOf(token);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Arc2D.Float.class.getName()), nfe);
        }

        l.setArc(x, y, width, height, start, extent, 0);
        return l;
    }

    public String toDatastoreType(Arc2D.Double arc)
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