/**
 * http://weblogs.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses
 */
package org.uli.builder;

public class ShapeV4 {
    private final double opacity;

    public static class Builder<T extends Builder<T>> {
        private double opacity;

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }

        public T opacity(double opacity) {
            this.opacity = opacity;
            return self();
        }

        public ShapeV4 build() {
            return new ShapeV4(this);
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static Builder<?> builder() {
        return new Builder();
    }

    protected ShapeV4(Builder<?> builder) {
        this.opacity = builder.opacity;
    }

    /**
     * @return the opacity
     */
    public double getOpacity() {
        return opacity;
    }
}
