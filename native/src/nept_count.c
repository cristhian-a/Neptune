#include <math.h>
#include <stdlib.h>
#include "nept_count.h"

typedef struct Counter {
    int value;
} Counter;

Counter* counter_create(int initial) {
    Counter* c = malloc(sizeof(Counter));
    if (!c) return NULL;
    c->value = initial;
    return c;
}

void counter_destroy(Counter* c) {
    free(c);
}

int counter_get(Counter* c) {
    return c->value;
}

void counter_increment(Counter* c) {
    c->value++;
}