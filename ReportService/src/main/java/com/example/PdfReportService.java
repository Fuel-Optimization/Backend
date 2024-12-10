package com.example;
import com.example.model.Repository.DriverRecordRepository;
import com.example.model.model.Driver;

import com.example.model.model.DriverRecord;
import com.example.model.model.Manager;

import java.time.LocalDate;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;

import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import static com.example.ChartUtils.*;

@Service
public class PdfReportService {

    private final DriverRecordRepository driverRecordRepository;

    public PdfReportService(DriverRecordRepository driverRecordRepository) {
        this.driverRecordRepository = driverRecordRepository;
    }

    public byte[] generateDriverReportWithChart(Driver driver, Manager manager , String groupBy) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Add Driver Information
            addDriverInfoSection(document, driver, manager);

            // Fetch Driver Records
            List<DriverRecord> records = driverRecordRepository.findByDriverId(driver.getId());
            if (records == null || records.isEmpty()) {
                document.add(new Paragraph("No records found for this driver.")
                        .setFontColor(ColorConstants.RED));
            } else {
                if ("week".equalsIgnoreCase(groupBy)) {
                    generateWeeklySummary(document, records);
                    // Add Last 5 Records Table
                    addSectionHeader(document, "Last 5 Records of Driver Data");
                    List<DriverRecord> lastRecords = getLastRecords(records, 5);
                    Table lastRecordsTable = createLastRecordsTable(lastRecords);
                    document.add(lastRecordsTable);
                } else if ("month".equalsIgnoreCase(groupBy)) {
                    generateMonthlySummary(document, records);
                    // Add Last 5 Records Table
                    addSectionHeader(document, "Last 5 Records of Driver Data");
                    List<DriverRecord> lastRecords = getLastRecords(records, 5);
                    Table lastRecordsTable = createLastRecordsTable(lastRecords);
                    document.add(lastRecordsTable);
                } else {
                    document.add(new Paragraph("Invalid groupBy parameter. Use 'week' or 'month'.")
                            .setFontColor(ColorConstants.RED));
                }
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }

    }


    private void generateWeeklySummary(Document document, List<DriverRecord> records) {
        // Weekly Contributions
        Map<String, Map<String, Double>> weeklyContributions = calculateWeeklyContributions(records);
        Map<String, Double> weeklyFuelConsumption = groupDataByWeek(records);

        // Weekly Contributions Table
        addSectionHeader(document, "Weekly Contributions");
        document.add(createWeeklyContributionsTable(weeklyContributions, weeklyFuelConsumption));

        // Weekly Fuel Consumption Comparison Table
        addSectionHeader(document, "Weekly Fuel Consumption Comparison");
        document.add(createFuelConsumptionComparisonTableForWeeks(weeklyFuelConsumption));

        // Weekly Fuel Consumption Chart
        addSectionHeader(document, "Weekly Fuel Consumption Chart");
        byte[] weeklyChartBytes = generateFuelConsumptionByWeekChart(weeklyFuelConsumption);
        if (weeklyChartBytes != null) {
            document.add(new Image(ImageDataFactory.create(weeklyChartBytes))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER));
        }

        // Weekly Fuel Consumption Comparison Chart
        addSectionHeader(document, "Weekly Fuel Consumption Comparison Chart");
        byte[] comparisonChartBytes = generateFuelConsumptionComparisonChartForWeeks(weeklyFuelConsumption);
        if (comparisonChartBytes != null) {
            document.add(new Image(ImageDataFactory.create(comparisonChartBytes))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER));
        }
    }

    private void generateMonthlySummary(Document document, List<DriverRecord> records) {
        // Monthly Contributions
        Map<String, Map<String, Double>> monthlyContributions = calculateMonthlyContributions(records);
        Map<String, Double> monthlyFuelConsumption = groupDataByMonth(records);

        // Monthly Contributions Table
        addSectionHeader(document, "Monthly Contributions");
        document.add(createMonthlyContributionsTable(monthlyContributions, monthlyFuelConsumption));

        // Monthly Fuel Consumption Comparison Table
        addSectionHeader(document, "Monthly Fuel Consumption Comparison");
        document.add(createFuelConsumptionComparisonTableForMonths(monthlyFuelConsumption));

        addSectionHeader(document, "Monthly Fuel Consumption Chart");
        // Monthly Fuel Consumption Chart
        byte[] monthlyChartBytes = generateFuelConsumptionByMonthChart(monthlyFuelConsumption);
        if (monthlyChartBytes != null) {
            document.add(new Image(ImageDataFactory.create(monthlyChartBytes))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER));
        }

        addSectionHeader(document, "Monthly Fuel Consumption Comparison Chart");
        // Monthly Fuel Consumption Comparison Chart
        byte[] comparisonChartBytes = generateFuelConsumptionComparisonChartForMonths(monthlyFuelConsumption);
        if (comparisonChartBytes != null) {
            document.add(new Image(ImageDataFactory.create(comparisonChartBytes))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER));
        }
    }


    private Map<String, Double> groupDataByWeek(List<DriverRecord> records) {
        DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("yyyy-'W'ww");

        return records.stream()
                .collect(Collectors.groupingBy(
                        record -> {
                            LocalDate date = record.getTime().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                            return date.format(weekFormatter);
                        },
                        Collectors.averagingDouble(DriverRecord::getPredictedFuelConsumption)
                ));
    }

    private Map<String, Map<String, Double>> calculateWeeklyContributions(List<DriverRecord> records) {
        DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("yyyy-'W'ww");

        return records.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getTime().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .format(weekFormatter),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::calculateContributionsForPeriod
                        )
                ));
    }

    private Map<String, Double> calculateContributionsForPeriod(List<DriverRecord> records) {
        Map<String, Double> contributions = new HashMap<>();
        contributions.put("engineSpeedContribution", calculateAverage(records, DriverRecord::getEngineSpeedContribution));
        contributions.put("vehicleSpeedContribution", calculateAverage(records, DriverRecord::getVehicleSpeedContribution));
        contributions.put("acceleratorPedalValueContribution", calculateAverage(records, DriverRecord::getAcceleratorPedalValueContribution));
        contributions.put("intakeAirPressureContribution", calculateAverage(records, DriverRecord::getIntakeAirPressureContribution));
        contributions.put("accelerationSpeedLongitudinalContribution", calculateAverage(records, DriverRecord::getAccelerationSpeedLongitudinalContribution));
        contributions.put("minimumIndicatedEngineTorqueContribution", calculateAverage(records, DriverRecord::getMinimumIndicatedEngineTorqueContribution));
        contributions.put("indicationOfBrakeSwitchOnOffContribution", calculateAverage(records, DriverRecord::getIndicationOfBrakeSwitchOnOffContribution));
        contributions.put("converterClutchContribution", calculateAverage(records, DriverRecord::getConverterClutchContribution));
        contributions.put("engineIdleTargetSpeedContribution", calculateAverage(records, DriverRecord::getEngineIdleTargetSpeedContribution));
        contributions.put("currentSparkTimingContribution", calculateAverage(records, DriverRecord::getCurrentSparkTimingContribution));
        contributions.put("masterCylinderPressureContribution", calculateAverage(records, DriverRecord::getMasterCylinderPressureContribution));
        contributions.put("torqueOfFrictionContribution", calculateAverage(records, DriverRecord::getTorqueOfFrictionContribution));
        contributions.put("engineInFuelCutOffContribution", calculateAverage(records, DriverRecord::getEngineInFuelCutOffContribution));
        contributions.put("currentGearContribution", calculateAverage(records, DriverRecord::getCurrentGearContribution));
        contributions.put("calculatedRoadGradientContribution", calculateAverage(records, DriverRecord::getCalculatedRoadGradientContribution));
        contributions.put("longTermFuelTrimBank1Contribution", calculateAverage(records, DriverRecord::getLongTermFuelTrimBank1Contribution));
        return contributions;
    }

    private double calculateAverage(List<DriverRecord> records, ToDoubleFunction<DriverRecord> extractor) {
        return records.stream().mapToDouble(extractor).average().orElse(0.0);
    }





    // ------------------------- Contributions Table -------------------------------
    private Table createWeeklyContributionsTable(Map<String, Map<String, Double>> weeklyContributions, Map<String, Double> weeklyFuelConsumption) {
        List<String> weeks = new ArrayList<>(weeklyContributions.keySet());
        Collections.sort(weeks);

        float[] columnWidths = new float[weeks.size() + 1];
        columnWidths[0] = 3; // Attribute column width
        Arrays.fill(columnWidths, 1, columnWidths.length, 2); // Week columns width

        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        // Header row
        table.addCell(createHeaderCell("Attributes"));
        for (String week : weeks) {
            table.addCell(createHeaderCell(week));
        }

        // Data rows
        String[] attributes = {
                "Engine Speed Contribution",
                "Vehicle Speed Contribution",
                "Accelerator Pedal Value Contribution",
                "Intake Air Pressure Contribution",
                "Acceleration Speed Longitudinal Contribution",
                "Minimum Indicated Engine Torque Contribution",
                "Indication of Brake Switch On/Off Contribution",
                "Converter Clutch Contribution",
                "Engine Idle Target Speed Contribution",
                "Current Spark Timing Contribution",
                "Master Cylinder Pressure Contribution",
                "Torque of Friction Contribution",
                "Engine in Fuel Cut-Off Contribution",
                "Current Gear Contribution",
                "Calculated Road Gradient Contribution",
                "Long Term Fuel Trim Bank1 Contribution",
                "Avg Fuel Consumption"
        };

        int rowIndex = 0; // To track the row index for alternating colors

        for (String attribute : attributes) {
            boolean isFuelConsumption = attribute.equals("Avg Fuel Consumption");
            boolean isEvenRow = rowIndex % 2 == 0;

            // Add the attribute name in the first column
            table.addCell(createDataCell(attribute, false, isEvenRow));

            for (String week : weeks) {
                if (isFuelConsumption) {
                    Double avgFuel = weeklyFuelConsumption.getOrDefault(week, 0.0);
                    table.addCell(createDataCell(String.format("%.2f", avgFuel), true, isEvenRow));
                } else {
                    String attributeKey = mapAttributeToKey(attribute);
                    Double value = weeklyContributions.get(week).getOrDefault(attributeKey, 0.0);
                    table.addCell(createDataCell(String.format("%.2f", value), false, isEvenRow));
                }
            }

            rowIndex++; // Increment the row index
        }

        return table;
    }

    private Table createMonthlyContributionsTable(Map<String, Map<String, Double>> monthlyContributions, Map<String, Double> monthlyFuelConsumption) {
        List<String> months = new ArrayList<>(monthlyContributions.keySet());
        Collections.sort(months);

        float[] columnWidths = new float[months.size() + 1];
        columnWidths[0] = 3; // Attribute column width
        Arrays.fill(columnWidths, 1, columnWidths.length, 2); // Month columns width

        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        // Header row
        table.addCell(createHeaderCell("Attributes"));
        for (String month : months) {
            table.addCell(createHeaderCell(month));
        }

        // Data rows
        String[] attributes = {
                "Engine Speed Contribution",
                "Vehicle Speed Contribution",
                "Accelerator Pedal Value Contribution",
                "Intake Air Pressure Contribution",
                "Acceleration Speed Longitudinal Contribution",
                "Minimum Indicated Engine Torque Contribution",
                "Indication of Brake Switch On/Off Contribution",
                "Converter Clutch Contribution",
                "Engine Idle Target Speed Contribution",
                "Current Spark Timing Contribution",
                "Master Cylinder Pressure Contribution",
                "Torque of Friction Contribution",
                "Engine in Fuel Cut-Off Contribution",
                "Current Gear Contribution",
                "Calculated Road Gradient Contribution",
                "Long Term Fuel Trim Bank1 Contribution",
                "Avg Fuel Consumption"
        };

        int rowIndex = 0; // To track the row index for alternating colors

        for (String attribute : attributes) {
            boolean isFuelConsumption = attribute.equals("Avg Fuel Consumption");
            boolean isEvenRow = rowIndex % 2 == 0;

            // Add the attribute name in the first column
            table.addCell(createDataCell(attribute, false, isEvenRow));

            for (String month : months) {
                if (isFuelConsumption) {
                    Double avgFuel = monthlyFuelConsumption.getOrDefault(month, 0.0);
                    table.addCell(createDataCell(String.format("%.2f", avgFuel), true, isEvenRow));
                } else {
                    String attributeKey = mapAttributeToKey(attribute);
                    Double value = monthlyContributions.get(month).getOrDefault(attributeKey, 0.0);
                    table.addCell(createDataCell(String.format("%.2f", value), false, isEvenRow));
                }
            }

            rowIndex++; // Increment the row index
        }

        return table;
    }

    // ------------------------- Contributions Table End -------------------------------



    // ------------------------- Fuel Consumption Comparison Table -------------------------------
    private Table createFuelConsumptionComparisonTableForWeeks(Map<String, Double> weeklyData) {

        float[] columnWidths = {3, 3}; // Define consistent column widths
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(50))
                .setMarginTop(10) // Add top margin
                .setMarginBottom(50); // Add bottom margin
        ; // Ensure the table spans the full width

        // Add header row
        table.addCell(createHeaderCell("Week"));
        table.addCell(createHeaderCell("Fuel Consumption Category"));

        // Add data rows with alternating colors
        return getTable(weeklyData, table);
    }

    private Table getTable(Map<String, Double> weeklyData, Table table) {
        final int[] rowIndex = {0};
        weeklyData.forEach((week, avgConsumption) -> {
            boolean isEvenRow = rowIndex[0] % 2 == 0;
            table.addCell(createDataCell(week, false, isEvenRow)); // Week name
            table.addCell(createDataCell(categorizeFuelConsumption(avgConsumption), false, isEvenRow)); // Fuel consumption category
            rowIndex[0]++;
        });

        return table;
    }

    private Table createFuelConsumptionComparisonTableForMonths(Map<String, Double> monthlyData) {
        float[] columnWidths = {3, 3}; // Define consistent column widths
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(50)) .setMarginTop(10) // Add top margin
                .setMarginBottom(50); // Add bottom margin; // Ensure the table spans the full width

        // Add header row
        table.addCell(createHeaderCell("Month"));
        table.addCell(createHeaderCell("Fuel Consumption"));

        // Add data rows with alternating colors
        return getTable(monthlyData, table);
    }

    // ------------------------- Fuel Consumption Comparison Table End-------------------------------



    // Help Methods
    public static String categorizeFuelConsumption(double averageConsumption) {
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




    // ------------------------------ Last 5 Records Table --------------------------------

    // Help Methods
    private List<DriverRecord> getLastRecords(List<DriverRecord> records, int limit) {
        if (records.size() <= limit) {
            return records;
        }
        return records.subList(records.size() - limit, records.size());
    }

    private Table createLastRecordsTable(List<DriverRecord> records) {
        Table table = new Table(new float[]{3, 3, 3, 3, 3});
        table.setWidth(UnitValue.createPercentValue(100));

        // Add headers
        table.addCell(createHeaderCell("Date"));
        table.addCell(createHeaderCell("Fuel Consumption"));
        table.addCell(createHeaderCell("Vehicle Speed"));
        table.addCell(createHeaderCell("Engine Speed"));
        table.addCell(createHeaderCell("Brake Status"));

        // Add data rows
        boolean isEvenRow = false;
        for (DriverRecord record : records) {
            table.addCell(createDataCell(record.getTime().toString(), false, isEvenRow));
            table.addCell(createDataCell(String.format("%.2f", record.getPredictedFuelConsumption()), true, isEvenRow));
            table.addCell(createDataCell(String.format("%.2f", record.getVehicleSpeedContribution()), false, isEvenRow));
            table.addCell(createDataCell(String.format("%.2f", record.getEngineSpeedContribution()), false, isEvenRow));
            table.addCell(createDataCell(record.getIndicationOfBrakeSwitchOnOffContribution() > 0 ? "On" : "Off", false, isEvenRow));
            isEvenRow = !isEvenRow;
        }
        return table;
    }

    // ------------------------------ Last 5 Records Table End--------------------------------





    private void addDriverInfoSection(Document document, Driver driver, Manager manager) {
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


    // Help Methods
    private String mapAttributeToKey(String attribute) {
        switch (attribute) {
            case "Engine Speed Contribution":
                return "engineSpeedContribution";
            case "Vehicle Speed Contribution":
                return "vehicleSpeedContribution";
            case "Accelerator Pedal Value Contribution":
                return "acceleratorPedalValueContribution";
            case "Intake Air Pressure Contribution":
                return "intakeAirPressureContribution";
            case "Acceleration Speed Longitudinal Contribution":
                return "accelerationSpeedLongitudinalContribution";
            case "Minimum Indicated Engine Torque Contribution":
                return "minimumIndicatedEngineTorqueContribution";
            case "Indication of Brake Switch On/Off Contribution":
                return "indicationOfBrakeSwitchOnOffContribution";
            case "Converter Clutch Contribution":
                return "converterClutchContribution";
            case "Engine Idle Target Speed Contribution":
                return "engineIdleTargetSpeedContribution";
            case "Current Spark Timing Contribution":
                return "currentSparkTimingContribution";
            case "Master Cylinder Pressure Contribution":
                return "masterCylinderPressureContribution";
            case "Torque of Friction Contribution":
                return "torqueOfFrictionContribution";
            case "Engine in Fuel Cut-Off Contribution":
                return "engineInFuelCutOffContribution";
            case "Current Gear Contribution":
                return "currentGearContribution";
            case "Calculated Road Gradient Contribution":
                return "calculatedRoadGradientContribution";
            case "Long Term Fuel Trim Bank1 Contribution":
                return "longTermFuelTrimBank1Contribution";
            default:
                return "";
        }
    }



    // Help Methods
    private void addSectionHeader(Document document, String title) {
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


    // Help Methods
    private Map<String, Map<String, Double>> calculateMonthlyContributions(List<DriverRecord> records) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        return records.stream()
                .collect(Collectors.groupingBy(
                        record -> {
                            LocalDate date = record.getTime().toInstant()
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDate();
                            return date.format(monthFormatter);
                        },
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                recordList -> {
                                    Map<String, Double> contributions = new HashMap<>();
                                    contributions.put("engineSpeedContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getEngineSpeedContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("vehicleSpeedContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getVehicleSpeedContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("acceleratorPedalValueContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getAcceleratorPedalValueContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("intakeAirPressureContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getIntakeAirPressureContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("accelerationSpeedLongitudinalContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getAccelerationSpeedLongitudinalContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("minimumIndicatedEngineTorqueContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getMinimumIndicatedEngineTorqueContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("indicationOfBrakeSwitchOnOffContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getIndicationOfBrakeSwitchOnOffContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("converterClutchContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getConverterClutchContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("engineIdleTargetSpeedContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getEngineIdleTargetSpeedContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("currentSparkTimingContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getCurrentSparkTimingContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("masterCylinderPressureContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getMasterCylinderPressureContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("torqueOfFrictionContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getTorqueOfFrictionContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("engineInFuelCutOffContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getEngineInFuelCutOffContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("currentGearContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getCurrentGearContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("calculatedRoadGradientContribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getCalculatedRoadGradientContribution)
                                            .average()
                                            .orElse(0.0));
                                    contributions.put("longTermFuelTrimBank1Contribution", recordList.stream()
                                            .mapToDouble(DriverRecord::getLongTermFuelTrimBank1Contribution)
                                            .average()
                                            .orElse(0.0));
                                    return contributions;
                                }
                        )
                ));
    }



    // Help Methods
    private Map<String, Double> groupDataByMonth(List<DriverRecord> records) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        return records.stream()
                .collect(Collectors.groupingBy(
                        record -> {
                            LocalDate date = record.getTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                            return date.format(monthFormatter);
                        },
                        Collectors.averagingDouble(DriverRecord::getPredictedFuelConsumption)
                ));
    }


    // Help Methods
    private Cell createHeaderCell(String content) {
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


    // Help Methods
    private Cell createDataCell(String content, boolean isFuelConsumption, boolean isEvenRow) {
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


}
