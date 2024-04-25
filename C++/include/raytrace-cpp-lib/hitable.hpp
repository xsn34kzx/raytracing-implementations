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

#ifndef HITABLE_HPP
#define HITABLE_HPP

#include <raytrace-cpp-lib/ray.hpp>
#include <raytrace-cpp-lib/hit_record.hpp>

class hitable
{
    public:
        virtual ~hitable() {}

        virtual bool hit(const ray& r, float t_min, float t_max, hit_record& rec) const = 0;
};

#endif