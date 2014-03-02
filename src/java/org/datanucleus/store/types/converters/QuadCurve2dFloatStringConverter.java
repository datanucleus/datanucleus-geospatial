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

import java.awt.geom.QuadCurve2D;
import java.util.StringTokenizer;

import org.datanucleus.exceptions.NucleusDataStoreException;

/**
 * Class to handle the conversion between java.awt.geom.QuadCurve2D.Float and a String form. The String form
 * is
 * 
 * <pre>
 * (x1,y1),(x2,y2),(xc,yc)
 * </pre>
 */
public class QuadCurve2dFloatStringConverter implements TypeConverter<QuadCurve2D.Float, String>
{
    public QuadCurve2D.Float toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        QuadCurve2D.Float cc = new QuadCurve2D.Float();

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
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, QuadCurve2D.Float.class.getName()), nfe);
        }
        float y1 = 0;
        try
        {
            y1 = Float.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, QuadCurve2D.Float.class.getName()), nfe);
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
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, QuadCurve2D.Float.class.getName()), nfe);
        }
        float y2 = 0;
        try
        {
            y2 = Float.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, QuadCurve2D.Float.class.getName()), nfe);
        }

        token = tokeniser.nextToken();
        token = token.substring(2); // xc,yc
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",") + 1);
        float xc = 0;
        try
        {
            xc = Float.valueOf(xStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, QuadCurve2D.Float.class.getName()), nfe);
        }
        float yc = 0;
        try
        {
            yc = Float.valueOf(yStr);
        }
        catch (NumberFormatException nfe)
        {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, QuadCurve2D.Float.class.getName()), nfe);
        }

        cc.setCurve(x1, y1, x2, y2, xc, yc);

        return cc;
    }

    public String toDatastoreType(QuadCurve2D.Float cc)
    {
        if (cc == null)
        {
            return null;
        }

        // Create string form like "(x1,y1),(x2,y2),(xc,yc)"
        StringBuffer str = new StringBuffer();
        str.append("(").append(cc.x1).append(",").append(cc.y1).append("),");
        str.append("(").append(cc.x2).append(",").append(cc.y2).append("),");
        str.append("(").append(cc.ctrlx).append(",").append(cc.ctrly).append("),");
        return str.toString();
    }
}