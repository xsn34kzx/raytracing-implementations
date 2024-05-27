package kz.sn34.raytrace.materials;

import java.util.Random;
import java.io.Serializable;

import kz.sn34.raytrace.util.Vector3;
import kz.sn34.raytrace.util.Ray;
import kz.sn34.raytrace.hitables.util.HitRecord;

public class Dielectric extends Material implements Serializable
{
    private static final long serialVersionUID = 1L;

    private double refractionIndex;

    public Dielectric(double refractionIndex)
    {
        this.refractionIndex = refractionIndex;
    }

    public boolean scatter(Ray r, HitRecord rec, Vector3 attenuation,
            Ray scattered)
    {
        this.albedo = new Vector3(1);
        attenuation.copy(albedo);
        double refractionIndex = rec.getFrontFace() 
            ? (1/this.refractionIndex) : this.refractionIndex;

        Vector3 unitDirection = r.getDirection().getUnitVector();
        Vector3 normal = rec.getNormal();

        double cosTheta = Math.min(unitDirection.multiply(-1).dot(normal), 1);
        double sinTheta = Math.sqrt(1 - cosTheta * cosTheta);

        Random rng = new Random();
        
        Vector3 direction;
        if((refractionIndex * sinTheta > 1) || 
                (reflectance(cosTheta, refractionIndex) > rng.nextDouble()))
            direction = unitDirection.reflect(normal);
        else
            direction = unitDirection.refract(normal, refractionIndex);

        scattered.setOrigin(rec.getPoint());
        scattered.setDirection(direction);

        return true;
    }

    static private double reflectance(double cosine, double refractionIndex)
    {
        double r0 = (1 - refractionIndex) / (1 + refractionIndex);
        r0 *= r0;

        return r0 + (1 - r0) * Math.pow(1 - cosine, 5);
    }
}

