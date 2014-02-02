package com.esri.arcgis.geometry;

import java.io.IOException;

import com.esri.arcgis.system.IClone;
import com.linar.jintegra.AutomationException;

public class Point implements IGeometry, IClone
{
    private static final long serialVersionUID = 1L;

    public Point()
    {
        // constructor stub
    }

    public Point(Object obj)
    {
        // constructor stub
    }

    public void setSpatialReferenceByRef(ISpatialReference spatialRef)
    {
        // method stub
    }

    public void putCoords(double d, double e)
    {
        // method stub
    }

    public int getGeometryType() throws IOException, AutomationException
    {
        // method stub
        return 0;
    }

    public ISpatialReference getSpatialReference()
    {
        // method stub
        return null;
    }

    public boolean isEqual(IClone clone)
    {
        return true;
    }

    public void setX(double x)
    {
        // method stub
    }

    public void setY(double y)
    {
        // method stub
    }
}
