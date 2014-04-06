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
import org.datanucleus.store.types.geospatial.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.method.AbstractSQLMethod;

/**
 * Implementation of Spatial "mPolyFromWKB" method.
 */
public class SpatialMPolyFromWKBMethod extends AbstractSQLMethod
{
    /*
     * (non-Javadoc)
     * @see
     * org.datanucleus.store.rdbms.sql.method.SQLMethod#getExpression(org.datanucleus.store.rdbms.sql.expression
     * .SQLExpression, java.util.List)
     */
    public SQLExpression getExpression(SQLExpression expr, List args)
    {
        if (args == null || args.size() != 2)
        {
            throw new NucleusUserException("Cannot invoke Spatial.mPolyFromWKB without 2 arguments");
        }

        SQLExpression wktExpr = (SQLExpression) args.get(0);
        SQLExpression sridExpr = (SQLExpression) args.get(1);

        ArrayList funcArgs = new ArrayList();
        funcArgs.add(wktExpr);
        funcArgs.add(sridExpr);

        return new GeometryExpression(stmt, null, "MPolyFromWKB", funcArgs, null);
    }
}