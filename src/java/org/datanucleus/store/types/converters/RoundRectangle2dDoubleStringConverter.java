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

import java.awt.geom.RoundRectangle2D;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;

/**
 * Class to handle the conversion between java.awt.geom.RoundRectangle2D.Double and a String form. The String
 * form is
 * 
 * <pre>
 * (x,y,width,height,arcwidth,archeight)
 * </pre>
 */
public class RoundRectangle2dDoubleStringConverter implements TypeConverter<RoundRectangle2D.Double, String>
{
    public RoundRectangle2D.Double toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        RoundRectangle2D.Double r = new RoundRectangle2D.Double();
        StringTokenizer tokeniser = new StringTokenizer(str.substring(1, str.length() - 1), ",");

        double x = 0;
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            try
            {
                x = Double.valueOf(token);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, RoundRectangle2D.Double.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        double y = 0;
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            try
            {
                y = Double.valueOf(token);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, RoundRectangle2D.Double.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        double width = 0;
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            try
            {
                width = Double.valueOf(token);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, RoundRectangle2D.Double.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        double height = 0;
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            try
            {
                height = Double.valueOf(token);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, RoundRectangle2D.Double.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        double arcwidth = 0;
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            try
            {
                arcwidth = Double.valueOf(token);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, RoundRectangle2D.Double.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        double archeight = 0;
        if (tokeniser.hasMoreTokens())
        {
            String token = tokeniser.nextToken().trim();
            try
            {
                archeight = Double.valueOf(token);
            }
            catch (NumberFormatException nfe)
            {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, RoundRectangle2D.Double.class.getName()), nfe);
            }
        }
        else
        {
            return null;
        }

        r.setRoundRect(x, y, width, height, arcwidth, archeight);
        return r;
    }

    public String toDatastoreType(RoundRectangle2D.Double rect)
    {
        return rect != null ? "(" + rect.getX() + "," + rect.getY() + "," + rect.getWidth() + "," + rect.getHeight() + "," + rect.arcwidth + "," + rect.archeight + ")" : null;
    }
}