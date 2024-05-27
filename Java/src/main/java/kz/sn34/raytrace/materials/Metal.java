package kz.sn34.raytrace.materials;

import java.io.Serializable;

import kz.sn34.raytrace.util.Vector3;
import kz.sn34.raytrace.util.Ray;
import kz.sn34.raytrace.hitables.util.HitRecord;

public class Metal extends Material implements Serializable
{
    private static final long serialVersionUID = 1L;

    private double fuzz;

    public Metal(Vector3 albedo, double fuzz)
    {
        this.albedo = albedo;
        this.fuzz = (fuzz < 1) ? fuzz : 1;
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, Vector3 attenuation,
            Ray scattered)
    {
        Vector3 direction = r.getDirection();
        Vector3 curNormal = rec.getNormal();

        Vector3 reflected = direction.reflect(curNormal);

        Vector3 randVector = Vector3.getRandomInUnitSphere();

        scattered.setOrigin(rec.getPoint());
        scattered.setDirection(
                reflected.getUnitVector()
                .add(randVector.multiply(this.fuzz)));
        attenuation.copy(this.albedo);

        return true;
    }
}
