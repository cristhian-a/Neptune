// #ifndef NEPT_H
// #define NEPT_H

// #ifdef __cplusplus
// extern "C" {
//     #endif

//     int add(int a, int b);
//     void increment_all(int* data, int length);

//     #ifdef __cplusplus
// }
// #endif

// #endif

#ifndef NEPT_H
#define NEPT_H

#ifdef _WIN32
  #define NEPT_API __declspec(dllexport)
#else
  #define NEPT_API
#endif

NEPT_API int add(int a, int b);
NEPT_API void increment_all(int* data, int length);

#endif