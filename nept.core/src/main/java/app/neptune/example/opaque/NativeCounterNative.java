package app.neptune.example.opaque;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.*;

final class NativeCounterNative {
    private static final Linker LINKER = Linker.nativeLinker();
    private static final SymbolLookup LOOKUP =  SymbolLookup.libraryLookup("C:\\Users\\User\\IdeaProjects\\neptune\\native\\build\\nept_count.dll", Arena.global());

    private static final MethodHandle CREATE;
    private static final MethodHandle DESTROY;
    private static final MethodHandle GET;
    private static final MethodHandle INCREMENT;

    static {
        try {
            CREATE = LINKER.downcallHandle(
                    LOOKUP.find("counter_create").orElseThrow(),
                    FunctionDescriptor.of(ADDRESS, JAVA_INT)
            );
            DESTROY = LINKER.downcallHandle(
                    LOOKUP.find("counter_destroy").orElseThrow(),
                    FunctionDescriptor.ofVoid(ADDRESS)
            );
            GET = LINKER.downcallHandle(
                    LOOKUP.find("counter_get").orElseThrow(),
                    FunctionDescriptor.of(JAVA_INT, ADDRESS)
            );
            INCREMENT = LINKER.downcallHandle(
                    LOOKUP.find("counter_increment").orElseThrow(),
                    FunctionDescriptor.ofVoid(ADDRESS)
            );
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    static long create(int initial) {
        try {
            return ((MemorySegment) CREATE.invokeExact(initial)).address();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static void destroy(long handle) {
        try {
            DESTROY.invokeExact(MemorySegment.ofAddress(handle));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static int get(long handle) {
        try {
            return (int) GET.invokeExact(MemorySegment.ofAddress(handle));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static void increment(long handle) {
        try {
            INCREMENT.invokeExact(MemorySegment.ofAddress(handle));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
