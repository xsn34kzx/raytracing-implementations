/**
 *  This file is a part of raytrace-cpp.
 *  Copyright (C) 2023-2024  Amani Hernandez
 *
 *  raytrace-cpp is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

#include <cmath>

#include <raytrace-cpp-lib/vec3.hpp>
#include <raytrace-cpp-lib/ray.hpp>
#include <raytrace-cpp-lib/hit_record.hpp>
#include <raytrace-cpp-lib/sphere.hpp>
#include <raytrace-cpp-lib/spheroid.hpp>

spheroid::spheroid(const vec3<float>& coefficients)
    : coefficients(coefficients), sphere(1.0) {}

spheroid::spheroid(const vec3<float>& coefficients, float radius)
    : coefficients(coefficients), sphere(radius) {}

spheroid::spheroid(const vec3<float>& coefficients, const vec3<float>& center)
    : coefficients(coefficients), sphere(center) {}

spheroid::spheroid(const vec3<float>& coefficients, const vec3<float>& center, float radius)
    : coefficients(coefficients), sphere(center, radius) {}

bool spheroid::hit(const ray& r, float t_min, float t_max, hit_record& rec) const
{
    vec3<float> origMinusCenter = r.get_origin() - get_center();
    vec3<float> dir = r.get_direction();

    float a = dir.weighted_dot(dir, coefficients);
    float b = dir.weighted_dot(origMinusCenter, coefficients);
    float c = origMinusCenter.weighted_dot(origMinusCenter, coefficients)
        - get_radius() * get_radius();
    
    float discriminant = b * b - a * c;

    if(discriminant > 0)
    {
        float sqrtDiscriminant = std::sqrt(discriminant);

        float closestRoot = (-b - sqrtDiscriminant) / a;
        if(closestRoot < t_max && closestRoot > t_min)
        {
            rec.t = closestRoot;
            rec.point = r.point_at(closestRoot);
            rec.normal = (rec.point - get_center()) / get_radius();

            return true;
        }

        float farthestRoot = (-b + sqrtDiscriminant) / a;
        if(farthestRoot < t_max && farthestRoot > t_min)
        {
            rec.t = farthestRoot;
            rec.point = r.point_at(farthestRoot);
            rec.normal = (rec.point - get_center()) / get_radius();

            return true;
        }
    }

    return false;
}