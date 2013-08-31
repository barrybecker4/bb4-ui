// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.util;

import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class ColoMapTest {

    private static final double VALUES[] = {0.1, 0.9, 1.0};
    private static final Color COLORS[] = {
        new Color( 255, 0, 0, 200 ),
        new Color( 0, 255, 0, 200 ),
        new Color( 0, 0, 255, 255 ),
    };

    private static final ColorMap COLOR_MAP =
            new ColorMap( VALUES, COLORS);

    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithEmptyLists() {

        new ColorMap(new double[0], new Color[0]);
    }

    @Test
    public void testGetColor() {
        assertEquals("Unexpected color", COLORS[1], COLOR_MAP.getColor(1));
    }

    @Test
    public void testGetColorForValuesAtExtremes() {
        assertEquals("Unexpected color", COLORS[0], COLOR_MAP.getColorForValue(0.0));
        assertEquals("Unexpected color", COLORS[2], COLOR_MAP.getColorForValue(10.0));
    }

    @Test
    public void testGetColorForIntermediateValue() {
        assertEquals("Unexpected color",
                new Color(128, 128, 0, 200), COLOR_MAP.getColorForValue(0.5));
    }

    @Test
    public void testGetClosestIndexForValue() {
        assertEquals("Unexpected color", 0, COLOR_MAP.getClosestIndexForValue(0.5));
        assertEquals("Unexpected color", 1, COLOR_MAP.getClosestIndexForValue(0.7));
    }

    @Test
    public void testGetNumValues() {
        assertEquals("Unexpected color", 3, COLOR_MAP.getNumValues());
    }


}
