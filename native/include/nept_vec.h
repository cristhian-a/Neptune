#ifndef NEPT_VEC_H
#define NEPT_VEC_H

typedef struct {
    float x;
    float y;
    float z;
} Vec3;

float vec3_length(Vec3 v);

void vec3_normalize(Vec3* v);

Vec3 vec3_add(Vec3 a, Vec3 b);

#endif