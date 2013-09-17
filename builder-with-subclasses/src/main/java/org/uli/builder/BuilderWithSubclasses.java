// http://weblogs.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses#comment-817860
package org.uli.builder;

import java.awt.Color;

public class BuilderWithSubclasses {

    public static void main(final String[] notUsed) {
        final Square s = new Square.Builder() {
            {
                color = Color.BLUE;
                size = 2;
            }
        }.build();
        System.out.println(s);
    }

    static class Shape {

        protected final Color color;

        protected Shape(final Builder values) {
            color = values.color;
        }

        public static class Builder {

            public Color color = Color.BLACK;

            public Shape build() {
                return new Shape(this);
            }
        }

        public String toString() {
            return "[" + color + "]";
        }
    }

    static class Square extends Shape {

        protected final int size;

        protected Square(final Builder values) {
            super(values);
            size = values.size;
        }

        public static class Builder extends Shape.Builder {

            public int size = 1;

            public Square build() {
                return new Square(this);
            }
        }

        public String toString() {
            return "[" + color + "," + size + "]";
        }
    }
}
