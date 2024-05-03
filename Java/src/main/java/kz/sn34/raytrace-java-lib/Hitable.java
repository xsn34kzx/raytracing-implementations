package kz.sn34.raytrace_java_lib;

public interface Hitable
{
    static final double TMAX = Double.MAX_VALUE; 
    static final double TMIN = 1e-3;

    boolean hit(Ray r, double tMin, double tMax, HitRecord rec);
}
