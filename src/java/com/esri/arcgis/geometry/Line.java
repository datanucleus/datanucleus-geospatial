package com.esri.arcgis.geometry;

import java.io.IOException;

import com.esri.arcgis.system.IClone;
import com.linar.jintegra.AutomationException;

public class Line implements IGeometry, IClone
{
    private static final long serialVersionUID = 1L;

    public Line() {
        // constructor stub
    }
    
    public Line(Object obj) {
        // constructor stub
    }
    
    public void setSpatialReferenceByRef(ISpatialReference spatialRef)
    {
        // method stub
    }

    public void putCoords(Point startPoint, Point endPoint)
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
