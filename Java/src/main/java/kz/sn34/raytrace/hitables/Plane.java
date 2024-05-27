package kz.sn34.raytrace.hitables;

import kz.sn34.raytrace.util.Vector3;
import kz.sn34.raytrace.util.Ray;
import kz.sn34.raytrace.hitables.util.HitRecord;
import kz.sn34.raytrace.materials.Material;

public class Plane implements Hitable
{
    private Vector3 center;
    private Vector3 coefficients;
    private double constant;
    private Material mat;

    public Plane(Vector3 center, Vector3 coefficients, double constant,
            Material mat)
    {
        this.center = center;
        this.coefficients = coefficients;
        this.constant = constant;

    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec)
    {
        Vector3 originMinusCenter = r.getOrigin().subtract(this.center);
        Vector3 direction = r.getDirection();

        double termOne = originMinusCenter.dot(this.coefficients);
        double termTwo = direction.dot(this.coefficients);

        if(termTwo != 0)
        {
            double root = (-this.constant + termOne) / termTwo;

            if(root < tMax && root > tMin)
            {
                rec.setT(root);
                rec.setPoint(r.pointAt(root));
                rec.setNormal(this.coefficients.getUnitVector());

                return true;
            }
        }

        return false;
    }
}
