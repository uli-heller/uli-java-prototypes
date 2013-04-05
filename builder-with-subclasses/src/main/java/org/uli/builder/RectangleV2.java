/**
 * http://weblogs.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses
 */
package org.uli.builder;

public class RectangleV2 extends ShapeV2 {
    private final double height;
    private final double width;
    //...
    protected static abstract class Init<T extends Init<T>> extends ShapeV2.Init<T> {
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

        public RectangleV2 build() {
            return new RectangleV2(this);
        }
    }

    public static class Builder extends Init<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }

    protected RectangleV2(Init<?> init) {
        super(init);
        this.height = init.height;
        this.width = init.width;
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
