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
    static
    {
        Localiser.registerBundle("org.datanucleus.store.rdbms.adapter.Localisation_PostGIS", PostGISAdapter.class.getClassLoader());
    }

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
     * @see org.datanucleus.store.rdbms.adapter.PostgreSQLAdapter#loadDatastoreMappings(org.datanucleus.plugin.PluginManager, org.datanucleus.ClassLoaderResolver)
     */
    @Override
    protected void loadDatastoreMappings(PluginManager mgr, ClassLoaderResolver clr)
    {
        // jts2postgis
        registerDatastoreMapping(com.vividsolutions.jts.geom.Geometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.GeometryRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.GeometryCollection.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.GeometryCollectionRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.LinearRing.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.LinearRingRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.LineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.LineStringRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.MultiLineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.MultiLineStringRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.MultiPolygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.MultiPolygonRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.MultiPoint.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.MultiPointRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.Point.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.PointRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(com.vividsolutions.jts.geom.Polygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.jts2postgis.PolygonRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);

        // pg2postgis
        registerDatastoreMapping(org.postgis.Geometry.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.GeometryRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(org.postgis.GeometryCollection.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.GeometryCollectionRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(org.postgis.LinearRing.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.LinearRingRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(org.postgis.LineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.LineStringRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(org.postgis.MultiLineString.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.MultiLineStringRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(org.postgis.MultiPolygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.MultiPolygonRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(org.postgis.MultiPoint.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.MultiPointRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(org.postgis.Point.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.GeometryRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(org.postgis.Polygon.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.GeometryRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(org.postgis.PGbox2d.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.GeometryRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);
        registerDatastoreMapping(org.postgis.PGbox3d.class.getName(), org.datanucleus.store.types.geospatial.rdbms.mapping.pg2postgis.GeometryRDBMSMapping.class, 
            JDBCType.OTHER, "geometry", true);

        super.loadDatastoreMappings(mgr, clr);
    }

    public String getAddPrimaryKeyStatement(PrimaryKey pk, IdentifierFactory factory)
    {
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

        for (int i = 0; i < columns.length; ++i)
        {
            String stmt = getAddColumnStatement(table, columns[i]);
            createStatements.append(stmt).append(";").append(getContinuationString());
        }

        return createStatements.toString();
    }

    public String getRetrieveCrsWktStatement(Table table, int srid)
    {
        return "SELECT srtext FROM #schema . spatial_ref_sys WHERE srid = #srid".replace("#schema", table.getSchemaName()).replace("#srid",
            "" + srid);
    }

    public String getRetrieveCrsNameStatement(Table table, int srid)
    {
        return "SELECT auth_name || ':' || auth_srid FROM #schema . spatial_ref_sys WHERE srid = #srid".replace("#schema",
            table.getSchemaName()).replace("#srid", "" + srid);
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
                NucleusLogger.DATASTORE.warn(
                    Localiser.msg("RDBMS.Adapter.InvalidExtensionValue", SRID_EXTENSION_KEY, extensionValue), nfe);
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
                NucleusLogger.DATASTORE.warn(
                    Localiser.msg("RDBMS.Adapter.InvalidExtensionValue", DIMENSION_EXTENSION_KEY, extensionValue), nfe);
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
                NucleusLogger.DATASTORE.warn(
                    Localiser.msg("RDBMS.Adapter.InvalidExtensionValue", HAS_MEASURE_EXTENSION_KEY, extensionValue), nfe);
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
}