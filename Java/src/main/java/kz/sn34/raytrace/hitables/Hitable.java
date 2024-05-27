package kz.sn34.raytrace.hitables;

import kz.sn34.raytrace.util.Ray;
import kz.sn34.raytrace.hitables.util.HitRecord;

public interface Hitable 
{
    static final double TMAX = Double.MAX_VALUE; 
    static final double TMIN = 1e-3;

    boolean hit(Ray r, double tMin, double tMax, HitRecord rec);
}
