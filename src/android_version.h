#define STR_HELPER(x) #x
#define STR(x) STR_HELPER(x)
#define VERSION_MAJOR 0
#define VERSION_MINOR 4
#define VERSION_PATCH 10
#define VERSION_PATCH_ORIG 10
#define CMAKE_VERSION_GITHASH "8d2e9117"
#define CMAKE_VERSION_STRING STR(VERSION_MAJOR)"."STR(VERSION_MINOR)"."STR(VERSION_PATCH)