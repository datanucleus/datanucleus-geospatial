/**********************************************************************
Copyright (c) 2008 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.store.rdbms.schema;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Types;

/**
 * SQL Type info for PostGIS datastores.
 */
public class PostGISTypeInfo extends SQLTypeInfo
{
    public static final PostGISTypeInfo TYPEINFO_PROTOTYPE = new PostGISTypeInfo("geometry", (short) Types.OTHER, 0, "", "", "",
            (short) DatabaseMetaData.typeNullable, false, (short) DatabaseMetaData.typeSearchable, false, false, false, "", (short) 0,
            (short) 0, 10);

    /**
     * @param rs ResultSet (from DatabaseMetaData.getTypeInfo() for example).
     */
    public PostGISTypeInfo(ResultSet rs)
    {
        super(rs);
    }

    public PostGISTypeInfo(String typeName, short dataType, int precision, String literalPrefix, String literalSuffix, String createParams,
            int nullable, boolean caseSensitive, short searchable, boolean unsignedAttribute, boolean fixedPrecScale,
            boolean autoIncrement, String localTypeName, short minimumScale, short maximumScale, int numPrecRadix)
    {
        super(typeName, dataType, precision, literalPrefix, literalSuffix, createParams, nullable, caseSensitive, searchable,
                unsignedAttribute, fixedPrecScale, autoIncrement, localTypeName, minimumScale, maximumScale, numPrecRadix);
    }

    public Object clone()
    {
        return new PostGISTypeInfo(getTypeName(), getDataType(), getPrecision(), getLiteralPrefix(), getLiteralSuffix(), getCreateParams(),
                getNullable(), isCaseSensitive(), getSearchable(), isUnsignedAttribute(), isFixedPrecScale(), isAutoIncrement(),
                getLocalTypeName(), getMinimumScale(), getMaximumScale(), getNumPrecRadix());
    }
}