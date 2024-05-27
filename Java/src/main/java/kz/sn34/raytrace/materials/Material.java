package kz.sn34.raytrace.materials;

import java.io.Serializable;

import kz.sn34.raytrace.util.Vector3;
import kz.sn34.raytrace.util.Ray;
import kz.sn34.raytrace.hitables.util.HitRecord;

public abstract class Material implements Serializable
{
    private static final long serialVersionUID = 1L;

    protected Vector3 albedo;

    public Vector3 getAlbedo()
    {
        return this.albedo;
    }

    public abstract boolean scatter(Ray r, HitRecord rec, Vector3 attenuation,
            Ray scattered);
}
