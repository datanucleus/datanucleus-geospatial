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
    public SQLExpression getExpression(SQLStatement stmt, SQLExpression ignore, List args)
    {
        if (args == null || args.size() != 3)
        {
            throw new NucleusUserException("Cannot invoke bBoxTest without 3 arguments");
        }

        SQLExpression argGeometry1 = (SQLExpression) args.get(0); // Geometry 1
        SQLExpression argGeometry2 = (SQLExpression) args.get(1); // Geometry 2
        SQLExpression argTolerance = (SQLExpression) args.get(2); // Tolerance

        StringLiteral mask = new StringLiteral(stmt, null, RELATE_MASK_FOR_BBOXTEST, null);

        ArrayList funcArgs = new ArrayList();
        funcArgs.add(argGeometry1);
        funcArgs.add(mask);
        funcArgs.add(argGeometry2);
        funcArgs.add(argTolerance);

        JavaTypeMapping m = stmt.getSQLExpressionFactory().getMappingForType(String.class, true);
        StringExpression relateExp = new StringExpression(stmt, m, "SDO_GEOM.RELATE", funcArgs);

        return relateExp.eq(mask);
    }
}