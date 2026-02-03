package app.neptune;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public final class NeptuneNative {

    static final Linker LINKER = Linker.nativeLinker();
    static final SymbolLookup LOOKUP = SymbolLookup.loaderLookup();

    static final MethodHandle ADD;

    static {
        try {
            ADD = LINKER.downcallHandle(
                    LOOKUP.find("add").orElseThrow(),
                    FunctionDescriptor.of(
                            ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT
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
}
