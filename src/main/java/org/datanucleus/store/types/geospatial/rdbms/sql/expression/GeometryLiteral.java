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

import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.BooleanExpression;
import org.datanucleus.store.rdbms.sql.expression.BooleanLiteral;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

import org.datanucleus.store.rdbms.sql.expression.SQLLiteral;

/**
 * Representation of a Geometry literal.
 */
public class GeometryLiteral extends GeometryExpression implements SQLLiteral
{
    private final Object value;

    /**
     * Constructor for a Geometry literal with a value.
     * @param stmt the SQL statement
     * @param mapping the mapping
     * @param value the value
     * @param parameterName Name of the parameter that this represents if any (as JDBC "?")
     */
    public GeometryLiteral(SQLStatement stmt, JavaTypeMapping mapping, Object value, String parameterName)
    {
        super(stmt, null, mapping);
        this.parameterName = parameterName;

        if (value == null)
        {
            this.value = null;
        }
        else
        {
            this.value = value;
        }

        setStatement();
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.sql.expression.SQLLiteral#getValue()
     */
    public Object getValue()
    {
        return value;
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.sql.expression.SQLLiteral#setNotParameter()
     */
    public void setNotParameter()
    {
        if (parameterName == null)
        {
            return;
        }
        parameterName = null;
        st.clearStatement();
        setStatement();
    }

    protected void setStatement()
    {
        if (parameterName == null)
        {
            st.append(String.valueOf(value));
        }
        else
        {
            st.appendParameter(parameterName, mapping, this.value);
        }
    }

    public BooleanExpression eq(SQLExpression expr)
    {
        if (expr instanceof GeometryLiteral)
        {
            return new BooleanLiteral(stmt, mapping, Boolean.valueOf(value.equals(((GeometryLiteral) expr).value)));
        }
        return super.eq(expr);
    }

    public BooleanExpression ne(SQLExpression expr)
    {
        if (expr instanceof GeometryLiteral)
        {
            return new BooleanLiteral(stmt, mapping, Boolean.valueOf(!value.equals(((GeometryLiteral) expr).value)));
        }
        return super.ne(expr);
    }
}