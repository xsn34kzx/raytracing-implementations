#ifndef VEC_HPP
#define VEC_HPP

#include <iostream>

template <class T>
class vec3
{
    private:
        T vec[3];

    public:
        vec3()
        {
            vec[0] = vec[1] = vec[2] = 0;
        }

        vec3(T a, T b, T c)
        {
            vec[0] = a;
            vec[1] = b;
            vec[2] = c;
        }

        T r() const
        {
            return vec[0];
        }

        T g() const
        {
            return vec[1];
        }

        T b() const
        {
            return vec[2];
        }
        
        friend std::ostream& operator<<(std::ostream& lhs, const vec3& rhs)
        {
            return lhs << rhs.vec[0] << " " << rhs.vec[1] << " " << rhs.vec[2];
        }

};

#endif
