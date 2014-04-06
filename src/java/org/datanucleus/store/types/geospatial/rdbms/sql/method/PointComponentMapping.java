/**********************************************************************
Copyright (c) 2012 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.store.types.geospatial.rdbms.sql.method;

import java.awt.Point;

import org.datanucleus.store.types.geospatial.rdbms.mapping.PointMapping;
import org.datanucleus.store.rdbms.mapping.datastore.DatastoreMapping;
import org.datanucleus.store.rdbms.mapping.java.IntegerMapping;

/**
 * Mapping for the X or Y component of a PointMapping. Is actually a wrapper to the real PointMapping just
 * returning the relevant column mapping.
 */
public class PointComponentMapping extends IntegerMapping
{
    /** The actual Point mapping. */
    private final PointMapping pointMapping;

    /** The index of the mapping we should return. */
    private final int mappingIndex;

    /**
     * @param pointMapping The <tt>PointMapping</tt> instance we are getting our data from.
     * @param mappingIndex The index of the mapping in the previously supplied <tt>PointMapping</tt> instance
     * we should return (ie. the mapping we pretend is the only that exists).
     */
    public PointComponentMapping(PointMapping pointMapping, int mappingIndex)
    {
        this.pointMapping = pointMapping;
        this.mappingIndex = mappingIndex;
    }

    public DatastoreMapping[] getDatastoreMappings()
    {
        DatastoreMapping[] startColumnMapping = {pointMapping.getDatastoreMappings()[mappingIndex]};
        return startColumnMapping;
    }

    public DatastoreMapping getDatastoreMapping(int index)
    {
        return pointMapping.getDatastoreMappings()[mappingIndex];
    }

    public int getNumberOfDatastoreMappings()
    {
        return 1;
    }

    public Class getJavaType()
    {
        return Point.class;
    }
}
