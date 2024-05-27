package kz.sn34.raytrace.materials;

import java.io.Serializable;

import kz.sn34.raytrace.util.Vector3;
import kz.sn34.raytrace.util.Ray;
import kz.sn34.raytrace.hitables.util.HitRecord;

public class Lambertian extends Material implements Serializable
{
    private static final long serialVersionUID = 1L;

    public Lambertian(Vector3 albedo)
    {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, Vector3 attenuation,
            Ray scattered)
    {
        Vector3 randVector = Vector3.getRandomInUnitSphere().getUnitVector();
      
        Vector3 scatterDirection = rec.getNormal().add(randVector);

        if(Vector3.isNearZero(scatterDirection))
            scatterDirection = rec.getNormal();

        scattered.setOrigin(rec.getPoint());
        scattered.setDirection(scatterDirection);
        attenuation.copy(this.albedo);

        return true;
    }
}
