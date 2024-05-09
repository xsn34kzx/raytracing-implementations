package kz.sn34.raytrace_java_lib;

public abstract class Material
{
    protected Vector3 albedo;

    public Vector3 getAlbedo()
    {
        return this.albedo;
    }

    public boolean scatter(Ray r, HitRecord rec, Vector3 attenuation, 
            Ray scattered)
    {
        return false;
    }
}
