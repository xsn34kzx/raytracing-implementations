#ifndef CAMERA_HPP
#define CAMERA_HPP

#include <raytrace-cpp-lib/vec3.hpp>
#include <raytrace-cpp-lib/ray.hpp>

class camera
{
    public:
        camera();

        ray get_ray(float u, float v) const;

    private:
        vec3<float> origin;
        vec3<float> upLeftCanonical;
        vec3<float> deltaX;
        vec3<float> deltaY;
};

#endif
