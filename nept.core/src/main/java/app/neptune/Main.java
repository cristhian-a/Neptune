package app.neptune;

final class Main {

    static void main() {
        System.load("C:\\Users\\User\\IdeaProjects\\neptune\\native\\build\\nept.dll");
        IO.println("Finished loading!");

        int result = NeptuneNative.add(9, 4);
        IO.println(result);
    }
}
