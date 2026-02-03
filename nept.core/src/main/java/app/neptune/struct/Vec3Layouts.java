package app.neptune.struct;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;

import static java.lang.foreign.ValueLayout.*;

public final class Vec3Layouts {

    public static final MemoryLayout VEC3 = MemoryLayout.structLayout(
            JAVA_FLOAT.withName("x"),
            JAVA_FLOAT.withName("y"),
            JAVA_FLOAT.withName("z")
    ).withName("vec3");

    public static final VarHandle X = VEC3.varHandle(MemoryLayout.PathElement.groupElement("x"));
    public static final VarHandle Y = VEC3.varHandle(MemoryLayout.PathElement.groupElement("y"));
    public static final VarHandle Z = VEC3.varHandle(MemoryLayout.PathElement.groupElement("z"));

    private static final FunctionDescriptor VEC3_LENGTH_DESC = FunctionDescriptor.of(
            JAVA_FLOAT,
            Vec3Layouts.VEC3
    );
    private static final FunctionDescriptor VEC3_NORMALIZE_DESC = FunctionDescriptor.ofVoid(
            ADDRESS
    );
    private static final FunctionDescriptor VEC3_ADD_DESC = FunctionDescriptor.of(
            Vec3Layouts.VEC3,
            Vec3Layouts.VEC3,
            Vec3Layouts.VEC3
    );

    static final Linker LINKER = Linker.nativeLinker();
    static final SymbolLookup LOOKUP = SymbolLookup.loaderLookup();

    public static final MethodHandle VEC3_LENGTH;
    public static final MethodHandle VEC3_NORMALIZE;
    public static final MethodHandle VEC3_ADD;

    static {
        try {
            VEC3_LENGTH = LINKER.downcallHandle(
                    LOOKUP.find("vec3_length").orElseThrow(),
                    VEC3_LENGTH_DESC
            );
            VEC3_NORMALIZE = LINKER.downcallHandle(
                    LOOKUP.find("vec3_normalize").orElseThrow(),
                    VEC3_NORMALIZE_DESC
            );
            VEC3_ADD = LINKER.downcallHandle(
                    LOOKUP.find("vec3_add").orElseThrow(),
                    VEC3_ADD_DESC
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
