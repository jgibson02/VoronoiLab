/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 *
 * @author jhg95693
 */
public class Ellipse2D extends java.awt.geom.Ellipse2D.Double {

    public static class Double extends Ellipse2D implements Serializable {

        public Double(Point p1, Point p2, Point p3) {
            // distance from side of frame
            double n = Math.pow(p2.x, 2) + Math.pow(p2.y, 2);

            // the midpoint of each line segment
            double abMidpoint = (Math.pow(p1.x, 2) + Math.pow(p1.y, 2) - n) / 2.0;
            double bcMidpoint = (n - Math.pow(p3.x, 2) - Math.pow(p3.y, 2)) / 2.0;

            // slope of line segment (x1 - x2)(y2 - y3) - (x2 - x3)(y1 - y2)
            double lineSlope = (p1.x - p2.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p2.y);

            // the slope of perpendicular bisector will be the inverse of the slope  
            // of the line segment
            double inverseSlope = 1 / lineSlope;

            double centerX = (abMidpoint * (p2.y - p3.y) - bcMidpoint * (p1.y - p2.y)) * inverseSlope;
            double centerY = (bcMidpoint * (p1.x - p2.x) - abMidpoint * (p2.x - p3.x)) * inverseSlope;
            double radius = Math.sqrt(Math.pow(p2.x - centerX, 2) + Math.pow(p2.y - centerY, 2));
            super.setFrame(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
        }

        public Double(double x, double y, double w, double h) {
            super.setFrame(x, y, w, h);
        }

        @Override
        public void setFrame(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
        }

        @Override
        public double getX() {
            return this.x;
        }

        @Override
        public double getY() {
            return this.y;
        }

        @Override
        public double getWidth() {
            return this.width;
        }

        @Override
        public double getHeight() {
            return this.height;
        }

        @Override
        public boolean isEmpty() {
            return (width <= 0.0 || height <= 0.0);
        }

        @Override
        public Rectangle2D getBounds2D() {
            return new Rectangle2D.Double(x, y, width, height);
        }
    }
}
