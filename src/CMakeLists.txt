set(LIB_NAME raytrace-cpp-lib)
set(INCLUDE_DIR ${PROJECT_SOURCE_DIR}/include/${LIB_NAME})
set(SRC_DIR ${PROJECT_SOURCE_DIR}/src/${LIB_NAME})

add_library(${LIB_NAME} STATIC
    ${SRC_DIR}/ray.cpp ${INCLUDE_DIR}/ray.hpp)

target_include_directories(${LIB_NAME} PUBLIC
    ${INCLUDE_DIR}/.. 
    ${SRC_DIR}/..)

