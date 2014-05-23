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

import java.awt.geom.CubicCurve2D;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.types.converters.TypeConverter;
import org.datanucleus.util.Localiser;

/**
 * Class to handle the conversion between java.awt.geom.CubicCurve2D.Float and a String form. The String form
 * is
 * 
 * <pre>
 * (x1,y1),(x2,y2),(xc1,yc1),(xc2,yc2)
 * </pre>
 */
public class CubicCurve2dFloatStringConverter implements TypeConverter<CubicCurve2D.Float, String>
{
    public CubicCurve2D.Float toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        CubicCurve2D.Float cc = new CubicCurve2D.Float();

        StringTokenizer tokeniser = new StringTokenizer(str, ")");

        String token = tokeniser.nextToken();
        token = token.substring(1); // x1,y1
        String xStr = token.substring(0, token.indexOf(","));
        String yStr = token.substring(token.indexOf(",") + 1);
        float x1 = 0;
        try
        {
            x1 = Float.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        float y1 = 0;
        try
        {
            y1 = Float.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        token = token.substring(2); // x2,y2
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",") + 1);
        float x2 = 0;
        try
        {
            x2 = Float.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        float y2 = 0;
        try
        {
            y2 = Float.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        token = token.substring(2); // xc1,yc1
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",") + 1);
        float xc1 = 0;
        try
        {
            xc1 = Float.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        float yc1 = 0;
        try
        {
            yc1 = Float.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        token = token.substring(2); // xc2,yc2
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",") + 1);
        float xc2 = 0;
        try
        {
            xc2 = Float.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        float yc2 = 0;
        try
        {
            yc2 = Float.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(Localiser.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }

        cc.setCurve(x1, y1, x2, y2, xc1, yc1, xc2, yc2);

        return cc;
    }

    public String toDatastoreType(CubicCurve2D.Float cc)
    {
        if (cc == null)
        {
            return null;
        }

        // Create string form like "(x1,y1),(x2,y2),(xc1,yc1),(xc2,yc2)"
        StringBuffer str = new StringBuffer();
        str.append("(").append(cc.x1).append(",").append(cc.y1).append("),");
        str.append("(").append(cc.x2).append(",").append(cc.y2).append("),");
        str.append("(").append(cc.ctrlx1).append(",").append(cc.ctrly1).append("),");
        str.append("(").append(cc.ctrlx2).append(",").append(cc.ctrly2).append(")");
        return str.toString();
    }
}