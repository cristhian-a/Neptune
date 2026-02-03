#include "nept.h"

int add(int a, int b) {
    return a + b;
}

void increment_all(int* data, int length) {
    for (int i = 0; i < length; i++) {
        data[i] += i;
    }
}