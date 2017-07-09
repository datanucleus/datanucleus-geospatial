/**********************************************************************
Copyright (c) 2009 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.store.types.geospatial.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.types.geospatial.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.method.SQLMethod;

/**
 * Implementation of "Spatial.exteriorRing(expr)" or "{expr}.getExteriorRing()" method for Oracle.
 */
public class SpatialExteriorRingMethod2 implements SQLMethod
{
    /*
     * (non-Javadoc)
     * @see
     * org.datanucleus.store.rdbms.sql.method.SQLMethod#getExpression(org.datanucleus.store.rdbms.sql.expression
     * .SQLExpression, java.util.List)
     */
    public SQLExpression getExpression(SQLStatement stmt, SQLExpression expr, List args)
    {
        if (expr == null && (args == null || args.size() != 1))
        {
            throw new NucleusUserException("Cannot invoke Spatial.exteriorRing without 1 argument");
        }
        else if (expr != null && args != null && args.size() != 0)
        {
            throw new NucleusUserException("Cannot invoke geom.getExteriorRing() with an argument");
        }

        SQLExpression argExpr = expr;
        if (expr == null)
        {
            argExpr = (SQLExpression) args.get(0);
        }

        ArrayList geomFuncArgs = new ArrayList();
        geomFuncArgs.add(argExpr);
        GeometryExpression geomExpr = new GeometryExpression(stmt, null, "geometry.from_sdo_geom", geomFuncArgs, null);

        ArrayList treatFuncArgs = new ArrayList();
        treatFuncArgs.add(geomExpr);
        ArrayList treatFuncTypeArgs = new ArrayList();
        treatFuncTypeArgs.add("Polygon");
        GeometryExpression treatExpr = new GeometryExpression(stmt, null, "treat", treatFuncArgs, treatFuncTypeArgs);

        ArrayList startFuncArgs = new ArrayList();
        startFuncArgs.add(treatExpr);
        GeometryExpression startExpr = new GeometryExpression(stmt, null, "exteriorRing", startFuncArgs, null);

        ClassLoaderResolver clr = stmt.getQueryGenerator().getClassLoaderResolver();
        ArrayList funcArgs = new ArrayList();
        funcArgs.add(startExpr);
        JavaTypeMapping geomMapping = SpatialMethodHelper.getGeometryMapping(clr, argExpr);
        return new GeometryExpression(stmt, geomMapping, "geometry.get_sdo_geom", funcArgs, null);
    }
}