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
   barisergun75@gmail.com
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
 * Implementation of Spatial "pointOnSurface" method for Oracle.
 * See https://docs.oracle.com/database/121/SPATL/sdo_geom-sdo_pointonsurface.htm#SPATL1124
 * This takes 2 arguments to the method unlike the other datastores.
 */
public class SpatialPointOnSurfaceMethod2 implements SQLMethod
{
    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.sql.method.SQLMethod#getExpression(org.datanucleus.store.rdbms.sql.expression.SQLExpression, java.util.List)
     */
    public SQLExpression getExpression(SQLStatement stmt, SQLExpression expr, List args)
    {
        if (args == null)
        {
            throw new NucleusUserException("Cannot invoke Spatial.pointOnSurface without arguments");
        }
        if (expr == null && args.size() != 2)
        {
            throw new NucleusUserException("Cannot invoke Spatial.pointOnSurface without 2 arguments");
        }
        else if (expr != null && args.size() != 1)
        {
            throw new NucleusUserException("Cannot invoke geom.getPointOnSurface() without 1 argument");
        }

        SQLExpression argExpr1 = expr;
        SQLExpression argExpr2 = (SQLExpression) args.get(0); // Geometry 2
        if (expr == null)
        {
            // "Spatial." method
            argExpr1 = (SQLExpression) args.get(0); // Geometry 1
            argExpr2 = (SQLExpression) args.get(1); // Geometry 2
        }

        ClassLoaderResolver clr = stmt.getQueryGenerator().getClassLoaderResolver();
        ArrayList funcArgs = new ArrayList();
        funcArgs.add(argExpr1);
        funcArgs.add(argExpr2);

        JavaTypeMapping geomMapping = SpatialMethodHelper.getGeometryMapping(clr, argExpr1);
        return new GeometryExpression(stmt, geomMapping, "SDO_GEOM.SDO_POINTONSURFACE", funcArgs, null);
    }
}