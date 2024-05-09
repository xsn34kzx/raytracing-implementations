package kz.sn34.raytrace_java_lib;

public class Lambertian extends Material
{
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
