package app.neptune.struct;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;

import static java.lang.foreign.ValueLayout.*;

/**
 * Memory layout, var handlers, and method handlers for a structure of arrays of Vec3.
 */
public final class Vec3Table {

    public static final MemoryLayout MEMORY_LAYOUT = MemoryLayout.structLayout(
            ADDRESS.withName("x"),
            ADDRESS.withName("y"),
            ADDRESS.withName("z")
    ).withName("vec3Table");

    public static long stride() {
        return MEMORY_LAYOUT.byteSize();
    }

    public static final VarHandle X = MEMORY_LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("x")
    ).withInvokeExactBehavior();

    public static final VarHandle Y = MEMORY_LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("y")
    ).withInvokeExactBehavior();

    public static final VarHandle Z = MEMORY_LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("z")
    ).withInvokeExactBehavior();

    private static final FunctionDescriptor ADD_INPLACE_DESC = FunctionDescriptor.ofVoid(
            ADDRESS.withName("ax"), ADDRESS.withName("ay"), ADDRESS.withName("az"),
            ADDRESS.withName("bx"), ADDRESS.withName("by"), ADDRESS.withName("bz"),
            JAVA_INT.withName("count")
    );

    private static final Linker LINKER = Linker.nativeLinker();
    private static final SymbolLookup LOOKUP = SymbolLookup.loaderLookup();

    /**
     * Example:
     *
     * <pre>{@code
     *      ADD_INPLACE.invokeExact(
     *                  float* ax, float* ay, float* az,
     *                  float* bx, float* by, float* bz,
     *                  int count
     *      );
     * }</pre>
     */
    public static final MethodHandle ADD_INPLACE;

    static {
        try {
            ADD_INPLACE = LINKER.downcallHandle(
                    LOOKUP.find("vec3_table_add_inplace").orElseThrow(),
                    ADD_INPLACE_DESC
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
