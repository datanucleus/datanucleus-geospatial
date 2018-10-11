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
import org.datanucleus.exceptions.ClassNotResolvedException;
import org.datanucleus.plugin.PluginManager;
import org.datanucleus.store.connection.ManagedConnection;
import org.datanucleus.store.rdbms.adapter.MySQLAdapter;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.store.rdbms.table.Table;
import org.datanucleus.store.schema.StoreSchemaHandler;
import org.datanucleus.util.NucleusLogger;

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
     * @see org.datanucleus.store.rdbms.adapter.MySQLAdapter#loadColumnMappings(org.datanucleus.plugin.PluginManager, org.datanucleus.ClassLoaderResolver)
     */
    @Override
    protected void loadColumnMappings(PluginManager mgr, ClassLoaderResolver clr)
    {
        try
        {
            Class cls = clr.classForName("oracle.spatial.geometry.JGeometry");
            if (cls != null)
            {
                // jgeom2mysql
                registerColumnMapping(oracle.spatial.geometry.JGeometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jgeom2mysql.JGeometryColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
            }
        }
        catch (Throwable thr)
        {
            NucleusLogger.DATASTORE.warn("Not loading RDBMS support for Oracle JGeometry since not present");
        }

        try
        {
            Class cls = clr.classForName("com.vividsolutions.jts.geom.Geometry");
            if (cls != null)
            {
                // jts2mysql
                registerColumnMapping(com.vividsolutions.jts.geom.Geometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.GeometryColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
                registerColumnMapping(com.vividsolutions.jts.geom.GeometryCollection.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.GeometryCollectionColumnMapping.class, 
                    JDBCType.BINARY, "geometrycollection", true);
                registerColumnMapping(com.vividsolutions.jts.geom.LinearRing.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.LinearRingColumnMapping.class, 
                    JDBCType.BINARY, "linestring", true);
                registerColumnMapping(com.vividsolutions.jts.geom.LineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.LineStringColumnMapping.class, 
                    JDBCType.BINARY, "linestring", true);
                registerColumnMapping(com.vividsolutions.jts.geom.MultiLineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.MultiLineStringColumnMapping.class, 
                    JDBCType.BINARY, "multilinestring", true);
                registerColumnMapping(com.vividsolutions.jts.geom.MultiPolygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.MultiPolygonColumnMapping.class, 
                    JDBCType.BINARY, "multipolygon", true);
                registerColumnMapping(com.vividsolutions.jts.geom.MultiPoint.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.MultiPointColumnMapping.class, 
                    JDBCType.BINARY, "multipoint", true);
                registerColumnMapping(com.vividsolutions.jts.geom.Point.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.PointColumnMapping.class, 
                    JDBCType.BINARY, "point", true);
                registerColumnMapping(com.vividsolutions.jts.geom.Polygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql.PolygonColumnMapping.class, 
                    JDBCType.BINARY, "polygon", true);
            }
        }
        catch (Throwable thr)
        {
            NucleusLogger.DATASTORE.warn("Not loading RDBMS support for Vividsolutions JTS types since not present");
        }

        try
        {
            Class cls = clr.classForName("org.postgis.Geometry");
            if (cls != null)
            {
                // pg2mysql
                registerColumnMapping(org.postgis.Geometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.GeometryColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
                registerColumnMapping(org.postgis.GeometryCollection.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.GeometryCollectionColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
                registerColumnMapping(org.postgis.LinearRing.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.LinearRingColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
                registerColumnMapping(org.postgis.LineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.LineStringColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
                registerColumnMapping(org.postgis.MultiLineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.MultiLineStringColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
                registerColumnMapping(org.postgis.MultiPolygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.MultiPolygonColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
                registerColumnMapping(org.postgis.MultiPoint.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.MultiPointColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
                registerColumnMapping(org.postgis.Point.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.PointColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
                registerColumnMapping(org.postgis.Polygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2mysql.PolygonColumnMapping.class, 
                    JDBCType.BINARY, "geometry", true);
            }
        }
        catch (Throwable thr)
        {
            NucleusLogger.DATASTORE.warn("Not loading RDBMS support for PostGIS types since not present");
        }
        
        super.loadColumnMappings(mgr, clr);
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

    /* (non-Javadoc)
     * @see org.datanucleus.store.rdbms.adapter.MySQLAdapter#getSQLMethodClass(java.lang.String, java.lang.String, org.datanucleus.ClassLoaderResolver)
     */
    @Override
    public Class getSQLMethodClass(String className, String methodName, ClassLoaderResolver clr)
    {
        if (className == null)
        {
            // Static methods, or functions
            if ("Spatial.envelope".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEnvelopeMethod.class;
            if ("Spatial.dimension".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDimensionMethod.class;
            if ("Spatial.boundary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBoundaryMethod.class;
            if ("Spatial.srid".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSridMethod.class;
            if ("Spatial.isSimple".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsSimpleMethod3.class;
            if ("Spatial.isEmpty".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsEmptyMethod3.class;
            if ("Spatial.asBinary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsBinaryMethod.class;
            if ("Spatial.asText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsTextMethod.class;
            if ("Spatial.geometryType".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryTypeMethod.class;

            if ("Spatial.contains".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialContainsMethod.class;
            if ("Spatial.overlaps".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialOverlapsMethod.class;
            if ("Spatial.touches".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialTouchesMethod.class;
            if ("Spatial.crosses".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCrossesMethod.class;
            if ("Spatial.within".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialWithinMethod.class;
            if ("Spatial.intersects".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectsMethod.class;
            if ("Spatial.equals".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEqualsMethod.class;
            if ("Spatial.disjoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDisjointMethod.class;
            if ("Spatial.relate".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialRelateMethod3.class;
            if ("Spatial.distance".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDistanceMethod3.class;
            if ("Spatial.intersection".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectionMethod.class;
            if ("Spatial.buffer".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBufferMethod.class;
            if ("Spatial.convexHull".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialConvexHullMethod.class;
            if ("Spatial.symDifference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSymDifferenceMethod.class;
            if ("Spatial.difference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDifferenceMethod.class;
            if ("Spatial.union".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialUnionMethod.class;

            if ("Spatial.length".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialLengthMethod.class;

            if ("Spatial.numPoints".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumPointsMethod.class;

            if ("Spatial.centroid".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCentroidMethod.class;
            if ("Spatial.area".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAreaMethod.class;
            if ("Spatial.pointOnSurface".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointOnSurfaceMethod.class;

            if ("Spatial.numGeometries".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumGeometriesMethod.class;
            if ("Spatial.geometryN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryNMethod.class;

            if ("Spatial.x".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialXMethod.class;
            if ("Spatial.y".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialYMethod.class;

            if ("Spatial.isRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsRingMethod3.class;
            if ("Spatial.isClosed".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsClosedMethod.class;
            if ("Spatial.startPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialStartPointMethod.class;
            if ("Spatial.endPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEndPointMethod.class;
            if ("Spatial.pointN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointNMethod.class;

            if ("Spatial.exteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialExteriorRingMethod.class;
            if ("Spatial.numInteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumInteriorRingMethod.class;
            if ("Spatial.interiorRingN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialInteriorRingNMethod.class;

            // Purely static methods
            if ("MySQL.mbrEqual".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.MySqlMbrEqualMethod.class;
            if ("MySQL.mbrDisjoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.MySqlMbrDisjointMethod.class;
            if ("MySQL.mbrIntersects".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.MySqlMbrIntersectsMethod.class;
            if ("MySQL.mbrTouches".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.MySqlMbrTouchesMethod.class;
            if ("MySQL.mbrWithin".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.MySqlMbrWithinMethod.class;
            if ("MySQL.mbrContains".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.MySqlMbrContainsMethod.class;
            if ("MySQL.mbrOverlaps".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.MySqlMbrOverlapsMethod.class;

            if ("Spatial.bboxTest".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBboxTestMethod2.class;

            if ("Spatial.geomFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromTextMethod.class;
            if ("Spatial.pointFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointFromTextMethod.class;
            if ("Spatial.lineFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialLineFromTextMethod.class;
            if ("Spatial.polyFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPolyFromTextMethod.class;
            if ("Spatial.mLineFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMLineFromTextMethod.class;
            if ("Spatial.mPointFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMPointFromTextMethod.class;
            if ("Spatial.mPolyFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMPolyFromTextMethod.class;
            if ("Spatial.geomCollFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomCollFromTextMethod.class;

            if ("Spatial.geomFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromWKBMethod.class;
            if ("Spatial.geomCollFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomCollFromWKBMethod.class;
            if ("Spatial.pointFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointFromWKBMethod.class;
            if ("Spatial.mPointFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMPointFromWKBMethod.class;
            if ("Spatial.lineFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialLineFromWKBMethod.class;
            if ("Spatial.mLineFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMLineFromWKBMethod.class;
            if ("Spatial.polyFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPolyFromWKBMethod.class;
            if ("Spatial.mPolyFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMPolyFromWKBMethod.class;

        }
        else
        {
            // Methods invoked on an object
            Class cls = null;
            try
            {
                cls = clr.classForName(className);
            }
            catch (ClassNotResolvedException cnre) {}

            boolean isGeometry = false;
            boolean isPoint = false;
            boolean isLineString = false;
            boolean isLinearRing = false;
            boolean isMultiLineString = false;
            boolean isPolygon = false;
            if (className.startsWith("com.vividsolutions.jts"))
            {
                if (com.vividsolutions.jts.geom.Geometry.class.getName().equals(className) || (cls != null && com.vividsolutions.jts.geom.Geometry.class.isAssignableFrom(cls)))
                {
                    isGeometry = true;
                }
                if (com.vividsolutions.jts.geom.Point.class.getName().equals(className) || (cls != null && com.vividsolutions.jts.geom.Point.class.isAssignableFrom(cls)))
                {
                    isPoint = true;
                }
                if (com.vividsolutions.jts.geom.LineString.class.getName().equals(className) || (cls != null && com.vividsolutions.jts.geom.LineString.class.isAssignableFrom(cls)))
                {
                    isLineString = true;
                }
                if (com.vividsolutions.jts.geom.MultiLineString.class.getName().equals(className) || (cls != null && com.vividsolutions.jts.geom.MultiLineString.class.isAssignableFrom(cls)))
                {
                    isMultiLineString = true;
                }
                if (com.vividsolutions.jts.geom.LinearRing.class.getName().equals(className) || (cls != null && com.vividsolutions.jts.geom.LinearRing.class.isAssignableFrom(cls)))
                {
                    isLinearRing = true;
                }
                if (com.vividsolutions.jts.geom.Polygon.class.getName().equals(className) || (cls != null && com.vividsolutions.jts.geom.Polygon.class.isAssignableFrom(cls)))
                {
                    isPolygon = true;
                }
            }
            else if (className.startsWith("org.postgis"))
            {
                if (org.postgis.Geometry.class.getName().equals(className) || (cls != null && org.postgis.Geometry.class.isAssignableFrom(cls)))
                {
                    isGeometry = true;
                }
                if (org.postgis.Point.class.getName().equals(className) || (cls != null && org.postgis.Point.class.isAssignableFrom(cls)))
                {
                    isPoint = true;
                }
                if (org.postgis.LineString.class.getName().equals(className) || (cls != null && org.postgis.LineString.class.isAssignableFrom(cls)))
                {
                    isLineString = true;
                }
                if (org.postgis.MultiLineString.class.getName().equals(className) || (cls != null && org.postgis.MultiLineString.class.isAssignableFrom(cls)))
                {
                    isMultiLineString = true;
                }
                if (org.postgis.LinearRing.class.getName().equals(className) || (cls != null && org.postgis.LinearRing.class.isAssignableFrom(cls)))
                {
                    isLinearRing = true;
                }
                if (org.postgis.Polygon.class.getName().equals(className) || (cls != null && org.postgis.Polygon.class.isAssignableFrom(cls)))
                {
                    isPolygon = true;
                }
            }
            else if (className.startsWith("oracle.spatial"))
            {
                // No obvious way of separating types from the class with Oracle
                isGeometry = true;
                isPoint = true;
                isLineString = true;
                isLinearRing = true;
                isMultiLineString = true;
                isPolygon = true;
            }

            if (isGeometry)
            {
                if ("getEnvelope".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEnvelopeMethod.class;
                if ("getDimension".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDimensionMethod.class;
                if ("getBoundary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBoundaryMethod3.class;
                if ("getSRID".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSridMethod.class;
                if ("isSimple".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsSimpleMethod3.class;
                if ("isEmpty".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsEmptyMethod.class;
                if ("toText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsTextMethod.class;
                if ("toBinary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsBinaryMethod.class;
                if ("getGeometryType".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryTypeMethod.class;

                if ("contains".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialContainsMethod.class;
                if ("overlaps".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialOverlapsMethod.class;
                if ("touches".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialTouchesMethod.class;
                if ("crosses".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCrossesMethod.class;
                if ("within".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialWithinMethod.class;
                if ("intersects".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectsMethod.class;
                if ("equals".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEqualsMethod.class;
                if ("disjoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDisjointMethod.class;
                if ("relate".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialRelateMethod.class;
                if ("distance".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDistanceMethod3.class;
                if ("intersection".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectionMethod.class;
                if ("buffer".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBufferMethod.class;
                if ("convexHull".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialConvexHullMethod.class;
                if ("symDifference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSymDifferenceMethod.class;
                if ("difference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDifferenceMethod.class;
                if ("union".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialUnionMethod.class;

                // Curve
                if ("getLength".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialLengthMethod.class;

                // LineString
                if ("getNumPoints".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumPointsMethod.class;

                // Surface
                if ("getCentroid".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCentroidMethod.class;
                if ("getArea".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAreaMethod.class;
                if ("getPointOnSurface".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointOnSurfaceMethod.class;

                // GeomCollection
                if ("getNumGeometries".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumGeometriesMethod.class;
                if ("getGeometryN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryNMethod.class;

                if (isPoint)
                {
                    if ("getX".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialXMethod.class;
                    if ("getY".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialYMethod.class;
                }
                if (isLineString)
                {
                    if ("isRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsRingMethod3.class;
                    if ("isClosed".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsClosedMethod.class;
                    if ("getStartPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialStartPointMethod.class;
                    if ("getEndPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEndPointMethod.class;
                    if ("getPointN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointNMethod.class;
                }
                if (isMultiLineString || isLinearRing)
                {
                    if ("isClosed".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsClosedMethod.class;
                }
                if (isPolygon)
                {
                    if ("getExteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialExteriorRingMethod.class;
                    if ("getInteriorRingN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialInteriorRingNMethod.class;
                    if ("getNumInteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumInteriorRingMethod.class;
                }
            }
        }

        return super.getSQLMethodClass(className, methodName, clr);
    }
}