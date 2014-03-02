/*
 * Copyright 2014 Baris ERGUN.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.datanucleus.store.rdbms.schema;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author barisergun
 */
public class OracleSpatialTypeInfoTest
{

    public OracleSpatialTypeInfoTest()
    {
    }

    private static final int CORRECT_SPATIAL_TYPE_INFO_1 = 2005;

    private static final int CORRECT_SPATIAL_TYPE_INFO_2 = 4307;

    private static final int CORRECT_SPATIAL_TYPE_INFO_3 = 3402;

    private static final int FALSE_SPATIAL_TYPE_INFO_1 = 5005;

    private static final int FALSE_SPATIAL_TYPE_INFO_2 = 4507;

    private static final int FALSE_SPATIAL_TYPE_INFO_3 = 3102;

    @Test
    public void testSpatialTypeInfoPattern()
    {
        Assert.assertTrue(String.valueOf(CORRECT_SPATIAL_TYPE_INFO_1).matches(OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY_PATTERN));
        Assert.assertTrue(String.valueOf(CORRECT_SPATIAL_TYPE_INFO_2).matches(OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY_PATTERN));
        Assert.assertTrue(String.valueOf(CORRECT_SPATIAL_TYPE_INFO_3).matches(OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY_PATTERN));
        Assert.assertFalse(String.valueOf(FALSE_SPATIAL_TYPE_INFO_1).matches(OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY_PATTERN));
        Assert.assertFalse(String.valueOf(FALSE_SPATIAL_TYPE_INFO_2).matches(OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY_PATTERN));
        Assert.assertFalse(String.valueOf(FALSE_SPATIAL_TYPE_INFO_3).matches(OracleSpatialTypeInfo.TYPES_SDO_GEOMETRY_PATTERN));

    }

    @org.testng.annotations.BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @org.testng.annotations.AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @org.testng.annotations.BeforeMethod
    public void setUpMethod() throws Exception
    {
    }

    @org.testng.annotations.AfterMethod
    public void tearDownMethod() throws Exception
    {
    }
}
