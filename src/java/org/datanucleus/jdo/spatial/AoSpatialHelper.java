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
package org.datanucleus.jdo.spatial;

import java.io.IOException;

import javax.jdo.PersistenceManager;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;

import com.esri.arcgis.geometry.IGeometry;

/**
 * Helper class to read Spatial MetaData from the datastore, specialized for the geometry classes of 
 * the ESRO ArcObjects. The methods assumes that users know what they're doing and will 
 * automatically downcast query results to <code>com.esri.arcgis.geometry.IGeometry</code>.
 */
public class AoSpatialHelper extends SpatialHelper
{
    /**
     * Creates a new <code>AoSpatialHelper</code> instance for the given PMF.
     * 
     * @param pmf The PMF, can't be <code>null</code> or closed.
     */
    public AoSpatialHelper(JDOPersistenceManagerFactory pmf)
    {
        super(pmf);
    }

    /**
     * Returns the srid of the first datastore entry for the given geometry field. 
     * Will return <code>null</code>, if there are no entries or if the geometry 
     * field of the first entry is <code>null</code>.
     *
     * @param pc The PersistenceCapapable class
     * @param fieldName Name of the geometry field
     * @param pm <code>PersistenceManager</code> instance that should be used 
     *        to access the datastore
     * @return The srid or <code>null</code>
     */
    public Integer getSridFromDatastoreEntry(final Class pc, final String fieldName, final PersistenceManager pm)
    {
        checkValid(pc, fieldName);
    
        Integer srid = null;
        IGeometry geom = (IGeometry) readFirstValueForField(pc, fieldName, pm);
        if (geom != null)
        {
            try
            {
                srid = Integer.valueOf(geom.getSpatialReference().getSpatialReferenceImpl());
            }
            catch (IOException e)
            {
                return null;
            }
        }
        
        return srid;
    }
}