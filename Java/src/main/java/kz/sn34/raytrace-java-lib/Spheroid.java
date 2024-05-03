package kz.sn34.raytrace_java_lib;

public class Spheroid extends Sphere
{
    private Vector3 coefficients;

    public Spheroid(Vector3 coefficients, Vector3 center, double radius)
    {
        // Constructors removed and default to Lambertian until issues with
        // rendering are fixed later
        super(center, radius, new Lambertian(new Vector3(1)));
        this.coefficients = coefficients;
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec)
    {
        Vector3 center = this.getCenter();
        double radius = this.getRadius();

        Vector3 rayOriginMinusCenter = r.getOrigin().subtract(center);
        Vector3 rayDirection = r.getDirection();

        double a = rayDirection.weightedDot(rayDirection, this.coefficients);
        double b = rayDirection.weightedDot(rayOriginMinusCenter, 
                this.coefficients);
        double c = rayOriginMinusCenter.dot(rayOriginMinusCenter)
            - radius * radius;

        double discriminant = b * b - a * c;

        if(discriminant > 0)
        {
            double sqrtDiscriminant = Math.sqrt(discriminant);

            double closestRoot = (-b - sqrtDiscriminant) / a;
            if(closestRoot < tMax && closestRoot > tMin)
            {
                Vector3 point = r.pointAt(closestRoot);

                rec.setT(closestRoot);
                rec.setPoint(point);
                rec.setNormal(point.subtract(center).getUnitVector());

                return true;
            }

            double farthestRoot = (-b + sqrtDiscriminant) / a;
            if(farthestRoot < tMax && farthestRoot > tMin)
            {
                Vector3 point = r.pointAt(farthestRoot);

                rec.setT(farthestRoot);
                rec.setPoint(point);
                rec.setNormal(point.subtract(center).getUnitVector());

                return true;
            }
        }
        
        return false;
    }
}
