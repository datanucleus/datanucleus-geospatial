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
package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Implementation of "Spatial.buffer(expr, expr2)" or "{expr}.buffer(expr2)" method for Oracle.
 */
public class SpatialBufferMethod2 extends AbstractSQLMethod
{
    /* (non-Javadoc)
     * @see org.datanucleus.store.rdbms.sql.method.SQLMethod#getExpression(org.datanucleus.store.rdbms.sql.expression.SQLExpression, java.util.List)
     */
    public SQLExpression getExpression(SQLExpression expr, List args)
    {
        if (expr == null && (args == null || args.size() != 2))
        {
            throw new NucleusUserException("Cannot invoke Spatial.buffer without 2 arguments");
        }
        else if (expr != null && args != null && args.size() != 1)
        {
            throw new NucleusUserException("Cannot invoke geom.buffer without 1 argument");
        }

        SQLExpression geomExpr = null;
        SQLExpression distExpr = null;
        if (expr == null)
        {
            // "Spatial." method
            geomExpr = (SQLExpression)args.get(0);
            distExpr = (SQLExpression)args.get(1);
        }
        else
        {
            geomExpr = expr;
            distExpr = (SQLExpression)args.get(0);
        }

        ArrayList geomFuncArgs = new ArrayList();
        geomFuncArgs.add(geomExpr);
        GeometryExpression geomExpr2 = new GeometryExpression(stmt, null, "geometry.from_sdo_geom", geomFuncArgs, null);

        ArrayList geomBufferFuncArgs = new ArrayList();
        geomBufferFuncArgs.add(geomExpr2);
        geomBufferFuncArgs.add(distExpr);
        GeometryExpression geomBufferExpr = new GeometryExpression(stmt, null, "buffer", geomBufferFuncArgs, null);

        ArrayList funcArgs = new ArrayList();
        funcArgs.add(geomBufferExpr);
        JavaTypeMapping geomMapping = SpatialMethodHelper.getGeometryMapping(clr, geomExpr);
        return new GeometryExpression(stmt, geomMapping, "geometry.get_sdo_geom", funcArgs, null);
    }
}