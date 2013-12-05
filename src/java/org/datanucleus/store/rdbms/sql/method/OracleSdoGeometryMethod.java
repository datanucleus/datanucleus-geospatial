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
package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Implementation of "Oracle.sdo_geometry" method.
 */
public class OracleSdoGeometryMethod extends AbstractSQLMethod
{
	/* (non-Javadoc)
     * @see org.datanucleus.store.rdbms.sql.method.SQLMethod#getExpression(org.datanucleus.store.rdbms.sql.expression.SQLExpression, java.util.List)
     */
    public SQLExpression getExpression(SQLExpression ignore, List args)
    {
        if (args == null || args.size() != 5)
        {
            throw new NucleusUserException("Cannot invoke Oracle.sdo_geometry without 5 arguments");
        }

        SQLExpression gtypeExpr = (SQLExpression)args.get(0); // Geometry Type
        SQLExpression sridExpr = (SQLExpression)args.get(1); // SRID
        SQLExpression pointExpr = (SQLExpression)args.get(2); // Point
        SQLExpression elemInfoExpr = (SQLExpression)args.get(3); // Element info
        SQLExpression ordinateExpr = (SQLExpression)args.get(4); // Ordinates

        ArrayList funcArgs = new ArrayList();
        funcArgs.add(gtypeExpr);
        funcArgs.add(sridExpr);
        funcArgs.add(pointExpr);
        funcArgs.add(elemInfoExpr);
        funcArgs.add(ordinateExpr);

        return new GeometryExpression(stmt, null, "SDO_GEOMETRY", funcArgs, null);
    }
}