package kz.sn34.raytrace_java_lib;

public class Metal extends Material
{
    private Vector3 albedo;
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

        Vector3 randUnitVector = Vector3.getRandomUnitVector();

        scattered.setOrigin(rec.getPoint());
        scattered.setDirection(
                reflected.getUnitVector()
                .add(randUnitVector.multiply(this.fuzz)));
        attenuation.copy(this.albedo);

        return true;
    }
}
