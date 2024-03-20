/**
 *  raytrace-cpp - a CLI program that uses raytracing to generate images
 *  Copyright (C) 2023-2024  Amani Hernandez
 *
 *  This program is free software: you can redistribute it and/or modify
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

#include <iostream>
#include <fstream>
#include <string>
#include <vector>

#include <raytrace-cpp-lib/vec3.hpp>
#include <raytrace-cpp-lib/ray.hpp>
#include <raytrace-cpp-lib/camera.hpp>
#include <raytrace-cpp-lib/hit_record.hpp>
#include <raytrace-cpp-lib/hitable.hpp>
#include <raytrace-cpp-lib/hitable_list.hpp>
#include <raytrace-cpp-lib/hitable_constants.hpp>
#include <raytrace-cpp-lib/plane.hpp>
#include <raytrace-cpp-lib/sphere.hpp>
#include <raytrace-cpp-lib/spheroid.hpp>

vec3<float> color(const ray& r, const hitable& world)
{
    hit_record finalRec;
    if(world.hit(r, hitable_constants::T_MIN, hitable_constants::T_MAX, finalRec))
    {
        return vec3<float>(finalRec.normal.x() + 1.0,
                finalRec.normal.y() + 1.0, finalRec.normal.z() + 1.0) * 0.5;
    }
    else
    {
        ray colorPercents(vec3<float>(1.0, 1.0, 1.0), vec3<float>(0.5, 0.7, 1.0));
        float t = 0.5 * (r.get_direction().unit_vector().y() + 1.0);

        return colorPercents.lerp(t);
    }
}

int main(int argc, char* argv[])
{
    if(argc == 4)
    {
        int width = std::stoi(std::string(argv[1]));
        int height = std::stoi(std::string(argv[2]));
        std::string fullPath = std::string("./img/") + std::string(argv[3]) + std::string(".ppm");

        std::ofstream outFile(fullPath.c_str());

        if(outFile)
        {
            outFile << "P3\n" << width << " " << height << "\n255\n";

            hitable_list world;
            world.add(new spheroid(vec3<float>(4.0, 1.0, 1.0), vec3<float>(0.0, 0.0, -1.0), 0.5));
            world.add(new sphere(vec3<float>(-0.75, 0.0, -1.5), 0.25));
            world.add(new plane(vec3<float>(0.0, -1.0, -0.25), -1.0));

            camera cam;

            float effHeight = static_cast<float>(height - 1);
            float effWidth = static_cast<float>(width - 1);

            float pixelHeight = 1.0 / effHeight;
            float pixelWidth = 1.0 / effWidth;

            for(float row = 0.0; row <= effHeight; row++)
            {
                for(float col = 0.0; col <= effWidth; col++)
                {
                    vec3<float> pixelPercents;

                    for(float subPixRow = 0.0; subPixRow <= 1.0; subPixRow += 0.5)
                    {
                        for(float subPixCol = 0.0; subPixCol <= 1.0; subPixCol += 0.5)
                        {
                            float v = (row + subPixRow) / effWidth;
                            float u = (col + subPixCol) / effWidth;

                            ray originToDir = cam.get_ray(u, v);
                            pixelPercents += color(originToDir, world);
                        }
                    }

                    vec3<unsigned> pixel(
                            static_cast<unsigned>(pixelPercents.r() * 255.0 / 9), 
                            static_cast<unsigned>(pixelPercents.g() * 255.0 / 9),
                            static_cast<unsigned>(pixelPercents.b() * 255.0 / 9));

                    outFile << pixel << "\n"; 
                }
            }
        }

        outFile.close();
    }
    else
        std::cout << "usage: raytrace [width] [height] [desired filename...]" << std::endl; 

    return 0;
}
