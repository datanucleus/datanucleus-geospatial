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

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.types.geospatial.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.method.SQLMethod;

/**
 * Implementation of "Spatial.startPoint(expr)" or "{expr}.getStartPoint()" method for PostGis.
 */
public class SpatialStartPointMethod3 implements SQLMethod
{
    public SQLExpression getExpression(SQLStatement stmt, SQLExpression expr, List args)
    {
        if (expr == null && (args == null || args.size() != 1))
        {
            throw new NucleusUserException("Cannot invoke Spatial.startPoint without 1 argument");
        }
        else if (expr != null && args != null && args.size() != 0)
        {
            throw new NucleusUserException("Cannot invoke geom.getStartPoint() with an argument");
        }

        SQLExpression argExpr = expr;
        if (expr == null)
        {
            argExpr = (SQLExpression) args.get(0);
        }

        ArrayList<SQLExpression> funcArgs = new ArrayList<SQLExpression>();
        funcArgs.add(argExpr);

        ClassLoaderResolver clr = stmt.getQueryGenerator().getClassLoaderResolver();
        JavaTypeMapping geomMapping = SpatialMethodHelper.getGeometryMapping(clr, argExpr);
        return new GeometryExpression(stmt, geomMapping, "st_startpoint", funcArgs, null);
    }
}
