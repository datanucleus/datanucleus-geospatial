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

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.types.geospatial.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.NumericExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.method.SQLMethod;

/**
 * Implementation of "Spatial.distance(expr, expr2)" or "{expr}.distance(expr2)" method for Oracle.
 */
public class SpatialDistanceMethod2 implements SQLMethod
{
    /*
     * (non-Javadoc)
     * @see
     * org.datanucleus.store.rdbms.sql.method.SQLMethod#getExpression(org.datanucleus.store.rdbms.sql.expression
     * .SQLExpression, java.util.List)
     */
    public SQLExpression getExpression(SQLStatement stmt, SQLExpression expr, List args)
    {
        if (args == null)
        {
            throw new NucleusUserException("Cannot invoke Spatial.distance without arguments");
        }
        if (expr == null && args.size() != 2)
        {
            throw new NucleusUserException("Cannot invoke Spatial.distance without 2 arguments");
        }
        else if (expr != null && args.size() != 1)
        {
            throw new NucleusUserException("Cannot invoke geom.distance without 1 argument");
        }

        SQLExpression argExpr1 = null;
        SQLExpression argExpr2 = null;
        if (expr == null)
        {
            // "Spatial." method
            argExpr1 = (SQLExpression) args.get(0);
            argExpr2 = (SQLExpression) args.get(1);
        }
        else
        {
            argExpr1 = expr;
            argExpr2 = (SQLExpression) args.get(0);
        }

        ArrayList geomFunc1Args = new ArrayList();
        geomFunc1Args.add(argExpr1);
        GeometryExpression geom1Expr = new GeometryExpression(stmt, null, "geometry.from_sdo_geom", geomFunc1Args, null);

        ArrayList geomFunc2Args = new ArrayList();
        geomFunc2Args.add(argExpr2);
        GeometryExpression geom2Expr = new GeometryExpression(stmt, null, "geometry.from_sdo_geom", geomFunc2Args, null);

        ArrayList funcArgs = new ArrayList();
        funcArgs.add(geom1Expr);
        funcArgs.add(geom2Expr);
        JavaTypeMapping m = stmt.getSQLExpressionFactory().getMappingForType(double.class);
        return new NumericExpression(stmt, m, "distance", funcArgs);
    }
}