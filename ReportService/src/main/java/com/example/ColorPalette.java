package com.example;

import com.itextpdf.kernel.colors.DeviceRgb;

public class ColorPalette {

    private ColorPalette() {}
    // Header Colors
    public static final DeviceRgb HEADER_BACKGROUND = new DeviceRgb(250, 0, 63); // Red for header

    // Data Cell Colors
    public static final DeviceRgb LIGHT_GRAY_FOR_EVEN_ROWS = new DeviceRgb(240, 240, 240); // Light gray for even rows
    public static final DeviceRgb LIGHT_RED = new DeviceRgb(254,215,221); // Light gray for odd rows

}
