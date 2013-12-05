package com.esri.arcgis.geometry;

import java.io.*;

import com.linar.jintegra.AutomationException;

public interface IGeometry extends Serializable
{
    public void setSpatialReferenceByRef(ISpatialReference spatialRef);
    
    public int getGeometryType() throws IOException, AutomationException;

    public ISpatialReference getSpatialReference();
}
