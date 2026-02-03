#include <stdlib.h>
#include "nept.h"

struct NeptContext {
    int total_calls;
};

NeptContext* nept_create(void) {
    NeptContext* ctx = malloc(sizeof(NeptContext));
    if (!ctx) return NULL;
    
    ctx->total_calls = 0;
    return ctx;
}

void nept_destroy(NeptContext* ctx) {
    if (!ctx) return;
    free(ctx);
}

// Operations

int add(int a, int b) {
    return a + b;
}

void increment_all(int* data, int length) {
    for (int i = 0; i < length; i++) {
        data[i] += i;
    }
}

void nept_increment_all(NeptContext* ctx, int* data, int length) {
    if (!ctx || !data || length < 1) return;
    
    for (int i = 0; i < length; i++) {
        data[i]++;
    }
    
    ctx->total_calls++;
}