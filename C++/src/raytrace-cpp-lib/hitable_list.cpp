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

#include <vector>

#include <raytrace-cpp-lib/ray.hpp>
#include <raytrace-cpp-lib/hit_record.hpp>
#include <raytrace-cpp-lib/hitable.hpp>
#include <raytrace-cpp-lib/hitable_list.hpp>

hitable_list::hitable_list() 
    : size(0) {}

hitable_list::~hitable_list()
{
    for(hitable* shape : list)
    {
        delete shape;
    }
}

void hitable_list::add(hitable* shape)
{
    list.push_back(shape);
}

bool hitable_list::hit(const ray& r, float t_min, float t_max, hit_record& rec) const
{
    hit_record curRec;
    bool hitSomething = false;
    float closestSoFar = t_max;

    for(hitable* shape : list)
    {
        if(shape->hit(r, t_min, closestSoFar, curRec))
        {
            hitSomething = true;
            closestSoFar = curRec.t;
        }
    }

    rec = curRec;
    return hitSomething;
}