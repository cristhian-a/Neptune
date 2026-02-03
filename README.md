# Neptune

With MSYS2 UCRT64 Shell in the native directory, do:

gcc -Iinclude -shared -o build/nept.dll src/nept.c

This will build the dll in the build folder. You can check it performing:

nm build/nept.dll | grep add

With the dll built, the java app should be able to call the native libs.
