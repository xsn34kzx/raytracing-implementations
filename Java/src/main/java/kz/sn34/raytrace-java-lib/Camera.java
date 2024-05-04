package kz.sn34.raytrace_java_lib;

public class Camera
{
    private Vector3 origin;
    private Vector3 upLeftCanonical;
    private Vector3 deltaX;
    private Vector3 deltaY;

    public Camera(Vector3 lookFrom, Vector3 lookAt, Vector3 camUp, 
            double verticalFOV, double aspectRatio)
    {
        double theta = Math.toRadians(verticalFOV);
        double halfHeight = Math.tan(theta / 2);
        double halfWidth = aspectRatio * halfHeight;
        this.origin = lookFrom;
        
        Vector3 w = lookFrom.subtract(lookAt).getUnitVector();
        Vector3 u = camUp.cross(w).getUnitVector();
        Vector3 v = w.cross(u);

        Vector3[] terms = {u.multiply(halfWidth), v.multiply(halfHeight)};
        this.upLeftCanonical = this.origin.add(terms).subtract(w);
        this.deltaX = u.multiply(-2 * halfWidth);
        this.deltaY = v.multiply(-2 * halfHeight);
    }

    public Ray getRay(double u, double v)
    {
        Vector3[] terms = {this.deltaX.multiply(u), this.deltaY.multiply(v)};

        return new Ray(this.origin, this.upLeftCanonical.add(terms).subtract(
                    this.origin));
    }
}
