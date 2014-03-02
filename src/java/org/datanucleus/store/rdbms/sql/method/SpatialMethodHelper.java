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

import java.util.List;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.query.expression.Expression;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.BooleanExpression;
import org.datanucleus.store.rdbms.sql.expression.CharacterLiteral;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpressionFactory;
import org.datanucleus.store.rdbms.sql.expression.StringExpression;

/**
 * Helper class for creating method handlers.
 */
public class SpatialMethodHelper
{
    /**
     * Method to return the java type mapping to use for the geometry of the input expression. Uses the java
     * type represented by the expression to define which Geometry class to use.
     * @param clr ClassLoader resolver
     * @param expr The expression
     * @return The mapping to use.
     */
    public static JavaTypeMapping getGeometryMapping(ClassLoaderResolver clr, SQLExpression expr)
    {
        if (expr.getJavaTypeMapping() == null)
        {
            return null;
        }

        String className;
        String geometryPackageName = expr.getJavaTypeMapping().getJavaType().getPackage().getName();
        if (geometryPackageName.equals("org.postgis"))
        {
            className = geometryPackageName + ".Geometry";
        }
        else if (geometryPackageName.equals("com.vividsolutions.jts.geom"))
        {
            className = geometryPackageName + ".Geometry";
        }
        else if (geometryPackageName.equals("oracle.spatial.geometry"))
        {
            className = geometryPackageName + ".JGeometry";
        }
        else
        {
            return null;
        }

        Class geometryClass = clr.classForName(className);
        if (geometryClass == null)
        {
            return null;
        }

        return expr.getSQLStatement().getRDBMSManager().getMappingManager()
                .getMappingWithDatastoreMapping(geometryClass, false, false, clr);
    }

    /**
     * Convenience accessor for a boolean expression for a geometry expression. Geometry expressions seem to
     * return a string "1" or "0" if true or false respectively so use an equality expression of that form.
     * @param stmt The statement that this is part of
     * @param funcName Function name
     * @param funcArgs Args to the function
     * @param exprFactory The expression factory
     * @return The boolean expression
     */
    public static BooleanExpression getBooleanExpression(SQLStatement stmt, String funcName, List funcArgs, SQLExpressionFactory exprFactory)
    {
        JavaTypeMapping boolMapping = exprFactory.getMappingForType(boolean.class, true);
        SQLExpression left = new StringExpression(stmt, boolMapping, funcName, funcArgs);
        JavaTypeMapping charMapping = exprFactory.getMappingForType(Character.class, false);
        SQLExpression right = new CharacterLiteral(stmt, charMapping, Character.valueOf('1'), null);
        return new BooleanExpression(left, Expression.OP_EQ, right);
    }
}