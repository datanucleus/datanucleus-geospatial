/**********************************************************************
 Copyright (c) 2007 Roger Blum, Pascal N�esch and others. All rights reserved.
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
package org.datanucleus.store.rdbms.mapping.ao2postgis;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.table.Column;

import com.esri.arcgis.geometry.GeometryBag;

/**
 * Mapping for a ESRI GeometryBag object to PostGIS.
 */
public class GeometryBagRDBMSMapping extends GeometryRDBMSMapping
{
    public GeometryBagRDBMSMapping(JavaTypeMapping mapping, RDBMSStoreManager storeMgr, Column col)
    {
        super(mapping, storeMgr, col);
    }

    public Object getObject(ResultSet rs, int exprIndex)
    {
        GeometryBag value = null;

        try
        {
            byte[] binaryResult = ((ResultSet) rs).getBytes(exprIndex);
            if (!((ResultSet) rs).wasNull() && binaryResult != null)
            {
                value = new GeometryBag(getFromWkb(binaryResult));
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("getObject", exprIndex, e), e);
        }

        return value;
    }
}
