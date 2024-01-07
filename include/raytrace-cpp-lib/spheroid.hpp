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

#ifndef SPHEROID_HPP
#define SPHEROID_HPP

#include <raytrace-cpp-lib/vec3.hpp>
#include <raytrace-cpp-lib/ray.hpp>
#include <raytrace-cpp-lib/hit_record.hpp>
#include <raytrace-cpp-lib/sphere.hpp>

class spheroid : public sphere
{
    public:
        spheroid(const vec3<float>& coefficients);
        spheroid(const vec3<float>& coefficients, float radius);
        spheroid(const vec3<float>& coefficients, const vec3<float>& center);
        spheroid(const vec3<float>& coefficients, const vec3<float>& center, float radius);

        bool hit(const ray& r, float t_min, float t_max, hit_record& rec) const override;

    private:
        vec3<float> coefficients;
};

#endif