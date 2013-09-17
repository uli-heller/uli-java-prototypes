/**
 * http://weblogs.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses
 */
package org.uli.builder;

public class ShapeV2 {
    private final double opacity;

    protected static abstract class Init<T extends Init<T>> {
        private double opacity;

        protected abstract T self();

        public T opacity(double opacity) {
            this.opacity = opacity;
            return self();
        }

        public ShapeV2 build() {
            return new ShapeV2(this);
        }
    }

    public static class Builder extends Init<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }

    protected ShapeV2(Init<?> init) {
        this.opacity = init.opacity;
    }

    /**
     * @return the opacity
     */
    public double getOpacity() {
        return opacity;
    }
}
