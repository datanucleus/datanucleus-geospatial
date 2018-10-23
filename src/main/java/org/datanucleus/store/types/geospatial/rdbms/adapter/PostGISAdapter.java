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
import java.util.Properties;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.exceptions.ClassNotResolvedException;
import org.datanucleus.metadata.MetaDataUtils;
import org.datanucleus.plugin.PluginManager;
import org.datanucleus.store.connection.ManagedConnection;
import org.datanucleus.store.rdbms.adapter.PostgreSQLAdapter;
import org.datanucleus.store.rdbms.identifier.IdentifierFactory;
import org.datanucleus.store.rdbms.key.PrimaryKey;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.store.rdbms.table.Table;
import org.datanucleus.store.rdbms.table.TableImpl;
import org.datanucleus.store.schema.StoreSchemaHandler;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.Localiser;

/**
 * Provides methods for adapting SQL language elements for the PostGIS extension.
 */
public class PostGISAdapter extends PostgreSQLAdapter implements SpatialRDBMSAdapter
{
    /** Key name for the hasMeasure extension. **/
    public static final String HAS_MEASURE_EXTENSION_KEY = "postgis-hasMeasure";

    public PostGISAdapter(DatabaseMetaData metadata)
    {
        super(metadata);
        supportedOptions.remove(PRIMARYKEY_IN_CREATE_STATEMENTS);
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
        SQLTypeInfo sqlType = PostGISTypeInfo.TYPEINFO_PROTOTYPE;
        addSQLTypeForJDBCType(handler, mconn, (short) Types.OTHER, sqlType, true);
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.rdbms.adapter.PostgreSQLAdapter#loadColumnMappings(org.datanucleus.plugin.PluginManager, org.datanucleus.ClassLoaderResolver)
     */
    @Override
    protected void loadColumnMappings(PluginManager mgr, ClassLoaderResolver clr)
    {
        try
        {
            Class cls = clr.classForName("com.vividsolutions.jts.geom.Geometry");
            if (cls != null)
            {
                // jts2postgis
                registerColumnMapping(com.vividsolutions.jts.geom.Geometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.GeometryColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(com.vividsolutions.jts.geom.GeometryCollection.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.GeometryCollectionColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(com.vividsolutions.jts.geom.LinearRing.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.LinearRingColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(com.vividsolutions.jts.geom.LineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.LineStringColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(com.vividsolutions.jts.geom.MultiLineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.MultiLineStringColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(com.vividsolutions.jts.geom.MultiPolygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.MultiPolygonColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(com.vividsolutions.jts.geom.MultiPoint.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.MultiPointColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(com.vividsolutions.jts.geom.Point.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.PointColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(com.vividsolutions.jts.geom.Polygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.PolygonColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
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
                // pg2postgis
                registerColumnMapping(org.postgis.Geometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.GeometryColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(org.postgis.GeometryCollection.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.GeometryCollectionColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(org.postgis.LinearRing.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.LinearRingColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(org.postgis.LineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.LineStringColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(org.postgis.MultiLineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.MultiLineStringColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(org.postgis.MultiPolygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.MultiPolygonColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(org.postgis.MultiPoint.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.MultiPointColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(org.postgis.Point.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.PointColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(org.postgis.Polygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.PolygonColumnMapping.class, 
                    JDBCType.OTHER, "geometry", true);
                registerColumnMapping(org.postgis.PGbox2d.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.PGbox2dColumnMapping.class, 
                    JDBCType.OTHER, "box2d", true);
                registerColumnMapping(org.postgis.PGbox3d.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.PGbox3dColumnMapping.class, 
                    JDBCType.OTHER, "box3d", true);
            }
        }
        catch (Throwable thr)
        {
            NucleusLogger.DATASTORE.warn("Not loading RDBMS support for PostGIS types since not present");
        }

        super.loadColumnMappings(mgr, clr);
    }

    public String getAddPrimaryKeyStatement(PrimaryKey pk, IdentifierFactory factory)
    {
        // TODO Document why we override this. See above PRIMARYKEY_IN_CREATE_STATEMENTS
        return "ALTER TABLE " + pk.getTable().toString() + " ADD " + pk;
    }

    public String getAddColumnStatement(Table table, Column column)
    {
        if (isGeometryColumn(column))
        {
            return getAddGeometryColumnStatement(table, column);
        }
        return super.getAddColumnStatement(table, column);
    }

    public String getCreateTableStatement(TableImpl table, Column[] columns, Properties props, IdentifierFactory factory)
    {
        boolean hasGeometryColumn = false;
        for (int i = 0; i < columns.length; ++i)
        {
            if (isGeometryColumn(columns[i]))
            {
                hasGeometryColumn = true;
                break;
            }
        }
        if (!hasGeometryColumn)
        {
            return super.getCreateTableStatement(table, columns, null, factory);
        }

        StringBuilder createStatements = new StringBuilder();

        // Create empty table first, then add each column individually,
        // because the geometry columns have to be added via SQL function.
        createStatements.append("CREATE TABLE ").append(table.toString()).append(" ();").append(getContinuationString());

        for (Column col : columns)
        {
            String stmt = getAddColumnStatement(table, col);
            createStatements.append(stmt).append(";").append(getContinuationString());
        }

        return createStatements.toString();
    }

    public String getRetrieveCrsWktStatement(Table table, int srid)
    {
        return "SELECT srtext FROM #schema . spatial_ref_sys WHERE srid = #srid".replace("#schema", table.getSchemaName()).replace("#srid","" + srid);
    }

    public String getRetrieveCrsNameStatement(Table table, int srid)
    {
        return "SELECT auth_name || ':' || auth_srid FROM #schema . spatial_ref_sys WHERE srid = #srid".replace("#schema", table.getSchemaName()).replace("#srid", "" + srid);
    }

    public String getCalculateBoundsStatement(Table table, Column column)
    {
        return "SELECT " + "min(xmin(box2d(#column))), " + "min(ymin(box2d(#column))), " + "max(xmax(box2d(#column))), " + "max(ymax(box2d(#column)))" + "FROM #schema . #table"
                .replace("#schema", table.getSchemaName()).replace("#table", "" + table.getIdentifier().getName())
                .replace("#column", "" + column.getIdentifier().getName());
    }

    private String getAddGeometryColumnStatement(Table table, Column column)
    {
        int srid = -1;
        byte dimension = 2;
        boolean hasMeasure = false;

        String extensionValue = MetaDataUtils.getValueForExtensionRecursively(column.getColumnMetaData(), SRID_EXTENSION_KEY);
        if (extensionValue != null)
        {
            try
            {
                srid = Integer.parseInt(extensionValue);
            }
            catch (NumberFormatException nfe)
            {
                NucleusLogger.DATASTORE.warn(Localiser.msg("044213", SRID_EXTENSION_KEY, extensionValue), nfe);
            }
        }

        extensionValue = MetaDataUtils.getValueForExtensionRecursively(column.getColumnMetaData(), DIMENSION_EXTENSION_KEY);
        if (extensionValue != null)
        {
            try
            {
                dimension = Byte.parseByte(extensionValue);
            }
            catch (NumberFormatException nfe)
            {
                NucleusLogger.DATASTORE.warn(Localiser.msg("044213", DIMENSION_EXTENSION_KEY, extensionValue), nfe);
            }
        }

        extensionValue = MetaDataUtils.getValueForExtensionRecursively(column.getColumnMetaData(), HAS_MEASURE_EXTENSION_KEY);
        if (extensionValue != null)
        {
            try
            {
                hasMeasure = Boolean.parseBoolean(extensionValue);
            }
            catch (NumberFormatException nfe)
            {
                NucleusLogger.DATASTORE.warn(Localiser.msg("044213", HAS_MEASURE_EXTENSION_KEY, extensionValue), nfe);
            }
        }

        if (hasMeasure)
        {
            dimension++;
        }

        return "SELECT AddGeometryColumn( '#schema', '#table', '#column', #srid, '#type', #dimension )"
                .replace("#schema", table.getSchemaName() == null ? "" : table.getSchemaName())
                .replace("#table", table.getIdentifier().getName())
                .replace("#column", column.getIdentifier().getName()).replace("#srid", "" + srid)
                .replace("#type", column.getTypeInfo().getLocalTypeName().concat(hasMeasure ? "M" : ""))
                .replace("#dimension", "" + dimension);
    }

    public boolean isGeometryColumn(Column column)
    {
        SQLTypeInfo typeInfo = column.getTypeInfo();
        return (typeInfo == null) ? false : typeInfo.getTypeName().equalsIgnoreCase("geometry");
    }

    /* (non-Javadoc)
     * @see org.datanucleus.store.rdbms.adapter.PostgreSQLAdapter#getSQLMethodClass(java.lang.String, java.lang.String, org.datanucleus.ClassLoaderResolver)
     */
    @Override
    public Class getSQLMethodClass(String className, String methodName, ClassLoaderResolver clr)
    {
        if (className == null)
        {
            if ("Spatial.envelope".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEnvelopeMethod3.class;
            if ("Spatial.dimension".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDimensionMethod3.class;
            if ("Spatial.boundary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBoundaryMethod3.class;
            if ("Spatial.srid".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSridMethod3.class;
            if ("Spatial.isSimple".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsSimpleMethod3.class;
            if ("Spatial.isEmpty".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsEmptyMethod3.class;
            if ("Spatial.asBinary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsBinaryMethod3.class;
            if ("Spatial.asText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsTextMethod3.class;
            if ("Spatial.geometryType".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryTypeMethod3.class;

            if ("Spatial.contains".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialContainsMethod3.class;
            if ("Spatial.overlaps".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialOverlapsMethod3.class;
            if ("Spatial.touches".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialTouchesMethod3.class;
            if ("Spatial.crosses".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCrossesMethod3.class;
            if ("Spatial.within".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialWithinMethod3.class;
            if ("Spatial.intersects".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectsMethod3.class;
            if ("Spatial.equals".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEqualsMethod3.class;
            if ("Spatial.disjoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDisjointMethod3.class;
            if ("Spatial.relate".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialRelateMethod3.class;
            if ("Spatial.distance".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDistanceMethod3.class;
            if ("Spatial.intersection".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectionMethod3.class;
            if ("Spatial.buffer".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBufferMethod3.class;
            if ("Spatial.convexHull".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialConvexHullMethod3.class;
            if ("Spatial.symDifference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSymDifferenceMethod3.class;
            if ("Spatial.difference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDifferenceMethod3.class;
            if ("Spatial.union".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialUnionMethod3.class;

            if ("Spatial.length".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialLengthMethod3.class;

            if ("Spatial.numPoints".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumPointsMethod3.class;

            if ("Spatial.centroid".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCentroidMethod3.class;
            if ("Spatial.area".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAreaMethod3.class;
            if ("Spatial.pointOnSurface".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointOnSurfaceMethod3.class;

            if ("Spatial.numGeometries".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumGeometriesMethod3.class;
            if ("Spatial.geometryN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryNMethod3.class;

            if ("Spatial.x".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialXMethod3.class;
            if ("Spatial.y".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialYMethod3.class;

            if ("Spatial.isRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsRingMethod3.class;
            if ("Spatial.isClosed".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsClosedMethod3.class;
            if ("Spatial.startPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialStartPointMethod3.class;
            if ("Spatial.endPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEndPointMethod3.class;
            if ("Spatial.pointN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointNMethod3.class;

            if ("Spatial.exteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialExteriorRingMethod3.class;
            if ("Spatial.numInteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumInteriorRingMethod3.class;
            if ("Spatial.interiorRingN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialInteriorRingNMethod3.class;

            if ("Spatial.transform".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomTransformMethod.class;

            if ("Spatial.bboxTest".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBboxTestMethod.class;

            // Purely static methods
            if ("PostGIS.bboxBelow".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISBboxBelowMethod.class;
            if ("PostGIS.bboxAbove".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISBboxAboveMethod.class;
            if ("PostGIS.bboxLeft".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISBboxLeftMethod.class;
            if ("PostGIS.bboxRight".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISBboxRightMethod.class;
            if ("PostGIS.bboxOverlapsBelow".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISBboxOverlapsBelowMethod.class;
            if ("PostGIS.bboxOverlapsAbove".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISBboxOverlapsAboveMethod.class;
            if ("PostGIS.bboxOverlapsLeft".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISBboxOverlapsLeftMethod.class;
            if ("PostGIS.bboxOverlapsRight".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISBboxOverlapsRightMethod.class;
            if ("PostGIS.bboxContains".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISBboxContainsMethod.class;
            if ("PostGIS.bboxWithin".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISBboxWithinMethod.class;
            if ("PostGIS.sameAs".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.PostGISSameAsMethod.class;

            if ("Spatial.geogFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeographyFromTextMethod.class;
            if ("Spatial.geomFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromTextMethod3.class;
            if ("Spatial.pointFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromTextMethod3.class;
            if ("Spatial.lineFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromTextMethod3.class;
            if ("Spatial.polyFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromTextMethod3.class;
            if ("Spatial.mLineFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromTextMethod3.class;
            if ("Spatial.mPointFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromTextMethod3.class;
            if ("Spatial.mPolyFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromTextMethod3.class;
            if ("Spatial.geomCollFromText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromTextMethod3.class;

            if ("Spatial.geogFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeographyFromWKBMethod.class;
            if ("Spatial.geomFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromWKBMethod3.class;
            if ("Spatial.geomCollFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromWKBMethod3.class;                    
            if ("Spatial.pointFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromWKBMethod3.class;
            if ("Spatial.mPointFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromWKBMethod3.class;
            if ("Spatial.lineFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromWKBMethod3.class;
            if ("Spatial.mLineFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromWKBMethod3.class;
            if ("Spatial.polyFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromWKBMethod3.class;
            if ("Spatial.mPolyFromWKB".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeomFromWKBMethod3.class;
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
            boolean isMultiLineString = false;
            boolean isLinearRing = false;
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

            if (isGeometry)
            {
                if ("getEnvelope".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEnvelopeMethod3.class;
                if ("getDimension".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDimensionMethod3.class;
                if ("getBoundary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBoundaryMethod3.class;
                if ("getSRID".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSridMethod3.class;
                if ("isSimple".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsSimpleMethod3.class;
                if ("isEmpty".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsEmptyMethod3.class;
                if ("toText".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsTextMethod3.class;
                if ("toBinary".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAsBinaryMethod3.class;
                if ("getGeometryType".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryTypeMethod3.class;

                if ("contains".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialContainsMethod3.class;
                if ("overlaps".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialOverlapsMethod3.class;
                if ("touches".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialTouchesMethod3.class;
                if ("crosses".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCrossesMethod3.class;
                if ("within".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialWithinMethod3.class;
                if ("intersects".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectsMethod3.class;
                if ("equals".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEqualsMethod3.class;
                if ("disjoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDisjointMethod3.class;
                if ("relate".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialRelateMethod3.class;
                if ("distance".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDistanceMethod3.class;
                if ("intersection".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIntersectionMethod3.class;
                if ("buffer".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBufferMethod3.class;
                if ("convexHull".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialConvexHullMethod3.class;
                if ("symDifference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialSymDifferenceMethod3.class;
                if ("difference".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialDifferenceMethod3.class;
                if ("union".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialUnionMethod3.class;
                if ("bboxTest".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialBboxTestMethod.class;

                // Curve
                if ("getLength".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialLengthMethod3.class;

                // LineString
                if ("getNumPoints".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumPointsMethod3.class;

                // Surface
                if ("getCentroid".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialCentroidMethod3.class;
                if ("getArea".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialAreaMethod3.class;
                if ("getPointOnSurface".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointOnSurfaceMethod3.class;

                // GeomCollection
                if ("getGeometryN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialGeometryNMethod3.class;
                if ("getNumGeometries".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumGeometriesMethod3.class;

                if (isPoint)
                {
                    if ("getX".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialXMethod3.class;
                    if ("getY".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialYMethod3.class;
                }
                if (isLineString)
                {
                    if ("isRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsRingMethod3.class;
                    if ("isClosed".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsClosedMethod3.class;
                    if ("getStartPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialStartPointMethod3.class;
                    if ("getEndPoint".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialEndPointMethod3.class;
                    if ("getPointN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialPointNMethod3.class;
                }
                if (isMultiLineString || isLinearRing)
                {
                    if ("isClosed".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialIsClosedMethod3.class;
                }
                if (isPolygon)
                {
                    if ("getExteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialExteriorRingMethod3.class;
                    if ("getInteriorRingN".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialInteriorRingNMethod3.class;
                    if ("getNumInteriorRing".equals(methodName)) return org.datanucleus.store.types.geospatial.rdbms.sql.method.SpatialNumInteriorRingMethod3.class;
                }
            }
        }

        return super.getSQLMethodClass(className, methodName, clr);
    }
}