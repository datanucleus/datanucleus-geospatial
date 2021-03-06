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

import java.awt.geom.QuadCurve2D;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.types.converters.TypeConverter;
import org.datanucleus.util.Localiser;

/**
 * Class to handle the conversion between java.awt.geom.QuadCurve2D.Double and a String form. The String form
 * is
 * 
 * <pre>
 * (x1,y1),(x2,y2),(xc1,yc1)
 * </pre>
 */
public class QuadCurve2dDoubleStringConverter implements TypeConverter<QuadCurve2D.Double, String>
{
    private static final long serialVersionUID = 7172082017676477969L;

    public QuadCurve2D.Double toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        QuadCurve2D.Double cc = new QuadCurve2D.Double();

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
            throw new NucleusDataStoreException(Localiser.msg("016002", str, QuadCurve2D.Double.class.getName()), nfe);
        }
        double y1 = 0;
        try
        {
            y1 = Double.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, QuadCurve2D.Double.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        token = token.substring(2); // x2,y2
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",") + 1);
        double x2 = 0;
        try
        {
            x2 = Double.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, QuadCurve2D.Double.class.getName()), nfe);
        }
        double y2 = 0;
        try
        {
            y2 = Double.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, QuadCurve2D.Double.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        token = token.substring(2); // xc,yc
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",") + 1);
        double xc = 0;
        try
        {
            xc = Double.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, QuadCurve2D.Double.class.getName()), nfe);
        }
        double yc = 0;
        try
        {
            yc = Double.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, QuadCurve2D.Double.class.getName()), nfe);
        }

        cc.setCurve(x1, y1, x2, y2, xc, yc);

        return cc;
    }

    public String toDatastoreType(QuadCurve2D.Double cc)
    {
        if (cc == null)
        {
            return null;
        }

        // Create string form like "(x1,y1),(x2,y2),(xc,yc)"
        StringBuilder str = new StringBuilder();
        str.append("(").append(cc.x1).append(",").append(cc.y1).append("),");
        str.append("(").append(cc.x2).append(",").append(cc.y2).append("),");
        str.append("(").append(cc.ctrlx).append(",").append(cc.ctrly).append("),");
        return str.toString();
    }
}