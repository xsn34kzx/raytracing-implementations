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