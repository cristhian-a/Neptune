package app.neptune;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

final class Main {

    static void main() {
        System.load("C:\\Users\\User\\IdeaProjects\\neptune\\native\\build\\nept.dll");
        System.load("C:\\Users\\User\\IdeaProjects\\neptune\\native\\build\\nept_vec.dll");
        IO.println("Finished loading!");

        example1();
        NeptVec.call();
    }

    static void example1() {
        IO.println("\nadd");
        int result = NeptNative.add(9, 4);
        IO.println(result);

        IO.println("\nincrement_all");
        try (Arena arena = Arena.ofConfined()) {
            long length = 5L;
            MemorySegment memSegment = arena.allocate(ValueLayout.JAVA_INT, length);

            for (long offset = 0L; offset < length; offset++) {
                memSegment.setAtIndex(ValueLayout.JAVA_INT, offset, 10);
            }

            // C call
            NeptNative.INCREMENT_ALL.invokeExact(memSegment, (int) length);

            for (long offset = 0L; offset < length; offset++) {
                int val = memSegment.getAtIndex(ValueLayout.JAVA_INT, offset);
                IO.println(val);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        IO.println();
    }
}
