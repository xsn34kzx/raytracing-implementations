# Project Description
Stemming from a Computer Graphics class assignment using Peter Shirley's
*Ray Tracing in One Weekend* (Version 1.54 of the book specifically), this repo
aims to expand the functionality and optimization of the original by rewriting
my original class submission from the ground up. In addition to rewriting the
project in its original language of choice, C++, the repo also intends to host
several different implementations written in other languages as a grounds for
learning or improving knowledge on a given language in addition to performance
and syntax comparisons.

# Usage
The C++ portion of the project uses CMake (Version 3.22 >= required), so its
Makefile can be generated normally. After doing so, it can be compiled with
``make``. 

After doing so, the program can be used to render a PPM file when provided the
desired width, height, and file name:
```
raytrace [width] [height] [desired filename...]
```

Note that ``make clean-img`` exists to clear all PPM files from the ``img``
folder generated post-build.

# Current Goal(s)
- Finish C++ rewrite with new features, improved syntax, and writing conventions
    - [ ] Add multithreading
    - [ ] Continue with the book series after "One Weekend" (OPTIONAL)
- Write Rust implementation with parity to the finished C++ implementation

# Credit
- Shirley, Peter et al. [Ray Tracing in One Weekend][raytracing-git]


[raytracing-git]: https://github.com/RayTracing/raytracing.github.io
