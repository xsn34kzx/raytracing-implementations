#include <iostream>
#include <fstream>
#include <string>

#include "vec3.hpp"

int main(int argc, char* argv[])
{
    if(argc == 4)
    {
        int width = std::stoi(std::string(argv[1]));
        int height = std::stoi(std::string(argv[2]));
        std::string fullPath = std::string("./") + std::string(argv[3]) + std::string(".ppm");

        std::ofstream outFile(fullPath.c_str());

        if(outFile)
        {
            outFile << "P3\n" << width << " " << height << "\n255\n";

            for(unsigned row = 0; row < height; row++)
            {
                for(unsigned col = 0; col < width; col++)
                {
                    vec3<unsigned> pixel(
                            unsigned((float(col) / (width - 1)) * 255.0), 
                            0, 
                            unsigned((float(row) / (height - 1)) * 255.0));

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
