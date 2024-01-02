#[[
    This file is a part of raytrace-cpp.
    Copyright (C) 2023-2024  Amani Hernandez

    raytrace-cpp is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
]]

set(LIB_NAME raytrace-cpp-lib)
set(INCLUDE_DIR ${PROJECT_SOURCE_DIR}/include/${LIB_NAME})
set(SRC_DIR ${PROJECT_SOURCE_DIR}/src/${LIB_NAME})

add_library(${LIB_NAME} STATIC
    ${SRC_DIR}/ray.cpp ${INCLUDE_DIR}/ray.hpp)

target_include_directories(${LIB_NAME} PUBLIC
    ${INCLUDE_DIR}/.. 
    ${SRC_DIR}/..)

