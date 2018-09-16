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

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.types.geospatial.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.method.SQLMethod;

/**
 * Implementation of "Spatial.pointN(expr, expr2)" or "{expr}.getPointN(expr2)" method.
 */
public class SpatialPointNMethod implements SQLMethod
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
            throw new NucleusUserException("Cannot invoke Spatial.pointN without arguments");
        }
        if (expr == null && args.size() != 2)
        {
            throw new NucleusUserException("Cannot invoke Spatial.pointN without 2 arguments");
        }
        else if (expr != null && args.size() != 1)
        {
            throw new NucleusUserException("Cannot invoke geom.getPointN() without 1 argument");
        }

        SQLExpression geomExpr = expr;
        SQLExpression distExpr = (SQLExpression) args.get(0);
        if (expr == null)
        {
            // "Spatial." method
            geomExpr = (SQLExpression) args.get(0);
            distExpr = (SQLExpression) args.get(1);
        }

        ArrayList funcArgs = new ArrayList();
        funcArgs.add(geomExpr);
        funcArgs.add(distExpr);

        ClassLoaderResolver clr = stmt.getQueryGenerator().getClassLoaderResolver();
        JavaTypeMapping geomMapping = SpatialMethodHelper.getGeometryMapping(clr, geomExpr);
        return new GeometryExpression(stmt, geomMapping, "PointN", funcArgs, null);
    }
}