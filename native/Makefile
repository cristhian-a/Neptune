CC = gcc
CFLAGS = -Iinclude -Wall -Wextra -O2
SRC = src/nept.c
OBJ = $(SRC:.c=.o)

all: libnept.a libnept.so

libnept.a: $(OBJ)
	ar rcs $@ $^

libnept.so: $(OBJ)
	$(CC) -shared -o $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -fPIC -c $< -o $@

clean:
	rm -f src/*.o libnept.a libnept.so
