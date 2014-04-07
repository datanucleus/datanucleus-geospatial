/**********************************************************************
Copyright (c) 2012 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.store.types.geospatial.query.inmemory;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.query.QueryUtils;
import org.datanucleus.query.expression.Expression;
import org.datanucleus.query.expression.InvokeExpression;
import org.datanucleus.query.expression.Literal;
import org.datanucleus.query.expression.ParameterExpression;
import org.datanucleus.query.expression.PrimaryExpression;
import org.datanucleus.query.inmemory.InMemoryExpressionEvaluator;
import org.datanucleus.query.inmemory.InvocationEvaluator;
import org.datanucleus.util.Localiser;

/**
 * Evaluator for the method "{rectExpr}.contains(pointExpr)" or "{rectExpr}.contains(rectExpr2)".
 */
public class RectangleContainsMethodEvaluator implements InvocationEvaluator
{
    /** Localisation utility for output messages */
    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.Localisation",
        org.datanucleus.ClassConstants.NUCLEUS_CONTEXT_LOADER);

    /*
     * (non-Javadoc)
     * @see
     * org.datanucleus.query.evaluator.memory.InvocationEvaluator#evaluate(org.datanucleus.query.expression
     * .InvokeExpression, org.datanucleus.query.evaluator.memory.InMemoryExpressionEvaluator)
     */
    public Object evaluate(InvokeExpression expr, Object invokedValue, InMemoryExpressionEvaluator eval)
    {
        String method = expr.getOperation();

        List<Expression> args = expr.getArguments();
        if (args == null || args.size() != 1)
        {
            throw new NucleusException("Should have 1 argument to Rectangle.contains(...)");
        }

        Object argVal = null;
        Object argExpr = expr.getArguments().get(0);
        if (argExpr instanceof PrimaryExpression)
        {
            PrimaryExpression primExpr = (PrimaryExpression) argExpr;
            argVal = eval.getValueForPrimaryExpression(primExpr);
        }
        else if (argExpr instanceof ParameterExpression)
        {
            ParameterExpression paramExpr = (ParameterExpression) argExpr;
            argVal = QueryUtils.getValueForParameterExpression(eval.getParameterValues(), paramExpr);
        }
        else if (argExpr instanceof Literal)
        {
            argVal = ((Literal) argExpr).getLiteral();
        }
        else if (argExpr instanceof InvokeExpression)
        {
            argVal = eval.getValueForInvokeExpression((InvokeExpression) argExpr);
        }
        else
        {
            throw new NucleusException(method + "(arg) where arg is instanceof " + argExpr.getClass().getName() + " not supported");
        }

        if (invokedValue == null)
        {
            // NPE likely better
            return null;
        }
        else if (invokedValue instanceof Rectangle)
        {
            if (argVal instanceof Point)
            {
                return ((Rectangle) invokedValue).contains((Point) argVal);
            }
            else if (argVal instanceof Rectangle)
            {
                return ((Rectangle) invokedValue).contains((Rectangle) argVal);
            }
            else
            {
                throw new NucleusException(method + "(arg) where arg is instanceof " + argVal.getClass().getName() + " not supported");
            }
        }
        else
        {
            throw new NucleusException(LOCALISER.msg("021011", method, invokedValue.getClass().getName()));
        }
    }
}