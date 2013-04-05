/**
 * http://weblogs.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses
 */
package org.uli.builder;

public class ShapeV3 {
    private final double opacity;

    public static abstract class Builder<T extends Builder<T>> {
        private double opacity;

        protected abstract T self();

        public T opacity(double opacity) {
            this.opacity = opacity;
            return self();
        }

        public ShapeV3 build() {
            return new ShapeV3(this);
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

    protected ShapeV3(Builder<?> builder) {
        this.opacity = builder.opacity;
    }

    /**
     * @return the opacity
     */
    public double getOpacity() {
        return opacity;
    }
}
