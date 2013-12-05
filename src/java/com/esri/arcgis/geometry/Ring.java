package com.esri.arcgis.geometry;

import java.io.IOException;

import com.esri.arcgis.system.IClone;
import com.linar.jintegra.AutomationException;

public class Ring implements IGeometry, IClone
{
    private static final long serialVersionUID = 1L;

    public Ring() {
        // constructor stub
    }
    
    public Ring(Object obj) {
        // constructor stub
    }
    
    public void setSpatialReferenceByRef(ISpatialReference spatialRef)
    {
        // method stub
    }

    public int getGeometryType() throws IOException, AutomationException
    {
        // method stub
        return 0;
    }

    public void addPoint(Point point, Object object, Object object2)
    {
        // method stub
        
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
