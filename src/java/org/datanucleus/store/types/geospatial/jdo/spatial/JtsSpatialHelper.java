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
package org.datanucleus.store.types.geospatial.jdo.spatial;

import javax.jdo.PersistenceManager;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Helper class to read Spatial MetaData from the datastore, specialized for the geometry classes of the Java
 * Topology Suite (JTS). The methods assumes that users know what they're doing and will automatically
 * downcast query results to <code>com.vividsolutions.jts.geom.Geometry</code>.
 */
public class JtsSpatialHelper extends SpatialHelper
{
    /**
     * Creates a new <code>JtsSpatialHelper</code> instance for the given PMF.
     * @param pmf The PMF, can't be <code>null</code> or closed.
     */
    public JtsSpatialHelper(JDOPersistenceManagerFactory pmf)
    {
        super(pmf);
    }

    /**
     * Returns the srid of the first datastore entry for the given geometry field. Will return
     * <code>null</code>, if there are no entries or if the geometry field of the first entry is
     * <code>null</code>.
     * @param pc The PersistenceCapapable class
     * @param fieldName Name of the geometry field
     * @param pm <code>PersistenceManager</code> instance that should be used to access the datastore
     * @return The srid or <code>null</code>
     */
    public Integer getSridFromDatastoreEntry(final Class pc, final String fieldName, final PersistenceManager pm)
    {
        checkValid(pc, fieldName);

        Integer srid = null;
        Geometry geom = (Geometry) readFirstValueForField(pc, fieldName, pm);
        if (geom != null)
        {
            srid = Integer.valueOf(geom.getSRID());
        }

        return srid;
    }
}