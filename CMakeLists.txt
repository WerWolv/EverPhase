cmake_minimum_required (VERSION 2.8)

project(VoxelGame)

find_package(Java REQUIRED)
include(UseJava)

FILE(GLOB src
    "${CMAKE_CURRENT_SOURCE_DIR}/src/com/werwolv/*/*.java"
    "${CMAKE_CURRENT_SOURCE_DIR}/src/com/werwolv/*/*/*.java"
)

FILE(GLOB lib
    "${CMAKE_CURRENT_SOURCE_DIR}/lib/*.jar"
)

add_jar(VoxelGame
	SOURCES ${src}
	INCLUDE_JARS ${lib})

# Thanks to http://stackoverflow.com/questions/18985726/cmake-to-compile-java-code
get_target_property(_jarFile VoxelGame JAR_FILE)
get_target_property(_classDir VoxelGame CLASSDIR)

message(STATUS "Output file: ${_jarFile}")
message(STATUS "Class output directory: ${_classDir}")

get_target_property(_jarFile VoxelGame JAR_FILE)

add_test(NAME VoxelGame_JAR
	COMMAND ${Java_JAVA_EXECUTABLE} -cp ${_jarFile} VoxelGame)
