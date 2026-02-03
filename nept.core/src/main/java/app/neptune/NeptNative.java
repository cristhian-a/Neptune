package app.neptune;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.*;

public final class NeptNative {

    static final Linker LINKER = Linker.nativeLinker();
    static final SymbolLookup LOOKUP = SymbolLookup.loaderLookup();

    static final MethodHandle ADD;
    static final MethodHandle INCREMENT_ALL;

    static {
        try {
            ADD = LINKER.downcallHandle(
                    LOOKUP.find("add").orElseThrow(),
                    FunctionDescriptor.of(
                            JAVA_INT,
                            JAVA_INT,
                            JAVA_INT
                    )
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        try {
            INCREMENT_ALL = LINKER.downcallHandle(
                    LOOKUP.find("increment_all").orElseThrow(),
                    FunctionDescriptor.ofVoid(
                            ADDRESS,
                            JAVA_INT
                    )
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static int add(int a, int b) {
        try {
            return (int) ADD.invokeExact(a, b);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private NeptNative() {}
}
