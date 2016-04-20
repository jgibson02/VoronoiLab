/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework;

import java.util.Random;

/**
 *
 * @author jhg95693
 */
public class Color extends java.awt.Color {
    
    private static int r;
    private static int g;
    private static int b;
    
    private static final Random RAND = new Random();

    public Color(int r, int g, int b, int a) {
        super(r, g, b, a);
    }
    
    public Color(int r, int g, int b) {
        super(r, g, b);
    }
       
    public Color(float r, float g, float b) {
        super(r, g, b);
    }
    
    public Color(java.awt.Color c) {
        super(c.getRed(), c.getBlue(), c.getGreen());
    }
    
    public static Color getRandomColor() {
        return new Color(RAND.nextFloat(), RAND.nextFloat(), RAND.nextFloat());
    }
    
    public static java.awt.Color invertColor(java.awt.Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int a = c.getAlpha();
        return new java.awt.Color(255 - r, 255 - g, 255 - b);
    }
    
}
