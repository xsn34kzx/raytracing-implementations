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
#include <raytrace-cpp-lib/vec3.hpp>
#include <raytrace-cpp-lib/ray.hpp>
#include <raytrace-cpp-lib/hit_record.hpp>
#include <raytrace-cpp-lib/plane.hpp>

plane::plane()
    : coefficients(vec3<float>(1.0)) {}

plane::plane(float constant)
    : coefficients(vec3<float>(1.0)), constant(constant) {}

plane::plane(const vec3<float>& coefficients, float constant)
    : coefficients(coefficients), constant(constant) {}

plane::plane(const vec3<float>& center, const vec3<float>& coefficients, float constant)
    : center(center), coefficients(coefficients), constant(constant) {}

bool plane::hit(const ray& r, float t_min, float t_max, hit_record& rec) const
{
    float termOne = (r.get_origin() - center).dot(coefficients);
    float termTwo = r.get_direction().dot(coefficients);

    if(termTwo != 0.0)
    {
        float root = (-constant + termOne) / termTwo;

        if(root < t_max && root > t_min)
        {
            rec.t = root;
            rec.point = r.point_at(root);
            rec.normal = coefficients.unit_vector();

            return true;
        }
    }

    return false;
}