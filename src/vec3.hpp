#ifndef VEC_HPP
#define VEC_HPP

#include <iostream>
#include <cmath>

template <class T>
class vec3
{
    public:
        vec3()
        {
            vec[0] = vec[1] = vec[2] = static_cast<T>(0);
        }

        vec3(T a, T b, T c)
        {
            vec[0] = a;
            vec[1] = b;
            vec[2] = c;
        }

        vec3(const vec3<T>& rhs)
        {
            vec[0] = rhs.vec[0];
            vec[1] = rhs.vec[1];
            vec[2] = rhs.vec[2];
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

        T x() const
        {
            return vec[0];
        }

        T y() const
        {
            return vec[1];
        }

        T z() const
        {
            return vec[2];
        }

        float magnitude() const
        {
            return std::sqrt(vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2]);
        }

        vec3<T> unit_vector() const
        {
            float magnitude = this->magnitude();
            return *this / magnitude; 
        }

        vec3<T> operator+(const vec3<T>& rhs) const
        {
            return vec3<T>(vec[0] + rhs.vec[0], vec[1] + rhs.vec[1], 
                    vec[2] + rhs.vec[2]);
        }

        vec3<T> operator*(T c) const
        {
            return vec3<T>(vec[0] * c, vec[1] * c, vec[2] * c);
        }

        vec3<T> operator/(T c) const
        {
            return vec3<T>(vec[0] / c, vec[1] / c, vec[2] / c);
        }
        
        friend std::ostream& operator<<(std::ostream& lhs, const vec3& rhs)
        {
            return lhs << rhs.vec[0] << " " << rhs.vec[1] << " " << rhs.vec[2];
        }

    private:
        T vec[3];
};

#endif
