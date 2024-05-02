package kz.sn34.raytrace_java_lib;

public class Camera
{
    private Vector3 origin;
    private Vector3 upLeftCanonical;
    private Vector3 deltaX;
    private Vector3 deltaY;

    public Camera()
    {
        this.origin = new Vector3();
        this.upLeftCanonical = new Vector3(-1, 1, -1);
        this.deltaX = new Vector3(2, 0, 0);
        this.deltaY = new Vector3(0, -2, 0);
    }

    public Ray getRay(double u, double v)
    {
        Vector3[] terms = {this.deltaX.multiply(u), this.deltaY.multiply(v)};

        return new Ray(this.origin, this.upLeftCanonical.add(terms));
    }
}
