package kz.sn34.raytrace_java_lib;

public class Spheroid extends Sphere
{
    private Vector3 coefficients;

    public Spheroid(Vector3 coefficients, Vector3 center, Material mat)
    {
        // TODO: Change Sphere & Spheroid class relationship
        super(center, 1, mat);
        this.coefficients = coefficients;
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec)
    {
        Vector3 center = this.getCenter();

        Vector3 rayOriginMinusCenter = r.getOrigin().subtract(center);
        Vector3 rayDirection = r.getDirection();

        double a = rayDirection.weightedDot(rayDirection, this.coefficients);
        double b = rayDirection.weightedDot(rayOriginMinusCenter, 
                this.coefficients);
        double c = rayOriginMinusCenter.weightedDot(rayOriginMinusCenter,
                this.coefficients) - 1;

        double discriminant = b * b - a * c;

        if(discriminant <= 0)
            return false;

        double sqrtDiscriminant = Math.sqrt(discriminant);

        double root = (-b - sqrtDiscriminant) / a;
        if(root >= tMax || root <= tMin)
        {
            root = (-b + sqrtDiscriminant) / a;
            if(root >= tMax || root <= tMin)
                return false;
        }

        Vector3 point = r.pointAt(root);
        Vector3 normal = point.subtract(center).getUnitVector();


        // TODO: Investigate normal vector calculation/setting issues
        rec.setT(root);
        rec.setPoint(point);
        rec.setMaterial(this.getMaterial());
        rec.setFaceNormal(rayDirection, normal);

        return true;
    }
}
