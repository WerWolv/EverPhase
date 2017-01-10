cmake_minimum_required (VERSION 2.8)

project(werwolv)

find_package(Java REQUIRED)
include(UseJava)

# Debug flag
set(CMAKE_JAVA_COMPILE_FLAGS -g)

FILE(GLOB src
	"${CMAKE_CURRENT_SOURCE_DIR}/src/com/werwolv/*/*.java"
	"${CMAKE_CURRENT_SOURCE_DIR}/src/com/werwolv/*/*/*.java"
	"${CMAKE_CURRENT_SOURCE_DIR}/src/com/werwolv/api/event/player/*.java"
)

FILE(GLOB lib
	"${CMAKE_CURRENT_SOURCE_DIR}/lib/lwjgl/*.jar"
	"${CMAKE_CURRENT_SOURCE_DIR}/lib/reflections/*.jar"
)

add_jar(werwolv
	SOURCES ${src}
	INCLUDE_JARS ${lib}
	ENTRY_POINT com.werwolv.main.Main)

# Thanks to http://stackoverflow.com/questions/18985726/cmake-to-compile-java-code
get_target_property(_jarFile werwolv JAR_FILE)
get_target_property(_classDir werwolv CLASSDIR)

message(STATUS "Output file: ${_jarFile}")
message(STATUS "Class output directory: ${_classDir}")

add_test(NAME test_out
	COMMAND ${Java_JAVA_EXECUTABLE} -cp ${_jarFile} com.werwolv.main.Main)
