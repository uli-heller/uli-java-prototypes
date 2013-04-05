/**
 * http://weblogs.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses
 */
package org.uli.builder;

public class Rectangle {
    private final double opacity;
    private final double height;
    private final double width;
    //...

    public static class Builder {
        private double opacity;
        private double height;
        private double width;
        //...

        public Builder opacity(double opacity) {
            this.opacity = opacity;
            return this;
        }

        public Builder height(double height) {
            this.height = height;
            return this;
        }

        public Builder width(double width) {
            this.width = width;
            return this;
        }
        //...

        public Rectangle build() {
            return new Rectangle(this);
        }
    }

    private Rectangle(Builder builder) {
        this.opacity = builder.opacity;
        this.height = builder.height;
        this.width = builder.width;
        //...
    }

    /**
     * @return the opacity
     */
    public double getOpacity() {
        return opacity;
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
