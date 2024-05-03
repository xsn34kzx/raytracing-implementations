package kz.sn34.raytrace_java_lib;

public class Lambertian extends Material
{
    private Vector3 albedo;

    public Lambertian(Vector3 albedo)
    {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, Vector3 attenuation,
            Ray scattered)
    {
        Vector3 randUnitVector = Vector3.getRandomUnitVector();
      
        if(randUnitVector.dot(rec.getNormal()) <= 0)
            randUnitVector = randUnitVector.multiply(-1);

        Vector3 scatterDirection = rec.getNormal().add(randUnitVector);

        if(Vector3.isNearZero(scatterDirection))
            scatterDirection = rec.getNormal();

        scattered.setOrigin(rec.getPoint());
        scattered.setDirection(scatterDirection);
        attenuation.copy(this.albedo);

        return true;
    }
}
