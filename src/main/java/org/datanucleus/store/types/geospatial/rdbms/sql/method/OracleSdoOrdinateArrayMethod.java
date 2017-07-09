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
   ...
 **********************************************************************/
package org.datanucleus.store.types.geospatial.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.types.geospatial.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpressionFactory;
import org.datanucleus.store.rdbms.sql.expression.StringLiteral;
import org.datanucleus.store.rdbms.sql.method.SQLMethod;

/**
 * Implementation of "Oracle.sdo_ordinate_array" method.
 */
public class OracleSdoOrdinateArrayMethod implements SQLMethod
{
    /*
     * (non-Javadoc)
     * @see
     * org.datanucleus.store.rdbms.sql.method.SQLMethod#getExpression(org.datanucleus.store.rdbms.sql.expression
     * .SQLExpression, java.util.List)
     */
    public SQLExpression getExpression(SQLStatement stmt, SQLExpression ignore, List args)
    {
        if (args == null || args.size() != 1)
        {
            throw new NucleusUserException("Cannot invoke Oracle.sdo_ordinate_array without 1 argument");
        }

        SQLExpression numbersExpr = (SQLExpression) args.get(0); // Numbers

        ArrayList funcArgs = new ArrayList();
        if (numbersExpr instanceof StringLiteral)
        {
            JavaTypeMapping m = stmt.getSQLExpressionFactory().getMappingForType(Double.class);

            // Numbers is a comma separated list of doubles
            String[] token = ((String) ((StringLiteral) numbersExpr).getValue()).split(",");
            try
            {
                SQLExpressionFactory exprFactory = stmt.getSQLExpressionFactory();
                for (int i = 0; i < token.length; i++)
                {
                    SQLExpression numberLit = exprFactory.newLiteral(stmt, m, Double.valueOf(token[i]));
                    funcArgs.add(numberLit);
                }
            }
            catch (NumberFormatException e)
            {
                throw new NucleusUserException("Impossible to convert input to Oracle.sdo_ordinate_array into list of doubles");
            }
        }

        return new GeometryExpression(stmt, null, "SDO_ORDINATE_ARRAY", funcArgs, null);
    }
}