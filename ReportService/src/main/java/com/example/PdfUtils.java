package com.example;

import com.example.model.model.Driver;

import com.example.model.model.DriverRecord;
import com.example.model.model.Manager;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.util.List;
import java.util.Map;

public class PdfUtils {

    private PdfUtils(){}

    // --------------------------- Table Helper Methods -------------------------------------

    static Cell createDataCell(String content, boolean isFuelConsumption, boolean isEvenRow) {
        Cell cell = new Cell()
                .add(new Paragraph(content)
                        .setFontSize(9)
                        .setFontColor(ColorConstants.BLACK))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5)
                .setBorder(Border.NO_BORDER);

        // Apply background color
        if (isFuelConsumption) {
            // Highlight fuel consumption cells with a specific color
            cell.setBackgroundColor(ColorPalette.LIGHT_RED); // Light orange
        } else {
            // Alternating row colors for non-highlighted cells
            if (isEvenRow) {
                cell.setBackgroundColor(ColorPalette.LIGHT_GRAY_FOR_EVEN_ROWS); // Light gray for even rows
            } else {
                cell.setBackgroundColor(ColorConstants.WHITE); // White for odd rows
            }
        }

        return cell;
    }

     static Cell createHeaderCell(String content) {
        return new Cell()
                .add(new Paragraph(content)
                        .setBold()
                        .setFontSize(10)
                        .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ColorPalette.HEADER_BACKGROUND) // Custom header color
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5)
                .setBorder(Border.NO_BORDER);
    }

    static Table getTable(Map<String, Double> weeklyData, Table table) {
        final int[] rowIndex = {0};
        weeklyData.forEach((week, avgConsumption) -> {
            boolean isEvenRow = rowIndex[0] % 2 == 0;
            table.addCell(createDataCell(week, false, isEvenRow)); // Week name
            table.addCell(createDataCell(categorizeFuelConsumption(avgConsumption), false, isEvenRow)); // Fuel consumption category
            rowIndex[0]++;
        });

        return table;
    }

    // --------------------------- Table Helper Methods End -------------------------------------


    // --------------------------- Header Helper Methods -------------------------------------
     static void addSectionHeader(Document document, String title) {
        document.add(new Paragraph(title)
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(12)
                .setBold()
                .setFontColor(ColorConstants.BLACK)
                .setMarginBottom(5));
        document.add(new LineSeparator(new SolidLine(1))
                .setStrokeColor(ColorConstants.YELLOW)
                .setMarginBottom(2)
                .setMarginTop(2)); // Reduced top margin for better alignment
    }

    // --------------------------- Header Helper Methods End -------------------------------------


    // --------------------------- Driver Info Helper Methods -------------------------------------
    static void addDriverInfoSection(Document document, Driver driver, Manager manager) {
        // Driver Name
        document.add(new Paragraph("Driver Name: " + driver.getUser().getFirstName() + " "
                + driver.getUser().getLastName() + " " + driver.getUser().getFamilyName())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(2));

        // Email
        document.add(new Paragraph("Email: " + driver.getUser().getEmail())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(2));

        // Phone
        document.add(new Paragraph("Phone: " + driver.getUser().getPhoneNumber())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(2));

        // Experience
        document.add(new Paragraph("Experience: " + driver.getYearsOfExperience() + " years")
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(2));

        // Manager
        document.add(new Paragraph("Manager: " + manager.getUser().getFirstName() + " "
                + manager.getUser().getLastName() + " " + manager.getUser().getFamilyName())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(10));
    }

    // --------------------------- Driver Info Helper Methods End-------------------------------------


    // --------------------------- Fuel Consumption Helper Methods -------------------------------------
    static String categorizeFuelConsumption(double averageConsumption) {
        if (averageConsumption >= 0 && averageConsumption < 10) {
            return "Excellent";
        } else if (averageConsumption >= 10 && averageConsumption <= 12) {
            return "Moderate";
        } else if (averageConsumption > 12 && averageConsumption <= 50) {
            return "Poor";
        } else {
            return "Invalid";
        }
    }


    static String getMaxKey(Map<String, Double> data) {
        return data.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    static String getMinKey(Map<String, Double> data) {
        return data.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    // Help Methods
    static String mapAttributeToKey(String attribute) {
        return switch (attribute) {
            case "Engine Speed Contribution" -> "engineSpeedContribution";
            case "Vehicle Speed Contribution" -> "vehicleSpeedContribution";
            case "Accelerator Pedal Value Contribution" -> "acceleratorPedalValueContribution";
            case "Intake Air Pressure Contribution" -> "intakeAirPressureContribution";
            case "Acceleration Speed Longitudinal Contribution" -> "accelerationSpeedLongitudinalContribution";
            case "Minimum Indicated Engine Torque Contribution" -> "minimumIndicatedEngineTorqueContribution";
            case "Indication of Brake Switch On/Off Contribution" -> "indicationOfBrakeSwitchOnOffContribution";
            case "Converter Clutch Contribution" -> "converterClutchContribution";
            case "Engine Idle Target Speed Contribution" -> "engineIdleTargetSpeedContribution";
            case "Current Spark Timing Contribution" -> "currentSparkTimingContribution";
            case "Master Cylinder Pressure Contribution" -> "masterCylinderPressureContribution";
            case "Torque of Friction Contribution" -> "torqueOfFrictionContribution";
            case "Engine in Fuel Cut-Off Contribution" -> "engineInFuelCutOffContribution";
            case "Current Gear Contribution" -> "currentGearContribution";
            case "Calculated Road Gradient Contribution" -> "calculatedRoadGradientContribution";
            case "Long Term Fuel Trim Bank1 Contribution" -> "longTermFuelTrimBank1Contribution";
            default -> "";
        };
    }




    // --------------------------- Fuel Consumption Helper Methods End -------------------------------------



    // --------------------------- Period Calculation Helper Methods -------------------------------------

    static List<DriverRecord> getLastRecords(List<DriverRecord> records, int limit) {
        if (records.size() <= limit) {
            return records;
        }
        return records.subList(records.size() - limit, records.size());
    }


}
