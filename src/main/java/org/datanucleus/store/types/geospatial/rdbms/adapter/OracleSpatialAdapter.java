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
package org.datanucleus.store.types.geospatial.rdbms.adapter;

import static org.datanucleus.store.types.geospatial.rdbms.adapter.OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY_PATTERN;

import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.Types;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.plugin.PluginManager;
import org.datanucleus.store.connection.ManagedConnection;
import org.datanucleus.store.rdbms.adapter.OracleAdapter;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.store.rdbms.table.Table;
import org.datanucleus.store.schema.StoreSchemaHandler;

/**
 * Provides methods for adapting SQL language elements for Oracle spatial elements.
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

        SQLTypeInfo sqlType = new org.datanucleus.store.rdbms.adapter.OracleTypeInfo("SDO_GEOMETRY", (short) Types.STRUCT, 0, null, null,
                null, 1, false, (short) 0, false, false, false, "SDO_GEOMETRY", (short) 0, (short) 0, 10);
        addSQLTypeForJDBCType(handler, mconn, (short) OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY, sqlType, true);
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.rdbms.adapter.OracleAdapter#loadDatastoreMappings(org.datanucleus.plugin.PluginManager, org.datanucleus.ClassLoaderResolver)
     */
    @Override
    protected void loadDatastoreMappings(PluginManager mgr, ClassLoaderResolver clr)
    {
        // jgeom2oracle
        registerDatastoreMapping(oracle.spatial.geometry.JGeometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jgeom2oracle.JGeometryRDBMSMapping.class, 
            JDBCType.STRUCT, "SDO_GEOMETRY", true);

        // jts2oracle
        registerDatastoreMapping(com.vividsolutions.jts.geom.Geometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2oracle.GeometryRDBMSMapping.class, 
            JDBCType.STRUCT, "SDO_GEOMETRY", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.GeometryCollection.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2oracle.GeometryRDBMSMapping.class, 
            JDBCType.STRUCT, "SDO_GEOMETRY", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.LinearRing.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2oracle.LinearRingRDBMSMapping.class, 
            JDBCType.STRUCT, "SDO_GEOMETRY", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.LineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2oracle.GeometryRDBMSMapping.class, 
            JDBCType.STRUCT, "SDO_GEOMETRY", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.MultiLineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2oracle.GeometryRDBMSMapping.class, 
            JDBCType.STRUCT, "SDO_GEOMETRY", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.MultiPolygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2oracle.GeometryRDBMSMapping.class, 
            JDBCType.STRUCT, "SDO_GEOMETRY", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.MultiPoint.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2oracle.GeometryRDBMSMapping.class, 
            JDBCType.STRUCT, "SDO_GEOMETRY", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.Point.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2oracle.GeometryRDBMSMapping.class, 
            JDBCType.STRUCT, "SDO_GEOMETRY", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.Polygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2oracle.GeometryRDBMSMapping.class, 
            JDBCType.STRUCT, "SDO_GEOMETRY", true);

        super.loadDatastoreMappings(mgr, clr);
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
                .replace("#column", column.getIdentifier().getName())
                .replace("#table", table.getIdentifier().getName());
    }
}