package com.example;

import com.itextpdf.kernel.colors.DeviceRgb;

public class ColorPalette {
    // Header Colors
    public static final DeviceRgb HEADER_BACKGROUND = new DeviceRgb(250, 0, 63); // Red for header
    public static final DeviceRgb WHITE = new DeviceRgb(255, 255, 255); // White font for header

    // Data Cell Colors
    public static final DeviceRgb DATA_CELL_BACKGROUND = new DeviceRgb(249,251,255); // Light gray for data cells
    public static final DeviceRgb HIGHLIGHT_CELL_BACKGROUND = new DeviceRgb(243,244,247); // Highlight color for special cells
    public static final DeviceRgb LIGHT_GRAY_FOR_EVEN_ROWS = new DeviceRgb(240, 240, 240); // Light gray for even rows
    public static final DeviceRgb LIGHT_RED = new DeviceRgb(254,215,221); // Light gray for odd rows
    public static final DeviceRgb BLACK = new DeviceRgb(0, 0, 0); // Black font for data cells

    // Line Colors
    public static final DeviceRgb LINE_SEPARATOR = new DeviceRgb(255, 255, 0); // Yellow line separator

    // Title Colors
    public static final DeviceRgb TITLE_FONT = new DeviceRgb(0, 0, 0); // Black font for title
}
