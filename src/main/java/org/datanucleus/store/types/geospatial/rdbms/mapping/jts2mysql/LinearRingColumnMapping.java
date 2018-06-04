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
package org.datanucleus.store.types.geospatial.rdbms.mapping.jts2mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.table.Column;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;

/**
 * Mapping for a JTS LinearRing object to MySQL.
 */
public class LinearRingColumnMapping extends LineStringColumnMapping
{

    public LinearRingColumnMapping(JavaTypeMapping mapping, RDBMSStoreManager storeMgr, Column col)
    {
        super(mapping, storeMgr, col);
    }

    public Object getObject(ResultSet rs, int exprIndex)
    {
        LineString lineString = (LineString) super.getObject(rs, exprIndex);
        if (lineString == null)
            return null;
        LinearRing linearRing = new LinearRing(lineString.getCoordinateSequence(), lineString.getFactory());
        linearRing.setSRID(lineString.getSRID());
        return linearRing;
    }

    public void setObject(PreparedStatement ps, int exprIndex, Object value)
    {
        LineString lineString = null;
        if (value != null)
        {
            LinearRing linearRing = (LinearRing) value;
            lineString = new LineString(linearRing.getCoordinateSequence(), linearRing.getFactory());
            lineString.setSRID(linearRing.getSRID());
        }
        super.setObject(ps, exprIndex, lineString);
    }

}
