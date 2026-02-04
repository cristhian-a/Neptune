package app.neptune.struct;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

final class Vec3TableTest {

    @BeforeAll
    static void load() {
        System.load("C:\\Users\\User\\IdeaProjects\\neptune\\native\\build\\nept_vec.dll");
    }

    @Test
    void addInplace() {
        float[] ax = new float[] { 1f, 2f, 3f };
        float[] ay = new float[] { 1f, 2f, 3f };
        float[] az = new float[] { 1f, 2f, 3f };

        float[] bx = new float[] { 10f, 20f, 30f };
        float[] by = new float[] { 10f, 20f, 30f };
        float[] bz = new float[] { 10f, 20f, 30f };

        int count = ax.length;

        try (Arena arena = Arena.ofConfined()){
            MemorySegment pointerAx = arena.allocateFrom(ValueLayout.JAVA_FLOAT, ax);
            MemorySegment pointerAy = arena.allocateFrom(ValueLayout.JAVA_FLOAT, ay);
            MemorySegment pointerAz = arena.allocateFrom(ValueLayout.JAVA_FLOAT, az);

            MemorySegment pointerBx = arena.allocateFrom(ValueLayout.JAVA_FLOAT, bx);
            MemorySegment pointerBy = arena.allocateFrom(ValueLayout.JAVA_FLOAT, by);
            MemorySegment pointerBz = arena.allocateFrom(ValueLayout.JAVA_FLOAT, bz);

            Vec3Table.ADD_INPLACE.invokeExact(
                    pointerAx, pointerAy, pointerAz,
                    pointerBx, pointerBy, pointerBz,
                    count
            );

            for (long offset = 0; offset < count; offset++) {
                float aax = pointerAx.getAtIndex(ValueLayout.JAVA_FLOAT, offset);
                float aay = pointerAy.getAtIndex(ValueLayout.JAVA_FLOAT, offset);
                float aaz = pointerAz.getAtIndex(ValueLayout.JAVA_FLOAT, offset);

                IO.println("vector " + (offset + 1));
                IO.println(aax + ", " + aay + ", " + aaz);

                int index = (int) offset;
                assert aax == (ax[index] + bx[index]);
                assert aay == (ay[index] + by[index]);
                assert aaz == (az[index] + bz[index]);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void tableAddInplace() {
        float[] ax = new float[] { 1f, 2f, 3f };
        float[] ay = new float[] { 1f, 2f, 3f };
        float[] az = new float[] { 1f, 2f, 3f };

        float[] bx = new float[] { 10f, 20f, 30f };
        float[] by = new float[] { 10f, 20f, 30f };
        float[] bz = new float[] { 10f, 20f, 30f };

        int count = ax.length;

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment ptrAx = arena.allocateFrom(ValueLayout.JAVA_FLOAT, ax);
            MemorySegment ptrAy = arena.allocateFrom(ValueLayout.JAVA_FLOAT, ay);
            MemorySegment ptrAz = arena.allocateFrom(ValueLayout.JAVA_FLOAT, az);

            MemorySegment ptrBx = arena.allocateFrom(ValueLayout.JAVA_FLOAT, bx);
            MemorySegment ptrBy = arena.allocateFrom(ValueLayout.JAVA_FLOAT, by);
            MemorySegment ptrBz = arena.allocateFrom(ValueLayout.JAVA_FLOAT, bz);

            MemorySegment tableA = arena.allocate(Vec3Table.MEMORY_LAYOUT);
            MemorySegment tableB = arena.allocate(Vec3Table.MEMORY_LAYOUT);

            Vec3Table.X.set(tableA, 0L, ptrAx);
            Vec3Table.Y.set(tableA, 0L, ptrAy);
            Vec3Table.Z.set(tableA, 0L, ptrAz);

            Vec3Table.X.set(tableB, 0L, ptrBx);
            Vec3Table.Y.set(tableB, 0L, ptrBy);
            Vec3Table.Z.set(tableB, 0L, ptrBz);

            long byteSize = count * ValueLayout.JAVA_FLOAT.byteSize();

            var testAx = ((MemorySegment) Vec3Table.X.get(tableA, 0L)).reinterpret(byteSize);
            var testAy = ((MemorySegment) Vec3Table.Y.get(tableA, 0L)).reinterpret(byteSize);
            var testAz = ((MemorySegment) Vec3Table.Z.get(tableA, 0L)).reinterpret(byteSize);
            // reinterpret is necessary so the recovered pointer knows its size

            var testBx = ((MemorySegment) Vec3Table.X.get(tableB, 0L)).reinterpret(byteSize);
            var testBy = ((MemorySegment) Vec3Table.Y.get(tableB, 0L)).reinterpret(byteSize);
            var testBz = ((MemorySegment) Vec3Table.Z.get(tableB, 0L)).reinterpret(byteSize);

            Vec3Table.ADD_INPLACE.invokeExact(
                    testAx, testAy, testAz,
                    testBx, testBy, testBz,
                    count
            );

            for (long offset = 0; offset < count; offset++) {
                IO.println(offset);
                float aax = testAx.getAtIndex(ValueLayout.JAVA_FLOAT, offset);
                float aay = testAy.getAtIndex(ValueLayout.JAVA_FLOAT, offset);
                float aaz = testAz.getAtIndex(ValueLayout.JAVA_FLOAT, offset);

                IO.println("vector " + (offset + 1));
                IO.println(aax + ", " + aay + ", " + aaz);

                int index = (int) offset;
                assert aax == (ax[index] + bx[index]);
                assert aay == (ay[index] + by[index]);
                assert aaz == (az[index] + bz[index]);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
