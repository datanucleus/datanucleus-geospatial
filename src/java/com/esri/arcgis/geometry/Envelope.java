package com.esri.arcgis.geometry;

import java.io.IOException;

import com.esri.arcgis.system.IClone;
import com.linar.jintegra.AutomationException;

public class Envelope implements IGeometry, IClone
{
    private static final long serialVersionUID = 1L;

    public Envelope() {
        // constructor stub
    }
    
    public Envelope(Object obj) {
        // constructor stub
    }
    
    public void setSpatialReferenceByRef(ISpatialReference spatialRef)
    {
        // method stub
    }

    public void setLowerLeft(Point lowerLeft)
    {
        // method stub
    }

    public void setLowerRight(Point lowerRight)
    {
        // method stub
    }

    public void setUpperLeft(Point upperLeft)
    {
        // method stub
    }

    public void setUpperRight(Point upperRight)
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
    
    public boolean isEqual(IClone clone) {
        return true;
    }
}
