#include <raytrace-cpp-lib/vec3.hpp>
#include <raytrace-cpp-lib/ray.hpp>

ray::ray() {}

ray::ray(const vec3<float>& origin, const vec3<float>& direction)
    : origin(origin), direction(direction) {}

vec3<float> ray::get_origin() const
{
    return origin;
}

vec3<float> ray::get_direction() const
{
    return direction;
}

void ray::set_direction(const vec3<float>& direction)
{
    this->direction = direction;
}

vec3<float> ray::point_at(float t) const
{
    return origin + direction * t;
}

vec3<float> ray::lerp(float t) const
{
    return origin * (1.0 - t) + direction * t;
}
