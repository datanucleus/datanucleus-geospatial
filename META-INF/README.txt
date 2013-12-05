********************
* Quickstart Guide *
********************

Refer to the documentation on the JPOX website for more information.
http://www.datanucleus.org/products/accessplatform/spatial.html

Installation
============
* Setup JPOX as usual

* Choose one of the supported mapping scenarios:
  jgeom2oracle
  jgeom2postgis
  jts2mysql
  jts2oracle
  jts2postgis
  pg2mysql
  pg2postgis

* Make the needed JAR-Files available in your classpath:

  jgeom2oracle
  ------------
  - sdoapi.jar
  - classes12.jar (for Java 1.3) / ojdbc14.jar
  
  jgeom2mysql
  ------------
  - sdoapi.jar
  - sdoutl.jar
  - mysql-connector-java-<version>.jar
  
  jts2mysql
  ---------
  - jts.jar
  - mysql-connector-java-<version>.jar
  
  jts2oracle
  ---------
  - jts.jar
  - jtsio.jar
  - classes12.jar (for Java 1.3) / ojdbc14.jar
  
  jts2postgis
  ---------
  - jts.jar
  - postgresql-<version>.jdbc3.jar
  - postgis_jts.jar
  
  pg2mysql
  --------
  - postgis.jar
  - mysql-connector-java-<version>.jar
  
  pg2postgis
  ----------
  - postgresql-<version>.jdbc3.jar
  - postgis.jar
  
Usage
=====
* Use the geometry types of your chosen library in your application

* Specify the geometry fields in your meta-data as you would with 
  any other JPOX-supported data type
  
  It's not mandatory in all cases, but there are some situations where 
  the following extensions are needed:
    
  - The srid & dimension values are used in different places. One of them is the 
    schema creation in PostGIS. Another is, when you query the SpatialHelper. 
    
    <extension vendor-name="jpox" key="spatial-srid" value="2001"/>
    <extension vendor-name="jpox" key="spatial-dimension" value="2"/>
    
  - If you want to use measure types in PostGIS you have to define that like this:
  
    <extension vendor-name="jpox" key="postgis-hasMeasure" value="true"/>
    
  - Every JTS geometry object can have a user data object attached to it. The 
    default behaviour is to serialize that object and store it in a separate column 
    in the database. If for some reason this isn't desired, the following extension 
    can be used and JPOX-Spatial will ignore the user data objects.

    <extension vendor-name="jpox" key="mapping" value="no-userdata"/>
    
 => The position of these tags in the meta-data determines their scope. If you use 
    them inside a <field>-tag the values are only used for that field specifically, 
    if you use them inside the <package>-tag the values are in effect for all 
    (geometry) fields of all classes inside that package, etc.

* Spatial functions can be used like this:

    Query query = pm.newQuery( ClassWithAPoint.class, "Spatial.isEmpty( point )" );
    query.setResultClass( Boolean.class );
    query.setUnique( true );
    Boolean isEmpty = (Boolean)query.execute();

 => For a list of all available functions see the documentation or have a look at 
    org.jpox.store.expression.spatial.SpatialExpression
    
* Use the SpatialHelper API to access metadata like dimension, srid and bounding box
  of your spatial data.
  
 => org.jpox.store.rdbms.spatial.SpatialHelper
