package app.neptune;

import app.neptune.dto.Vec3;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.List;

final class Main {

    static void main() {
        System.load("C:\\Users\\User\\IdeaProjects\\neptune\\native\\build\\nept.dll");
        System.load("C:\\Users\\User\\IdeaProjects\\neptune\\native\\build\\nept_vec.dll");
        IO.println("Finished loading!");

//        example1();
        NeptVec.call();
//        addAllExample();
    }

    static void addAllExample() {
        List<Vec3> va = List.of(
                new Vec3(1f, 2f, 3f),
                new Vec3(1f, 2f, 3f),
                new Vec3(1f, 2f, 3f)
        );

        List<Vec3> vb = List.of(
                new Vec3(10f, 20f, 30f),
                new Vec3(10f, 20f, 30f),
                new Vec3(10f, 20f, 30f)
        );

        NeptVec.addAll(va, vb);
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
