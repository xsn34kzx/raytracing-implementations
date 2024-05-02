package kz.sn34.raytrace_java_lib;

public interface Hitable
{
    static final double TMAX = 10;
    static final double TMIN = 0;

    boolean hit(Ray r, double tMin, double tMax, HitRecord rec);
}
