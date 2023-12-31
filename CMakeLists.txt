cmake_minimum_required(VERSION 3.22)
project(raytrace
    LANGUAGES CXX)

set(SRC_DIR ${CMAKE_SOURCE_DIR}/src)

add_subdirectory(${SRC_DIR})

add_executable(${PROJECT_NAME} ${SRC_DIR}/main.cpp)

set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_STANDARD 11)
set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_EXTENSIONS False)

add_custom_command(TARGET ${PROJECT_NAME}
    COMMAND ${CMAKE_COMMAND} -E make_directory "img"
    WORKING_DIRECTORY ${CMAKE_BINARY_DIR})

add_custom_target(clean-img 
    COMMAND ${CMAKE_COMMAND} -E remove -f ./img/*.ppm
    WORKING_DIRECTORY ${CMAKE_BINARY_DIR} )


