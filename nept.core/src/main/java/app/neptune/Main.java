package app.neptune;

import app.neptune.dto.Point;

final class Main {

    static void main() {
        var point = new Point(1f, 2f);
        IO.println(point.getX());
    }
}
