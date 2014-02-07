/**********************************************************************
 Copyright (c) 2006 Thomas Marti, Stefan Schmid and others. All rights reserved.
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
package org.datanucleus.store.rdbms.adapter;

import java.sql.DatabaseMetaData;
import java.sql.Types;

import static org.datanucleus.store.rdbms.schema.OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY_PATTERN;

import org.datanucleus.store.connection.ManagedConnection;
import org.datanucleus.store.rdbms.schema.OracleSpatialTypeInfo;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.store.rdbms.table.Table;
import org.datanucleus.store.schema.StoreSchemaHandler;

/**
 * Provides methods for adapting SQL language elements for Oracle spatial
 * elements.
 */
public class OracleSpatialAdapter extends OracleAdapter implements SpatialRDBMSAdapter
{
    public OracleSpatialAdapter(DatabaseMetaData metadata)
    {
        super(metadata);
    }

    /**
     * Initialise the types for this datastore.
     * @param handler SchemaHandler that we initialise the types for
     * @param mconn Managed connection to use
     */
    public void initialiseTypes(StoreSchemaHandler handler, ManagedConnection mconn)
    {
        super.initialiseTypes(handler, mconn);

        SQLTypeInfo sqlType = new org.datanucleus.store.rdbms.schema.OracleTypeInfo(
            "SDO_GEOMETRY", (short)Types.STRUCT, 0, null, null, null, 1, false, (short)0,
            false, false, false, "SDO_GEOMETRY", (short)0, (short)0, 10);
        addSQLTypeForJDBCType(handler, mconn, (short)OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY, sqlType, true);
    }

    public boolean isGeometryColumn(Column c)
    {
        return String.valueOf(c.getTypeInfo().getDataType()).matches(TYPES_SDO_GEOMETRY_PATTERN);
    }

    public String getRetrieveCrsNameStatement(Table table, int srid)
    {
        return "SELECT CS_NAME FROM MDSYS.CS_SRS WHERE SRID = #srid".replace("#srid", "" + srid);
    }

    public String getRetrieveCrsWktStatement(Table table, int srid)
    {
        return "SELECT WKTEXT FROM MDSYS.CS_SRS WHERE SRID = #srid".replace("#srid", "" + srid);
    }

    public String getCalculateBoundsStatement(Table table, Column column)
    {
        return "SELECT " + "SDO_GEOM.SDO_MIN_MBR_ORDINATE(SDO_AGGR_MBR(#column), 1), " + "SDO_GEOM.SDO_MIN_MBR_ORDINATE(SDO_AGGR_MBR(#column), 2), " + "SDO_GEOM.SDO_MAX_MBR_ORDINATE(SDO_AGGR_MBR(#column), 1), " + "SDO_GEOM.SDO_MAX_MBR_ORDINATE(SDO_AGGR_MBR(#column), 2) " + "FROM #table"
                .replace("#column", column.getIdentifier().getIdentifierName())
                .replace("#table", table.getIdentifier().getIdentifierName());
    }
}