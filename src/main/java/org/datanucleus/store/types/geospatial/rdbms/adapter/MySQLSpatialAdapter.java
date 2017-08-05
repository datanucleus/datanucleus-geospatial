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

import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.Types;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.plugin.PluginManager;
import org.datanucleus.store.connection.ManagedConnection;
import org.datanucleus.store.rdbms.adapter.MySQLAdapter;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.store.rdbms.table.Table;
import org.datanucleus.store.schema.StoreSchemaHandler;

/**
 * Provides methods for adapting SQL language elements for MySQL spatial elements.
 */
public class MySQLSpatialAdapter extends MySQLAdapter implements SpatialRDBMSAdapter
{
    public MySQLSpatialAdapter(DatabaseMetaData metadata)
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

        // Add on any missing JDBC types
        SQLTypeInfo sqlType = MySQLSpatialTypeInfo.TYPEINFO_PROTOTYPE;
        addSQLTypeForJDBCType(handler, mconn, (short) Types.BINARY, sqlType, true);
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.rdbms.adapter.MySQLAdapter#loadDatastoreMappings(org.datanucleus.plugin.PluginManager, org.datanucleus.ClassLoaderResolver)
     */
    @Override
    protected void loadDatastoreMappings(PluginManager mgr, ClassLoaderResolver clr)
    {
        // jgeom2mysql
        registerDatastoreMapping(oracle.spatial.geometry.JGeometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jgeom2mysql.JGeometryRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);

        // jts2mysql
        registerDatastoreMapping(com.vividsolutions.jts.geom.Geometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.GeometryRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.GeometryCollection.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.GeometryCollectionRDBMSMapping.class, 
            JDBCType.BINARY, "geometrycollection", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.LinearRing.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.LinearRingRDBMSMapping.class, 
            JDBCType.BINARY, "linestring", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.LineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.LineStringRDBMSMapping.class, 
            JDBCType.BINARY, "linestring", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.MultiLineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.MultiLineStringRDBMSMapping.class, 
            JDBCType.BINARY, "multilinestring", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.MultiPolygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.MultiPolygonRDBMSMapping.class, 
            JDBCType.BINARY, "multipolygon", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.MultiPoint.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.MultiPointRDBMSMapping.class, 
            JDBCType.BINARY, "multipoint", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.Point.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.PointRDBMSMapping.class, 
            JDBCType.BINARY, "point", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.Polygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.PolygonRDBMSMapping.class, 
            JDBCType.BINARY, "polygon", true);

        // pg2mysql
        registerDatastoreMapping(org.postgis.Geometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.GeometryRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);
        registerDatastoreMapping(org.postgis.GeometryCollection.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.GeometryCollectionRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);
        registerDatastoreMapping(org.postgis.LinearRing.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.LinearRingRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);
        registerDatastoreMapping(org.postgis.LineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.LineStringRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);
        registerDatastoreMapping(org.postgis.MultiLineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.MultiLineStringRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);
        registerDatastoreMapping(org.postgis.MultiPolygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.MultiPolygonRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);
        registerDatastoreMapping(org.postgis.MultiPoint.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.MultiPointRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);
        registerDatastoreMapping(org.postgis.Point.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.PointRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);
        registerDatastoreMapping(org.postgis.Polygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.PolygonRDBMSMapping.class, 
            JDBCType.BINARY, "geometry", true);
        
        super.loadDatastoreMappings(mgr, clr);
    }

    public boolean isGeometryColumn(Column c)
    {
        SQLTypeInfo typeInfo = c.getTypeInfo();
        if (typeInfo == null)
        {
            return false;
        }
        return typeInfo.getTypeName().equalsIgnoreCase("geometry") || typeInfo.getTypeName().equalsIgnoreCase("geometrycollection");
    }

    public String getRetrieveCrsNameStatement(Table table, int srid)
    {
        return null;
    }

    public String getRetrieveCrsWktStatement(Table table, int srid)
    {
        return null;
    }

    public String getCalculateBoundsStatement(Table table, Column column)
    {
        return "SELECT " + "min(X(PointN(ExteriorRing(Envelope(#column1)),1))), " + "min(Y(PointN(ExteriorRing(Envelope(#column2)),1))), " + "max(X(PointN(ExteriorRing(Envelope(#column3)),1))), " + "max(Y(PointN(ExteriorRing(Envelope(#column4)),1))) " + "FROM #table"
                .replace("#column", column.getIdentifier().getName())
                .replace("#table", table.getIdentifier().getName());
    }
}