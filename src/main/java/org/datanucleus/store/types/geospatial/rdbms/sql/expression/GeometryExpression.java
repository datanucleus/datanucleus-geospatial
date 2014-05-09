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
package org.datanucleus.store.types.geospatial.rdbms.sql.expression;

import java.util.List;

import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.query.expression.Expression;
import org.datanucleus.query.expression.Expression.DyadicOperator;
import org.datanucleus.query.expression.Expression.MonadicOperator;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.types.geospatial.rdbms.mapping.jts.GeometryMapping;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.SQLTable;
import org.datanucleus.store.rdbms.sql.expression.BooleanExpression;
import org.datanucleus.store.rdbms.sql.expression.NullLiteral;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Representation of a Geometry expression in an SQL statement.
 */
public class GeometryExpression extends SQLExpression
{
    /**
     * Perform an operation
     * 
     * <pre>
     * op
     * </pre>
     * 
     * between
     * 
     * <pre>
     * expr1
     * </pre>
     * 
     * and
     * 
     * <pre>
     * expr2
     * </pre>
     * 
     * .
     * @param stmt The statement
     * @param table Table
     * @param mapping The mapping in the table for this expression
     */
    public GeometryExpression(SQLStatement stmt, SQLTable table, JavaTypeMapping mapping)
    {
        super(stmt, table, getMappingForUseWithExpression(mapping));
    }

    /**
     * Perform an operation
     * 
     * <pre>
     * op
     * </pre>
     * 
     * between
     * 
     * <pre>
     * expr1
     * </pre>
     * 
     * and
     * 
     * <pre>
     * expr2
     * </pre>
     * 
     * .
     * @param op Operation
     * @param expr1 The expression
     */
    public GeometryExpression(MonadicOperator op, SQLExpression expr1)
    {
        super(op, expr1);
    }

    /**
     * Perform an operation
     * 
     * <pre>
     * op
     * </pre>
     * 
     * between
     * 
     * <pre>
     * expr1
     * </pre>
     * 
     * and
     * 
     * <pre>
     * expr2
     * </pre>
     * 
     * .
     * @param expr1 First expression
     * @param op The operation
     * @param expr2 Second operation
     */
    public GeometryExpression(SQLExpression expr1, DyadicOperator op, SQLExpression expr2)
    {
        super(expr1, op, expr2);
    }

    /**
     * Generates statement as "FUNCTION_NAME(arg [AS type] [,argN [AS typeN]])".
     * @param stmt SQL statement
     * @param mapping Mapping to use
     * @param functionName Name of function
     * @param args The args
     * @param types Types of the args
     */
    public GeometryExpression(SQLStatement stmt, JavaTypeMapping mapping, String functionName, List args, List types)
    {
        super(stmt, getMappingForUseWithExpression(mapping), functionName, args, types);
    }

    protected static JavaTypeMapping getMappingForUseWithExpression(JavaTypeMapping mapping)
    {
        if (mapping == null)
        {
            return null;
        }
        else if (mapping instanceof GeometryMapping)
        {
            return ((GeometryMapping) mapping).getMappingWithoutUserData();
        }
        return mapping;
    }

    public BooleanExpression eq(SQLExpression expr)
    {
        if (expr instanceof NullLiteral)
        {
            return expr.eq(this);
        }
        else if (expr instanceof GeometryLiteral)
        {
            return new BooleanExpression(this, Expression.OP_EQ, expr);
        }
        else if (expr instanceof GeometryExpression)
        {
            return new BooleanExpression(this, Expression.OP_EQ, expr);
        }
        else
        {
            return super.eq(expr);
        }
    }

    public BooleanExpression ne(SQLExpression expr)
    {
        if (expr instanceof NullLiteral)
        {
            return expr.ne(this);
        }
        else if (expr instanceof GeometryLiteral)
        {
            return new BooleanExpression(this, Expression.OP_NOTEQ, expr);
        }
        else if (expr instanceof GeometryExpression)
        {
            return new BooleanExpression(this, Expression.OP_NOTEQ, expr);
        }
        else
        {
            return super.ne(expr);
        }
    }

    public SQLExpression invoke(String methodName, List args)
    {
        if (mapping == null)
        {
            throw new NucleusException("Call to invoke on " + this + " but the expression has a null mapping!");
        }
        return stmt.getRDBMSManager().getSQLExpressionFactory().invokeMethod(stmt, mapping.getType(), methodName, this, args);
    }
}