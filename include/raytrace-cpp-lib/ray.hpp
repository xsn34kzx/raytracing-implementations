#ifndef RAY_HPP
#define RAY_HPP

#include <raytrace-cpp-lib/vec3.hpp>

class ray
{
    private:
        vec3<float> origin;
        vec3<float> direction;

    public:
        ray();
        ray(const vec3<float>& origin, const vec3<float>& direction);

        vec3<float> get_origin() const;
        vec3<float> get_direction() const;

        void set_direction(const vec3<float>& direction);

        vec3<float> point_at(float t) const;
        vec3<float> lerp(float t) const;
};

#endif
