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

cmake_minimum_required(VERSION 3.22)
project(raytrace-cpp
    LANGUAGES CXX)

set(SRC_DIR ${CMAKE_SOURCE_DIR}/src)

add_subdirectory(${SRC_DIR})

add_executable(${PROJECT_NAME} ${SRC_DIR}/main.cpp)

set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_STANDARD 11)
set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_EXTENSIONS False)

target_link_libraries(${PROJECT_NAME} PUBLIC raytrace-cpp-lib)

add_custom_command(TARGET ${PROJECT_NAME}
    COMMAND ${CMAKE_COMMAND} -E make_directory "img"
    WORKING_DIRECTORY ${CMAKE_BINARY_DIR})

add_custom_target(clean-img 
    COMMAND ${CMAKE_COMMAND} -E remove -f ./img/*.ppm
    WORKING_DIRECTORY ${CMAKE_BINARY_DIR} )


