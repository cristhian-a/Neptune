#ifndef NEPT_COUNT_H
#define NEPT_COUNT_H

typedef struct Counter Counter;

Counter* counter_create(int initial);
void counter_destroy(Counter* c);
int counter_get(Counter* c);
void counter_increment(Counter* c);

#endif