package kz.sn34.raytrace_java_lib;

public abstract class Material
{
    public boolean scatter(Ray r, HitRecord rec, Vector3 attenuation, 
            Ray scattered)
    {
        return false;
    }
}
