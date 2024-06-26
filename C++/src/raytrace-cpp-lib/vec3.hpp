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

        explicit vec3(T a)
        {
            vec[0] = vec[1] = vec[2] = a;
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

        T dot(const vec3<T>& rhs) const
        {
            return vec[0] * rhs.vec[0] + vec[1] * rhs.vec[1] + vec[2] * rhs.vec[2];
        }

        T weighted_dot(const vec3<T>& rhs, const vec3<T>& weights)
        {
            return weights.vec[0] * vec[0] * rhs.vec[0]
                + weights.vec[1] * vec[1] * rhs.vec[1]
                + weights.vec[2] * vec[2] * rhs.vec[2];
        }

        vec3<T> operator+(const vec3<T>& rhs) const
        {
            return vec3<T>(vec[0] + rhs.vec[0], vec[1] + rhs.vec[1], 
                    vec[2] + rhs.vec[2]);
        }

        vec3<T> operator-(const vec3<T>& rhs) const
        {
            return vec3<T>(vec[0] - rhs.vec[0], vec[1] - rhs.vec[1], 
                    vec[2] - rhs.vec[2]);
        }

        vec3<T> operator*(T c) const
        {
            return vec3<T>(vec[0] * c, vec[1] * c, vec[2] * c);
        }

        vec3<T> operator/(T c) const
        {
            return vec3<T>(vec[0] / c, vec[1] / c, vec[2] / c);
        }

        vec3<T> operator+=(vec3<T> v)
        {
            this->vec[0] += v.vec[0];
            this->vec[1] += v.vec[1];
            this->vec[2] += v.vec[2];

            return *this;
        }
        
        friend std::ostream& operator<<(std::ostream& lhs, const vec3& rhs)
        {
            return lhs << rhs.vec[0] << " " << rhs.vec[1] << " " << rhs.vec[2];
        }

    private:
        T vec[3];
};

#endif
