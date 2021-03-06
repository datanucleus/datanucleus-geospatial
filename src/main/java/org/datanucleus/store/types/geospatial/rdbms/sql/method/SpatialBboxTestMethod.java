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

import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.query.expression.Expression;
import org.datanucleus.store.query.expression.Expression.DyadicOperator;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.BooleanExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.method.SQLMethod;

/**
 * Implementation of "Spatial.bboxTest" method for PostGIS.
 */
public class SpatialBboxTestMethod implements SQLMethod
{
    private static final DyadicOperator BBOX_OVERLAPS = new Expression.DyadicOperator("&&", 3, false);

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
            throw new NucleusUserException("Cannot invoke Spatial.bboxTest without arguments");
        }
        if (expr == null && args.size() != 2)
        {
            throw new NucleusUserException("Cannot invoke Spatial.bboxTest without 2 arguments");
        }
        else if (expr != null && args.size() != 1)
        {
            throw new NucleusUserException("Cannot invoke geom.bboxTest() without 1 argument");
        }

        SQLExpression argExpr1 = null;
        SQLExpression argExpr2 = null;

        if (expr == null)
        {
            argExpr1 = (SQLExpression) args.get(0);
            argExpr2 = (SQLExpression) args.get(1);
        }
        else
        {
            argExpr1 = expr;
            argExpr2 = (SQLExpression) args.get(0);
        }

        BooleanExpression boolExpr = new BooleanExpression(argExpr1, BBOX_OVERLAPS, argExpr2);
        boolExpr.setJavaTypeMapping(stmt.getSQLExpressionFactory().getMappingForType(boolean.class, false));
        return boolExpr;
    }
}