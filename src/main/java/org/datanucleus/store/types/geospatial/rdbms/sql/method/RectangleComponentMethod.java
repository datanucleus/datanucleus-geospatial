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
package org.datanucleus.store.types.geospatial.rdbms.sql.method;

import java.util.List;

import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.types.geospatial.rdbms.mapping.RectangleMapping;
import org.datanucleus.store.rdbms.mapping.datastore.DatastoreMapping;
import org.datanucleus.store.rdbms.sql.expression.NumericExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.method.AbstractSQLMethod;

/**
 * Abstract implementation of method for component access to Rectangle objects.
 */
public abstract class RectangleComponentMethod extends AbstractSQLMethod
{
    public SQLExpression getExpressionForMapping(SQLExpression expr, List args, int mappingIndex)
    {
        if (expr == null || (args != null && args.size() > 0))
        {
            throw new NucleusUserException("Cannot invoke Rectangle.getX/getY/getWidth/getHeight on null object");
        }

        DatastoreMapping datastoreMapping = expr.getJavaTypeMapping().getDatastoreMapping(0);
        Object javaTypeMapping = datastoreMapping.getJavaTypeMapping();
        if (!(javaTypeMapping instanceof RectangleMapping))
        {
            throw new NucleusException("Can only be used with 'RectangleMapping' Java Type mappings.");
        }

        RectangleMapping rectMapping = (RectangleMapping) javaTypeMapping;
        RectangleComponentMapping rectCmptMapping = new RectangleComponentMapping(rectMapping, mappingIndex);
        return new NumericExpression(stmt, expr.getSQLTable(), rectCmptMapping);
    }
}
