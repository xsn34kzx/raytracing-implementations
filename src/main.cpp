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

bool hit_sphere(const vec3<float>& center, float radius, const ray& r)
{
    vec3<float> rayOrigMinusCenter = r.get_origin() - center;
    vec3<float> rayDir = r.get_direction();

    float a = rayDir.dot(rayDir);
    float b = 2 * rayDir.dot(rayOrigMinusCenter);
    float c = rayOrigMinusCenter.dot(rayOrigMinusCenter) - radius * radius;

    float discriminant = b * b - 4 * a * c;

    return (b * b - 4 * a * c) > 0;
}

vec3<float> color(const ray& r)
{
    if(hit_sphere(vec3<float>(0.0, 0.0, -1.0), 0.5, r))
        return vec3<float>(1.0, 0.0, 0.0);

    ray colorPercents(vec3<float>(1.0, 1.0, 1.0), vec3<float>(0.5, 0.7, 1.0));
    float t = 0.5 * (r.get_direction().unit_vector().y() + 1.0);
    
    return colorPercents.lerp(t);
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

            ray originToDir;

            vec3<float> upLeftPlane(-1.0, 1.0, -1.0);
            vec3<float> deltaX(2.0, 0.0, 0.0);
            vec3<float> deltaY(0.0, -2.0, 0.0);

            float effHeight = static_cast<float>(height - 1);
            float effWidth = static_cast<float>(width - 1);

            for(float row = 0.0; row <= effHeight; row++)
            {
                float v = row / effWidth;

                for(float col = 0.0; col <= effWidth; col++)
                {
                    float u = col / effWidth;

                    originToDir.set_direction(upLeftPlane + deltaX * u + deltaY * v);

                    vec3<float> pixelPercents = color(originToDir);
                    vec3<unsigned> pixel(
                            static_cast<unsigned>(pixelPercents.r() * 255.0), 
                            static_cast<unsigned>(pixelPercents.g() * 255.0),
                            static_cast<unsigned>(pixelPercents.b() * 255.0));

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
