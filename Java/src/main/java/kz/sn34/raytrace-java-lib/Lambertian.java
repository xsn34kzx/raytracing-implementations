package kz.sn34.raytrace_java_lib;

public class Lambertian implements Material
{
    private Vector3 albedo;

    @Override
    public boolean scatter(Ray r, HitRecord rec, Vector3 attenuation,
            Ray scattered) 
    {
        // For Chapter 7, all objects will have an albedo of gray
        this.albedo = new Vector3(0.5, 0.5, 0.5);

        Vector3 randVector;
        do {
            randVector = Vector3.random(-1, 1);
        } while(randVector.getMagnitudeSquared() >= 1);

        randVector = randVector.getUnitVector();

        if(randVector.dot(rec.getNormal()) <= 0)
            randVector = randVector.multiply(-1);

        Vector3 scatterDirection = rec.getNormal().add(randVector);
        scattered.setOrigin(rec.getPoint());
        scattered.setDirection(scatterDirection);
        attenuation.copy(this.albedo);

        return true;
    }
}
