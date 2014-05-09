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

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.method.AbstractSQLMethod;

/**
 * Implementation of "Spatial.relate(expr, expr2, expr3)" or "expr.relate(expr2,expr3)" method for Postgresql.
 */
public class SpatialRelateMethod3 extends AbstractSQLMethod
{
    public SQLExpression getExpression(SQLExpression expr, List args)
    {
        if (args == null)
        {
            throw new NucleusUserException("Cannot invoke Spatial.relate without arguments");
        }
        if (expr == null && args.size() != 3)
        {
            throw new NucleusUserException("Cannot invoke Spatial.relate without 3 arguments");
        }
        else if (expr != null && args.size() != 2)
        {
            throw new NucleusUserException("Cannot invoke geom.relate() without 2 argument");
        }

        SQLExpression argExpr1 = null;
        SQLExpression argExpr2 = null;
        SQLExpression argExpr3 = null;
        if (expr == null)
        {
            // "Spatial." method
            argExpr1 = (SQLExpression) args.get(0); // Geometry 1
            argExpr2 = (SQLExpression) args.get(1); // Geometry 2
            argExpr3 = (SQLExpression) args.get(2); // Pattern
        }
        else
        {
            argExpr1 = expr; // Geometry 1
            argExpr2 = (SQLExpression) args.get(0); // Geometry 2
            argExpr3 = (SQLExpression) args.get(1); // Pattern
        }

        ArrayList<SQLExpression> funcArgs = new ArrayList<SQLExpression>();
        funcArgs.add(argExpr1);
        funcArgs.add(argExpr2);
        funcArgs.add(argExpr3);

        return SpatialMethodHelper.getBooleanExpression(stmt, "st_relate", funcArgs, exprFactory);
    }
}
