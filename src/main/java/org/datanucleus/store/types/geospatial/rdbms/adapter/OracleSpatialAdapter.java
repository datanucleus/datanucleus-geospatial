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
import org.datanucleus.exceptions.ClassNotResolvedException;
import org.datanucleus.plugin.PluginManager;
import org.datanucleus.store.connection.ManagedConnection;
import org.datanucleus.store.rdbms.adapter.OracleAdapter;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.store.rdbms.table.Table;
import org.datanucleus.store.schema.StoreSchemaHandler;
import org.datanucleus.util.NucleusLogger;

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
        try
        {
            Class cls = clr.classForName("oracle.spatial.geometry.JGeometry");
            if (cls != null)
            {
                // jgeom2oracle
                registerDatastoreMapping(oracle.spatial.geometry.JGeometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jgeom2oracle.JGeometryRDBMSMapping.class, 
                    JDBCType.STRUCT, "SDO_GEOMETRY", true);
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
            }
        }
        catch (Throwable thr)
        {
            NucleusLogger.DATASTORE.warn("Not loading RDBMS support for Vividsolutions JTS types since not present");
        }

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

    /* (non-Javadoc)
     * @see org.datanucleus.store.rdbms.adapter.OracleAdapter#getSQLMethodClass(java.lang.String, java.lang.String, org.datanucleus.ClassLoaderResolver)
     */
    @Override
    public Class getSQLMethodClass(String className, String methodName, ClassLoaderResolver clr)
    {
        if (className == null)
        {
            if ("Oracle.sdo_elem_info_array".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.OracleSdoElemInfoArrayMethod.class;
            if ("Oracle.sdo_geometry".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.OracleSdoGeometryMethod.class;
            if ("Oracle.sdo_ordinate_array".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.OracleSdoOrdinateArrayMethod.class;
            if ("Oracle.sdo_point_type".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.OracleSdoPointTypeMethod.class;

            if ("Spatial.bboxTest".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBboxTestMethod3.class;
            if ("Spatial.dimension".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDimensionMethod2.class;
            if ("Spatial.srid".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSridMethod2.class;
            if ("Spatial.x".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialXMethod2.class;
            if ("Spatial.y".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialYMethod2.class;
            if ("Spatial.area".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAreaMethod2.class;
            if ("Spatial.length".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialLengthMethod3.class;
            if ("Spatial.distance".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDistanceMethod2.class;
            if ("Spatial.numPoints".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumPointsMethod2.class;
            if ("Spatial.numInteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumInteriorRingMethod3.class;
            if ("Spatial.numGeometries".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumGeometriesMethod2.class;
            if ("Spatial.asBinary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsBinaryMethod2.class;
            if ("Spatial.asText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsTextMethod2.class;
            if ("Spatial.geometryType".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryTypeMethod2.class;
            if ("Spatial.geomFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromTextMethod2.class;
            if ("Spatial.pointFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointFromTextMethod2.class;
            if ("Spatial.lineFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialLineFromTextMethod2.class;
            if ("Spatial.polyFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPolyFromTextMethod2.class;
            if ("Spatial.mLineFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMLineFromTextMethod2.class;
            if ("Spatial.mPointFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMPointFromTextMethod2.class;
            if ("Spatial.mPolyFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMPolyFromTextMethod2.class;
            if ("Spatial.geomCollFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomCollFromTextMethod2.class;
            if ("Spatial.envelope".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEnvelopeMethod2.class;
            if ("Spatial.boundary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBoundaryMethod2.class;
            if ("Spatial.convexHull".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialConvexHullMethod2.class;
            if ("Spatial.startPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialStartPointMethod2.class;
            if ("Spatial.endPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEndPointMethod2.class;
            if ("Spatial.centroid".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCentroidMethod2.class;
            if ("Spatial.pointOnSurface".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointOnSurfaceMethod2.class;
            if ("Spatial.exteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialExteriorRingMethod2.class;
            if ("Spatial.equals".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEqualsMethod2.class;
            if ("Spatial.disjoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDisjointMethod2.class;
            if ("Spatial.intersects".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectsMethod2.class;
            if ("Spatial.touches".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialTouchesMethod2.class;
            if ("Spatial.crosses".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCrossesMethod2.class;
            if ("Spatial.within".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialWithinMethod2.class;
            if ("Spatial.contains".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialContainsMethod2.class;
            if ("Spatial.overlaps".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialOverlapsMethod2.class;
            if ("Spatial.relate".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialRelateMethod2.class;
            if ("Spatial.isClosed".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsClosedMethod2.class;
            if ("Spatial.isEmpty".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsEmptyMethod2.class;
            if ("Spatial.isRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsRingMethod2.class;
            if ("Spatial.isSimple".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsSimpleMethod2.class;
            if ("Spatial.buffer".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBufferMethod2.class;
            if ("Spatial.difference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDifferenceMethod2.class;
            if ("Spatial.intersection".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectionMethod2.class;
            if ("Spatial.union".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialUnionMethod2.class;
            if ("Spatial.symDifference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSymDifferenceMethod2.class;
            if ("Spatial.interiorRingN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialInteriorRingNMethod2.class;
            if ("Spatial.pointN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointNMethod2.class;
            if ("Spatial.geometryN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryNMethod2.class;
            if ("Spatial.geomFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromWKBMethod2.class;
            if ("Spatial.geomCollFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomCollFromWKBMethod2.class;
            if ("Spatial.pointFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointFromWKBMethod2.class;
            if ("Spatial.mPointFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMPointFromWKBMethod2.class;
            if ("Spatial.lineFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialLineFromWKBMethod2.class;
            if ("Spatial.mLineFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMLineFromWKBMethod2.class;
            if ("Spatial.polyFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPolyFromWKBMethod2.class;
            if ("Spatial.mPolyFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialMPolyFromWKBMethod2.class;
        }
        else
        {
            Class cls = null;
            try
            {
                cls = clr.classForName(className);
            }
            catch (ClassNotResolvedException cnre) {}

            if ("com.vividsolutions.jts.geom.Point".equals(className) || (cls != null && com.vividsolutions.jts.geom.Point.class.isAssignableFrom(cls)))
            {
                if ("getX".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialXMethod2.class;
                if ("getY".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialYMethod2.class;
            }
            if ("com.vividsolutions.jts.geom.Geometry".equals(className) || (cls != null && com.vividsolutions.jts.geom.Geometry.class.isAssignableFrom(cls)))
            {
                if ("getNumPoints".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumPointsMethod2.class;
                if ("getArea".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAreaMethod2.class;
                if ("contains".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialContainsMethod2.class;
                if ("getEnvelope".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEnvelopeMethod2.class;
                if ("getDimension".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDimensionMethod2.class;
                if ("getLength".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialLengthMethod3.class;
                if ("getBoundary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBoundaryMethod2.class;
                if ("getSRID".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSridMethod2.class;
                if ("isSimple".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsSimpleMethod2.class;
                if ("isEmpty".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsEmptyMethod2.class;
                if ("overlaps".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialOverlapsMethod2.class;
                if ("touches".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialTouchesMethod2.class;
                if ("crosses".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCrossesMethod2.class;
                if ("within".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialWithinMethod2.class;
                if ("intersects".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectsMethod2.class;
                if ("intersection".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectionMethod2.class;
                if ("equals".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEqualsMethod2.class;
                if ("disjoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDisjointMethod2.class;
                if ("relate".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialRelateMethod2.class;
                if ("difference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDifferenceMethod2.class;
                if ("symDifference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSymDifferenceMethod2.class;
                if ("getCentroid".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCentroidMethod2.class;
                if ("toText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsTextMethod2.class;
                if ("union".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialUnionMethod2.class;
                if ("getGeometryType".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryTypeMethod2.class;
                if ("distance".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDistanceMethod2.class;
                if ("buffer".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBufferMethod2.class;
                if ("convexHull".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialConvexHullMethod2.class;
            }
            if ("com.vividsolutions.jts.geom.LineString".equals(className) || (cls != null && com.vividsolutions.jts.geom.LineString.class.isAssignableFrom(cls)))
            {
                if ("isRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsRingMethod2.class;
                if ("isClosed".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsClosedMethod2.class;
                if ("getStartPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialStartPointMethod2.class;
                if ("getEndPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEndPointMethod2.class;
                if ("getPointN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointNMethod2.class;
            }
            if ("com.vividsolutions.jts.geom.Polygon".equals(className) || (cls != null && com.vividsolutions.jts.geom.Polygon.class.isAssignableFrom(cls)))
            {
                if ("getExteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialExteriorRingMethod2.class;
                if ("getInteriorRingN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialInteriorRingNMethod2.class;
                if ("getNumInteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumInteriorRingMethod3.class;
            }
            if ("com.vividsolutions.jts.geom.GeometryCollection".equals(className) || (cls != null && com.vividsolutions.jts.geom.GeometryCollection.class.isAssignableFrom(cls)))
            {
                if ("getNumGeometries".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumGeometriesMethod2.class;
                if ("getGeometryN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryNMethod2.class;
            }
            if ("org.postgis.Point".equals(className) || (cls != null && org.postgis.Point.class.isAssignableFrom(cls)))
            {
                if ("getX".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialXMethod2.class;
                if ("getY".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialYMethod2.class;
            }
            if ("org.postgis.Geometry".equals(className) || (cls != null && org.postgis.Geometry.class.isAssignableFrom(cls)))
            {
                if ("numPoints".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumPointsMethod2.class;
                if ("getDimension".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDimensionMethod2.class;
                if ("getSrid".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSridMethod2.class;
                if ("equals".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEqualsMethod2.class;
            }
        }

        return super.getSQLMethodClass(className, methodName, clr);
    }

}