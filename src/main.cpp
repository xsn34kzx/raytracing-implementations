#include <iostream>
#include <fstream>
#include <string>
#include <vector>

#include "vec3.hpp"

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

            float effHeight = float(height - 1);
            float effWidth = float(width - 1);

            for(float row = 0.0; row <= effHeight; row++)
            {
                float v = row / effWidth;

                for(float col = 0.0; col <= effWidth; col++)
                {
                    float u = col / effWidth;

                    vec3<unsigned> pixel(unsigned(u * 255.0), unsigned(v * 255.0), 0);

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
