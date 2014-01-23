/*
 * Copyright 2014 Baris ERGUN.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.datanucleus.store.rdbms.schema;

import java.sql.ResultSet;

/**
 * 
 * @author barisergun
 */
public class OracleSpatialTypeInfo extends SQLTypeInfo
{
    public static final String TYPES_SDO_GEOMETRY_PATTERN = "[2-4][0,3,4][0-9][0-9]";

    public OracleSpatialTypeInfo(ResultSet rs)
    {
        super(rs);
    }

    public OracleSpatialTypeInfo(String typeName, short dataType, int precision, String literalPrefix, String literalSuffix,
            String createParams, int nullable, boolean caseSensitive, short searchable, boolean unsignedAttribute, boolean fixedPrecScale,
            boolean autoIncrement, String localTypeName, short minimumScale, short maximumScale, int numPrecRadix)
    {
        super(typeName, dataType, precision, literalPrefix, literalSuffix, createParams, nullable, caseSensitive, searchable,
                unsignedAttribute, fixedPrecScale, autoIncrement, localTypeName, minimumScale, maximumScale, numPrecRadix);
    }

    public Object clone()
    {
        return new OracleSpatialTypeInfo(getTypeName(), getDataType(), getPrecision(), getLiteralPrefix(), getLiteralSuffix(),
                getCreateParams(), getNullable(), isCaseSensitive(), getSearchable(), isUnsignedAttribute(), isFixedPrecScale(),
                isAutoIncrement(), getLocalTypeName(), getMinimumScale(), getMaximumScale(), getNumPrecRadix());
    }

}
