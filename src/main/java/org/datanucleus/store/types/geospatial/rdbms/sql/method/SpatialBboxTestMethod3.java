/**********************************************************************
Copyright (c) 2010 Andy Jefferson and others. All rights reserved.
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
   barisergun75@gmail.com
 **********************************************************************/
package org.datanucleus.store.types.geospatial.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.StringExpression;
import org.datanucleus.store.rdbms.sql.expression.StringLiteral;
import org.datanucleus.store.rdbms.sql.method.SQLMethod;

/**
 * Implementation of "Spatial.bboxTest" method for Oracle.
 */
public class SpatialBboxTestMethod3 implements SQLMethod
{
    private final static String RELATE_MASK_FOR_BBOXTEST = "MASK=OVERLAPBDYINTERSECT QUERYTYPE=WINDOW";

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.sql.method.SQLMethod#getExpression(org.
     * datanucleus.store.rdbms.sql.expression.SQLExpression, java.util.List)
     */
    public SQLExpression getExpression(SQLStatement stmt, SQLExpression expr, List args)
    {
        if (args == null)
        {
            throw new NucleusUserException("Cannot invoke Spatial.bboxTest without arguments");
        }
        if (expr == null && args.size() != 3)
        {
            throw new NucleusUserException("Cannot invoke Spatial.bboxTest without 3 arguments");
        }
        else if (expr != null && args.size() != 2)
        {
            throw new NucleusUserException("Cannot invoke geom.bboxTest() without 2 arguments");
        }

        SQLExpression argExpr1 = null;
        SQLExpression argExpr2 = null;
        SQLExpression argTol = null;

        if (expr == null)
        {
            argExpr1 = (SQLExpression) args.get(0);
            argExpr2 = (SQLExpression) args.get(1);
            argTol = (SQLExpression) args.get(2);
        }
        else
        {
            argExpr1 = expr;
            argExpr2 = (SQLExpression) args.get(0);
            argTol = (SQLExpression) args.get(1);
        }

        StringLiteral mask = new StringLiteral(stmt, null, RELATE_MASK_FOR_BBOXTEST, null);

        ArrayList funcArgs = new ArrayList();
        funcArgs.add(argExpr1);
        funcArgs.add(mask);
        funcArgs.add(argExpr2);
        funcArgs.add(argTol);

        JavaTypeMapping m = stmt.getSQLExpressionFactory().getMappingForType(String.class, true);
        StringExpression relateExp = new StringExpression(stmt, m, "SDO_GEOM.RELATE", funcArgs);

        return relateExp.eq(mask);
    }
}