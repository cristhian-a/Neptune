#ifndef NEPT_VEC_H
#define NEPT_VEC_H

typedef struct {
    float x;
    float y;
    float z;
} Vec3;

typedef struct {
    float* x;
    float* y;
    float* z;
} Vec3Table;

float vec3_length(Vec3 v);

void vec3_normalize(Vec3* v);

Vec3 vec3_add(Vec3 a, Vec3 b);

void vec3_add_all(
    const Vec3* a,
    const Vec3* b,
    Vec3* out,
    int count
);

void vec3_add_inplace(Vec3* a, const Vec3* b, int count);

void vec3_table_add_inplace(
    float* ax, float* ay, float* az,
    float* bx, float* by, float* bz,
    int count
);

#endif