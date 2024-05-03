package kz.sn34.raytrace_java_lib;

public interface Material
{
    boolean scatter(Ray r, HitRecord rec, Vector3 attenuation, Ray scattered);
}
