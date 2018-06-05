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

import java.awt.Rectangle;

import org.datanucleus.store.types.geospatial.rdbms.mapping.RectangleMapping;
import org.datanucleus.store.rdbms.mapping.column.ColumnMapping;
import org.datanucleus.store.rdbms.mapping.java.IntegerMapping;

/**
 * Mapping for the X or Y component of a RectangleMapping. Is actually a wrapper to the real PointMapping just
 * returning the relevant column mapping.
 */
public class RectangleComponentMapping extends IntegerMapping
{
    /** The actual Rectangle mapping. */
    private final RectangleMapping rectMapping;

    /** The index of the mapping we should return. */
    private final int mappingIndex;

    /**
     * @param rectMapping The <tt>RectangleMapping</tt> instance we are getting our data from.
     * @param mappingIndex The index of the mapping in the previously supplied <tt>PointMapping</tt> instance
     * we should return (ie. the mapping we pretend is the only that exists).
     */
    public RectangleComponentMapping(RectangleMapping rectMapping, int mappingIndex)
    {
        this.rectMapping = rectMapping;
        this.mappingIndex = mappingIndex;
    }

    public ColumnMapping[] getColumnMappings()
    {
        ColumnMapping[] startColumnMapping = {rectMapping.getColumnMappings()[mappingIndex]};
        return startColumnMapping;
    }

    public ColumnMapping getColumnMapping(int index)
    {
        return rectMapping.getColumnMappings()[mappingIndex];
    }

    public int getNumberOfColumnMappings()
    {
        return 1;
    }

    public Class getJavaType()
    {
        return Rectangle.class;
    }
}
