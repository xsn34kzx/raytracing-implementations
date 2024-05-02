#include <raytrace-cpp-lib/vec3.hpp>
#include <raytrace-cpp-lib/ray.hpp>
#include <raytrace-cpp-lib/camera.hpp>

camera::camera()
    : upLeftCanonical(vec3<float>(-1.0, 1.0, -1.0)), 
    deltaX(vec3<float>(2.0, 0.0, 0.0)), deltaY(vec3<float>(0.0, -2.0, 0.0)) {}

ray camera::get_ray(float u, float v) const
{
    return ray(origin, vec3<float>(upLeftCanonical + deltaX * u + deltaY * v));
}
