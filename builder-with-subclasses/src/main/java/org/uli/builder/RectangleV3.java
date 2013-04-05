/**
 * http://weblogs.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses
 */
package org.uli.builder;

public class RectangleV3 extends ShapeV3 {
    private final double height;
    private final double width;
    //...

    public static abstract class Builder<T extends Builder<T>> extends ShapeV3.Builder<T> {
        private double height;
        private double width;

        public T height(double height) {
            this.height = height;
            return self();
        }

        public T width(double width) {
            this.width = width;
            return self();
        }

        public RectangleV3 build() {
            return new RectangleV3(this);
        }
    }

    private static class Builder2 extends Builder<Builder2> {
        @Override
        protected Builder2 self() {
            return this;
        }
    }

    public static Builder<?> builder() {
        return new Builder2();
    }

    protected RectangleV3(Builder<?> builder) {
        super(builder);
        this.height = builder.height;
        this.width = builder.width;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return the width
     */
    public double getWidth() {
        return width;
    }
}
