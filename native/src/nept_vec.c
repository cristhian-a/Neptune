#include <math.h>
#include "nept_vec.h"

float vec3_length(Vec3 v) {
    return sqrtf(v.x * v.x + v.y * v.y + v.z * v.z);
}

void vec3_normalize(Vec3* v) {
    float len = vec3_length(*v);
    if (len == 0.0f) return;

    v->x /= len;
    v->y /= len;
    v->z /= len;
}

Vec3 vec3_add(Vec3 a, Vec3 b) {
    Vec3 r;
    r.x = a.x + b.x;
    r.y = a.y + b.y;
    r.z = a.z + b.z;
    return r;
}

void vec3_add_all(const Vec3* a, const Vec3* b, Vec3* out, int count) {
    for (int i = 0; i < count; i++) {
        out[i].x = a[i].x + b[i].x;
        out[i].y = a[i].y + b[i].y;
        out[i].z = a[i].z + b[i].z;
    }
    
}