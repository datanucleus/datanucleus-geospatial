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
package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.expression.NumericExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Implementation of "Spatial.length(expr)" or "{expr}.length()" method for PostGis.
 */
public class SpatialLengthMethod4 extends AbstractSQLMethod
{
    public SQLExpression getExpression(SQLExpression expr, List args)
    {
        if (expr == null && (args == null || args.size() != 1))
        {
            throw new NucleusUserException("Cannot invoke Spatial.length without 1 argument");
        }
        else if (expr != null && args != null && args.size() != 0)
        {
            throw new NucleusUserException("Cannot invoke geom.length() with an argument");
        }

        SQLExpression argExpr = expr;
        if (expr == null)
        {
            argExpr = (SQLExpression)args.get(0);
        }

        ArrayList<SQLExpression> funcArgs = new ArrayList<SQLExpression>();
        funcArgs.add(argExpr);

        JavaTypeMapping m = getMappingForClass(double.class);
        return new NumericExpression(stmt, m, "st_length", funcArgs);
    }
}
