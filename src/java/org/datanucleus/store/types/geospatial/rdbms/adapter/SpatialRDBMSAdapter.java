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

import org.datanucleus.store.rdbms.adapter.DatastoreAdapter;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.store.rdbms.table.Table;

/**
 * Adapter for spatially enabled databases.
 */
public interface SpatialRDBMSAdapter extends DatastoreAdapter
{
    /** Key name for the srid extension. **/
    String SRID_EXTENSION_KEY = "spatial-srid";

    /** Key name for the dimension extension. **/
    String DIMENSION_EXTENSION_KEY = "spatial-dimension";

    /**
     * Checks whether the given column is geometry backed by the datastore.
     * @param column Column to check
     * @return <code>true</code> if the given column is geometry backed, <code>false</code> otherwise
     */
    boolean isGeometryColumn(Column column);

    /**
     * Returns the appropriate SQL statement to retrieve description of the Coordinate Reference System (CRS)
     * with the given srid.
     * @param table A table
     * @param srid The srid
     * @return SQL statement, <code>null</code> if not available for the datastore
     */
    String getRetrieveCrsWktStatement(Table table, int srid);

    /**
     * Returns the appropriate SQL statement to retrieve the name of the Coordinate Reference System (CRS)
     * with the given srid.
     * @param table A table
     * @param srid The srid
     * @return SQL statement, <code>null</code> if not available for the datastore
     */
    String getRetrieveCrsNameStatement(Table table, int srid);

    /**
     * Returns the appropriate SQL statement that calculates the bounds of all geometries in the given column.
     * @param table The table
     * @param column The column
     * @return SQL statement, <code>null</code> if not available for the datastore
     */
    String getCalculateBoundsStatement(Table table, Column column);
}