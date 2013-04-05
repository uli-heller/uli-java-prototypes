/**
 * http://weblogs.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses
 */
package org.uli.builder;

public class RectangleV4 extends ShapeV4 {
    private final double height;
    private final double width;
    //...

    public static class Builder<T extends Builder<T>> extends ShapeV4.Builder<T> {
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

        public RectangleV4 build() {
            return new RectangleV4(this);
        }
    }

    @SuppressWarnings("rawtypes")
    public static Builder<?> builder() {
        return new Builder();
    }

    protected RectangleV4(Builder<?> builder) {
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
