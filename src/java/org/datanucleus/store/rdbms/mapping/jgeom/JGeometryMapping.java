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
2006 Erik Bengtson - Original implementation
    ...
**********************************************************************/
package org.datanucleus.store.rdbms.mapping.jgeom;

import oracle.spatial.geometry.JGeometry;

import org.datanucleus.store.rdbms.mapping.java.SingleFieldMapping;

/**
 * Mapping for oracle.spatial.geometry.JGeometry to its datastore representation.
 */
public class JGeometryMapping extends SingleFieldMapping
{
    public Class getJavaType()
    {
        return JGeometry.class;
    }

    /**
     * Any usage of this type as a parameter cannot be used as a String in SQL.
     * @return false
     */
    public boolean representableAsStringLiteralInStatement()
    {
        return false;
    }
}