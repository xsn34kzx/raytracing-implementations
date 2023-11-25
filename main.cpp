#include <iostream>
#include <fstream>
#include <string>

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
                   unsigned red = unsigned((float(col) / (width - 1)) * 255.0);
                   unsigned blue = unsigned((float(row) / (height - 1)) * 255.0);
                   unsigned green = 0;

                   outFile << red << " " << green << " " << " " << blue;
                   if(col != width - 1)
                    outFile << "\t";
                }

                outFile << "\n";
            }
        }

        outFile.close();
    }

    return 0;
}
