package kz.sn34.raytrace_java_lib;

import java.io.Serializable;

public abstract class Material implements Serializable
{
    private static final long serialVersionUID = 1L;

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
