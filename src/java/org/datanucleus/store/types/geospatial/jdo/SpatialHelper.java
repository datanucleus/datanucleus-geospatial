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
package org.datanucleus.store.types.geospatial.jdo;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOCanRetryException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaDataManager;
import org.datanucleus.metadata.MetaDataUtils;
import org.datanucleus.store.types.geospatial.rdbms.adapter.SpatialRDBMSAdapter;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.store.rdbms.table.Table;
import org.datanucleus.util.NucleusLogger;

/**
 * <p>
 * Helper class to search and access Spatial MetaData. This class wraps some of the spatial functions that
 * datanucleus-spatial offers via JDOQL in "real" Java methods and also adds extended functionality, like
 * reading values from JDO-Metadata and getting CRS (Coordinate Reference System) information from the
 * database.
 * </p>
 * <p>
 * The datatypes and terminology used in this class is (like most of datanucleus-spatial) heavily based on
 * OGC's Simple Feature specification. See <a
 * href="http://www.opengeospatial.org/standards/sfa">http://www.opengeospatial.org/standards/sfa</a> for
 * details.
 * </p>
 */
public class SpatialHelper
{
    protected JDOPersistenceManagerFactory pmf;

    protected RDBMSStoreManager storeMgr;

    /**
     * Creates a new <code>SpatialHelper</code> instance for the given PMF.
     * @param pmf The PMF, can't be <code>null</code> or closed.
     */
    public SpatialHelper(JDOPersistenceManagerFactory pmf)
    {
        if (pmf == null || pmf.isClosed())
        {
            throw new IllegalArgumentException("pmf is null or closed. pmf = " + pmf);
        }
        this.pmf = pmf;
        this.storeMgr = (RDBMSStoreManager) pmf.getNucleusContext().getStoreManager();
    }

    /**
     * Returns the srid from JDO-Metadata for the given geometry field. Will return <code>null</code>, if the
     * user has not specified a value in his metadata.
     * @param pc The PersistenceCapapable class
     * @param fieldName Name of the geometry field
     * @return The srid or <code>null</code>.
     * @see SpatialRDBMSAdapter#SRID_EXTENSION_KEY
     */
    public Integer getSridFromJdoMetadata(Class pc, String fieldName)
    {
        return getIntegerFromJdoMetadata(pc, fieldName, SpatialRDBMSAdapter.SRID_EXTENSION_KEY);
    }

    /**
     * Returns the dimension from JDO-Metadata for the given geometry field. Will return <code>null</code>, if
     * the user has not specified a value in his metadata.
     * @param pc The PersistenceCapapable class
     * @param fieldName Name of the geometry field
     * @return The dimension or <code>null</code>.
     * @see SpatialRDBMSAdapter#DIMENSION_EXTENSION_KEY
     */
    public Integer getDimensionFromJdoMetadata(Class pc, String fieldName)
    {
        return getIntegerFromJdoMetadata(pc, fieldName, SpatialRDBMSAdapter.DIMENSION_EXTENSION_KEY);
    }

    /**
     * Returns the srid from datastore metadata for the given geometry field. Will return <code>null</code>,
     * if the datastore doesn't support such an operation (e.g. MySQL) or if there is no metadata in the
     * datastore available for the given class and field name.
     * @param pc The <code>PersistenceCapable</code> class
     * @param fieldName Name of the geometry field
     * @param pm <code>PersistenceManager</code> instance that should be used to access the datastore
     * @return The srid or <code>null</code>.
     */
    public Integer getSridFromDatastoreMetadata(final Class pc, final String fieldName, final PersistenceManager pm)
    {
        checkValid(pc, fieldName);

        Integer srid = (Integer) new QueryExecutor(pm.currentTransaction())
        {
            Query getQuery()
            {
                Query q = pm.newQuery(pc);
                q.setResult("Spatial.sridFromMetadata( " + fieldName + " )");
                q.setUnique(true);
                q.setRange(0, 1);
                return q;
            }
        }.execute();

        return srid;
    }

    /**
     * <p>
     * Returns the (estimated) spatial extent, also called <i>bounding box</i> from datastore metadata for the
     * given geometry field. Will return <code>null</code>, if the datastore doesn't support such an operation
     * (e.g. MySQL) or if there is no metadata in the datastore available for the given class and field name.
     * @param pc The <code>PersistenceCapable</code> class
     * @param fieldName Name of the geometry field
     * @param pm <code>PersistenceManager</code> instance that should be used to access the datastore
     * @return The bbox or <code>null</code>.
     */
    public Rectangle2D estimateBoundsFromDatastoreMetadata(final Class pc, final String fieldName, final PersistenceManager pm)
    {
        checkValid(pc, fieldName);

        Rectangle2D bounds = null;
        try
        {
            bounds = (Rectangle2D) new QueryExecutor(pm.currentTransaction())
            {
                Query getQuery()
                {
                    Query q = pm.newQuery(pc);
                    q.setResult("Spatial.rectangle2DFromMetadata( " + fieldName + " )");
                    q.setUnique(true);
                    q.setRange(0, 1);
                    return q;
                }
            }.execute();
        }
        catch (JDOCanRetryException e)
        {
            // TODO: I18n
            NucleusLogger.QUERY.info("estimateBoundsFromDatastoreMetadata() failed", e);
        }

        return bounds;
    }

    public Rectangle2D calculateBoundsInDatastore(final Class pc, final String fieldName, final PersistenceManager pm)
    {
        checkValid(pc, fieldName);

        Table table = getTable(pc);
        Column column = getColumn(pc, fieldName);
        final String stmt = getAdapter().getCalculateBoundsStatement(table, column);

        if (stmt == null)
            return null;

        Rectangle2D bounds = (Rectangle2D) new QueryExecutor(pm.currentTransaction())
        {
            Query getQuery()
            {
                Query q = pm.newQuery(Query.SQL, stmt);
                q.setResultClass(String.class);
                q.setUnique(true);
                return q;
            }
        }.execute();

        return bounds;
    }

    /**
     * Returns the description of the Coordinate Reference System (CRS) for the given srid in WKT (Well-Known
     * Text). Will return <code>null</code>, if the datastore doesn't support such an operation (e.g. MySQL)
     * or if there is no metadata in the datastore available for the given class and srid.
     * @param pc The <code>PersistenceCapable</code> class
     * @param srid The srid
     * @param pm <code>PersistenceManager</code> instance that should be used to access the datastore
     * @return Description of the CRS in WKT or <code>null</code>.
     */
    public String getCrsWktForSrid(Class pc, int srid, final PersistenceManager pm)
    {
        Table table = getTable(pc);
        final String stmt = getAdapter().getRetrieveCrsWktStatement(table, srid);

        if (stmt == null)
            return null;

        String crsWKT = (String) new QueryExecutor(pm.currentTransaction())
        {
            Query getQuery()
            {
                Query q = pm.newQuery(Query.SQL, stmt);
                q.setResultClass(String.class);
                q.setUnique(true);
                return q;
            }
        }.execute();

        return crsWKT;
    }

    /**
     * Returns the name of the Coordinate Reference System (CRS) for the given srid. Will return
     * <code>null</code>, if the datastore doesn't support such an operation (e.g. MySQL) or if there is no
     * metadata in the datastore available for the given class and srid.
     * @param pc The <code>PersistenceCapable</code> class
     * @param srid The srid
     * @param pm <code>PersistenceManager</code> instance that should be used to access the datastore
     * @return Name the CRS or <code>null</code>.
     */
    public String getCrsNameForSrid(Class pc, int srid, final PersistenceManager pm)
    {
        Table table = getTable(pc);
        final String stmt = getAdapter().getRetrieveCrsNameStatement(table, srid);

        if (stmt == null)
            return null;

        String crsName = (String) new QueryExecutor(pm.currentTransaction())
        {
            Query getQuery()
            {
                Query q = pm.newQuery(Query.SQL, stmt);
                q.setResultClass(String.class);
                q.setUnique(true);
                return q;
            }
        }.execute();

        return crsName;
    }

    protected Integer getIntegerFromJdoMetadata(Class pc, String fieldName, String extensionKey)
    {
        String value = getValueFromJdoMetadata(pc, fieldName, extensionKey);
        Integer integer = null;
        try
        {
            integer = Integer.valueOf(value);
        }
        catch (NumberFormatException e)
        {
        }

        return integer;
    }

    protected String getValueFromJdoMetadata(Class pc, String memberName, String extensionKey)
    {
        checkValid(pc, memberName);

        AbstractClassMetaData cmd = storeMgr.getMetaDataManager().getMetaDataForClass(pc,
            storeMgr.getNucleusContext().getClassLoaderResolver(null));
        AbstractMemberMetaData fmd = (cmd != null ? cmd.getMetaDataForMember(memberName) : null);
        String value = MetaDataUtils.getValueForExtensionRecursively(fmd, extensionKey);
        if (value == null || value.trim().equals(""))
        {
            return null;
        }
        else
        {
            return value;
        }
    }

    /**
     * Return all field names of the given class that are backed by a geometry column in the database.
     * @param pc The <code>PersistenceCapable</code> class
     * @return Array of names, may be empty, but never <code>null</code>.
     */
    public String[] getGeometryColumnBackedFields(Class pc)
    {
        List fieldNames = new ArrayList();

        AbstractMemberMetaData[] fieldMetaData = getMetaDataManager().getMetaDataForClass(pc, null).getManagedMembers();
        for (int i = 0; i < fieldMetaData.length; i++)
        {
            Column c = getColumn(pc, fieldMetaData[i]);
            if (isGeometryColumnBackedField(c))
            {
                fieldNames.add(fieldMetaData[i].getName());
            }
        }

        return (String[]) fieldNames.toArray(new String[0]);
    }

    /**
     * Checks whether the given field name for the given class is backed by a geometry column in the database.
     * @param pc The <code>PersistenceCapable</code> class
     * @param fieldName Name of the field
     * @return <code>true</code>, if the field is backed by a geometry column, <code>false</code> otherwise.
     */
    public boolean isGeometryColumnBackedField(Class pc, String fieldName)
    {
        Column c = getColumn(pc, fieldName);
        return isGeometryColumnBackedField(c);
    }

    protected boolean isGeometryColumnBackedField(Column c)
    {
        return getAdapter().isGeometryColumn(c);
    }

    protected Table getTable(Class pc)
    {
        ClassLoaderResolver clr = pmf.getNucleusContext().getClassLoaderResolver(getClass().getClassLoader());
        return (Table) storeMgr.getDatastoreClass(pc.getName(), clr);
    }

    protected Column getColumn(Class pc, String fieldName)
    {
        ClassLoaderResolver clr = pmf.getNucleusContext().getClassLoaderResolver(getClass().getClassLoader());
        return (Column) storeMgr.getDatastoreClass(pc.getName(), clr).getMemberMapping(fieldName).getDatastoreMappings()[0].getColumn();
    }

    protected Column getColumn(Class pc, AbstractMemberMetaData mmd)
    {
        ClassLoaderResolver clr = pmf.getNucleusContext().getClassLoaderResolver(getClass().getClassLoader());
        return (Column) storeMgr.getDatastoreClass(pc.getName(), clr).getMemberMapping(mmd).getDatastoreMappings()[0].getColumn();
    }

    protected SpatialRDBMSAdapter getAdapter() throws ClassCastException
    {
        return (SpatialRDBMSAdapter) storeMgr.getDatastoreAdapter();
    }

    protected MetaDataManager getMetaDataManager()
    {
        return pmf.getNucleusContext().getMetaDataManager();
    }

    protected void checkValid(Class pc, String fieldName) throws IllegalArgumentException, NullPointerException
    {
        // TODO: Better exceptions??
        // TODO: I18n
        if (pc == null || fieldName == null)
        {
            throw new NullPointerException("pc = " + pc + " / fieldName = " + fieldName);
        }
        if (!isGeometryColumnBackedField(pc, fieldName))
        {
            throw new IllegalArgumentException("'" + pc.getName() + "#" + fieldName + "' is not a geometry column backed field.");
        }
    }

    protected Object readFirstValueForField(final Class pc, final String fieldName, final PersistenceManager pm)
    {
        return new QueryExecutor(pm.currentTransaction())
        {
            Query getQuery()
            {
                Query q = pm.newQuery(pc);
                q.setResult(fieldName);
                q.setUnique(true);
                q.setRange(0, 1);
                return q;
            }
        }.execute();
    }

    /**
     * Abstract helper class to execute queries. Applies the <i>Template Method</i> pattern. TODO Doesn't
     * close the query after use.
     */
    protected abstract class QueryExecutor
    {
        private Transaction tx;

        QueryExecutor(Transaction tx)
        {
            this.tx = tx;
        }

        Object execute()
        {
            Object result = null;
            boolean isActive = tx.isActive();
            try
            {
                if (!isActive)
                    tx.begin();
                result = getQuery().execute();
            }
            finally
            {
                if (!isActive)
                    tx.rollback();
            }

            return result;
        }

        abstract Query getQuery();
    }
}