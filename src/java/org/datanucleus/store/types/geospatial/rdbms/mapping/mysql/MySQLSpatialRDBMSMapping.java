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
package org.datanucleus.store.types.geospatial.rdbms.mapping.mysql;

import java.sql.SQLException;

import org.datanucleus.store.rdbms.mapping.datastore.AbstractDatastoreMapping;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.table.Column;

/**
 * Abstract base class for all MySQL spatial mappings. Contains helper methods that convert WKB (Well Known
 * Binary) data to MySQL spatial objects and vice versa.
 */
public abstract class MySQLSpatialRDBMSMapping extends AbstractDatastoreMapping
{
    protected static final int SRID_LENGTH = 4;

    private static final byte XDR = 0; // big endian

    private static final byte NDR = 1; // little endian

    public MySQLSpatialRDBMSMapping(JavaTypeMapping mapping, RDBMSStoreManager storeMgr, Column col)
    {
        super(storeMgr, mapping);
        column = col;
        initialize();
    }

    protected void initialize()
    {
        initTypeInfo();
    }

    /**
     * Converts a spatial object from MySQL binary format to WKB.
     * @param mysqlBinary A spatial object in MySQL binary format.
     * @return The spatial object as WKB.
     * @throws SQLException If the object contains invalid data.
     */

    protected byte[] mysqlBinaryToWkb(byte[] mysqlBinary) throws SQLException
    {
        byte[] wkb = new byte[mysqlBinary.length - SRID_LENGTH];
        System.arraycopy(mysqlBinary, SRID_LENGTH, wkb, 0, wkb.length);
        return wkb;
    }

    /**
     * Extracts the SRID of a spatial object in MySQL binary format.
     * @param mysqlBinary A spatial object in MySQL binary format.
     * @return The SRID
     * @throws SQLException If the object contains invalid data.
     */

    protected int mysqlBinaryToSrid(byte[] mysqlBinary) throws SQLException
    {
        if (isBigEndian(mysqlBinary[SRID_LENGTH]))
        {
            return mysqlBinary[0] << 24 | (mysqlBinary[1] & 0xff) << 16 | (mysqlBinary[2] & 0xff) << 8 | (mysqlBinary[3] & 0xff);
        }
        else
        {
            return mysqlBinary[3] << 24 | (mysqlBinary[2] & 0xff) << 16 | (mysqlBinary[1] & 0xff) << 8 | (mysqlBinary[0] & 0xff);
        }
    }

    /**
     * Converts a spatial object from WKB to MySQL binary format.
     * @param wkb A spatial object in WKB format.
     * @param srid The SRID of the spatial object.
     * @return The spatial object in MySQL binary format.
     * @throws SQLException If the object contains invalid data.
     */

    protected byte[] wkbToMysqlBinary(byte[] wkb, int srid) throws SQLException
    {
        byte[] mysqlBinary = new byte[wkb.length + SRID_LENGTH];
        if (isBigEndian(wkb[0]))
        {
            mysqlBinary[0] = (byte) (srid >> 24);
            mysqlBinary[1] = (byte) (srid >> 16);
            mysqlBinary[2] = (byte) (srid >> 8);
            mysqlBinary[3] = (byte) srid;
        }
        else
        {
            mysqlBinary[3] = (byte) (srid >> 24);
            mysqlBinary[2] = (byte) (srid >> 16);
            mysqlBinary[1] = (byte) (srid >> 8);
            mysqlBinary[0] = (byte) srid;
        }
        System.arraycopy(wkb, 0, mysqlBinary, SRID_LENGTH, wkb.length);
        return mysqlBinary;
    }

    private boolean isBigEndian(byte flag) throws SQLException
    {
        if (flag == XDR)
            return true;
        else if (flag == NDR)
            return false;
        else
            throw new SQLException("Unknown Endian type:" + flag);
    }

}
