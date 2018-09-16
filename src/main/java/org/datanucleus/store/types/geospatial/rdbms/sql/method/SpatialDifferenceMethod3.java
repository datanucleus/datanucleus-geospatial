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
   2013 barisergun75@gmail.com - NUCSPATIAL-28 Adding postgis support
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
 * Implementation of "Spatial.difference(expr, expr2)" or "{expr}.difference(expr2)" method for PostGIS.
 */
public class SpatialDifferenceMethod3 implements SQLMethod
{
    public SQLExpression getExpression(SQLStatement stmt, SQLExpression expr, List args)
    {
        if (args == null)
        {
            throw new NucleusUserException("Cannot invoke Spatial.difference without arguments");
        }
        if (expr == null && args.size() != 2)
        {
            throw new NucleusUserException("Cannot invoke Spatial.difference without 2 arguments");
        }
        else if (expr != null && args.size() != 1)
        {
            throw new NucleusUserException("Cannot invoke geom.difference() without 1 argument");
        }

        SQLExpression argExpr1 = expr;
        SQLExpression argExpr2 = (SQLExpression) args.get(0); // Geometry 2
        if (expr == null)
        {
            // "Spatial." method
            argExpr1 = (SQLExpression) args.get(0); // Geometry 1
            argExpr2 = (SQLExpression) args.get(1); // Geometry 2
        }

        ArrayList<SQLExpression> funcArgs = new ArrayList<SQLExpression>();
        funcArgs.add(argExpr1);
        funcArgs.add(argExpr2);

        ClassLoaderResolver clr = stmt.getQueryGenerator().getClassLoaderResolver();
        JavaTypeMapping geomMapping = SpatialMethodHelper.getGeometryMapping(clr, argExpr1);
        return new GeometryExpression(stmt, geomMapping, "st_difference", funcArgs, null);
    }
}
