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