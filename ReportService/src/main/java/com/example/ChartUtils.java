package com.example;

import org.knowm.xchart.*;


import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.PdfUtils.categorizeFuelConsumption;import static com.example.PdfUtils.categorizeFuelConsumption;


public class ChartUtils {


    public static byte[] generateFuelConsumptionByMonthChart(Map<String, Double> monthlyData) {
        try {
            // Convert month strings to Date objects for the X-axis
            SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
            List<Date> months = new ArrayList<>();
            List<Double> consumptions = new ArrayList<>();

            for (Map.Entry<String, Double> entry : monthlyData.entrySet()) {
                months.add(sdf.parse(entry.getKey())); // Parse the month string into Date
                consumptions.add(entry.getValue());    // Add the corresponding consumption
            }

            // Sort the data by date
            Collections.sort(months);
            Collections.sort(consumptions);

            // Create an XY chart
            XYChart chart = new XYChartBuilder()
                    .width(800)
                    .height(600)
                    .title("Fuel Consumption Over Months")
                    .xAxisTitle("Month")
                    .yAxisTitle("Fuel Consumption (Liters)")
                    .build();

            // Add series to the chart
            chart.addSeries("Fuel Consumption", months, consumptions);

            // Style the chart
            chart.getStyler().setLegendVisible(true);
            chart.getStyler().setChartBackgroundColor(Color.WHITE);
            chart.getStyler().setXAxisLabelRotation(45);
            chart.getStyler().setPlotGridLinesVisible(false);
            chart.getStyler().setDatePattern("MMM yyyy"); // Format the date on the X-axis
            chart.getStyler().setSeriesColors(new Color[]{new Color(250, 0, 63)});

            // Export chart to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating chart", e);
        }
    }

    public static byte[] generateFuelConsumptionByWeekChart(Map<String, Double> weeklyData) {
        try {
            // Convert week strings to Date objects for the X-axis
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-'W'ww");
            List<Date> weeks = new ArrayList<>();
            List<Double> consumptions = new ArrayList<>();

            for (Map.Entry<String, Double> entry : weeklyData.entrySet()) {
                weeks.add(sdf.parse(entry.getKey())); // Parse the week string into Date
                consumptions.add(entry.getValue());  // Add the corresponding consumption
            }

            // Sort the data by date
            List<Date> sortedWeeks = new ArrayList<>(weeks);
            Collections.sort(sortedWeeks);

            List<Double> sortedConsumptions = sortedWeeks.stream()
                    .map(weeks::indexOf)
                    .map(consumptions::get)
                    .collect(Collectors.toList());

            // Create an XY chart
            XYChart chart = new XYChartBuilder()
                    .width(800)
                    .height(600)
                    .title("Fuel Consumption Over Weeks")
                    .xAxisTitle("Week")
                    .yAxisTitle("Fuel Consumption (Liters)")
                    .build();

            // Add series to the chart
            chart.addSeries("Fuel Consumption", sortedWeeks, sortedConsumptions);

            // Style the chart
            chart.getStyler().setLegendVisible(true);
            chart.getStyler().setChartBackgroundColor(Color.WHITE);
            chart.getStyler().setXAxisLabelRotation(45);
            chart.getStyler().setPlotGridLinesVisible(false);
            chart.getStyler().setDatePattern("yyyy-'W'ww"); // Format the date on the X-axis
            chart.getStyler().setSeriesColors(new Color[]{new Color(250, 0, 63)}); // Red line

            // Export chart to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating weekly fuel consumption chart", e);
        }
    }

    public static byte[] generateFuelConsumptionComparisonChartForWeeks(Map<String, Double> weeklyData) {
        try {
            // Prepare data
            List<String> weeks = new ArrayList<>(weeklyData.keySet());
            List<Double> consumptions = new ArrayList<>(weeklyData.values());

            // Create a bar chart
            CategoryChart chart = new CategoryChartBuilder()
                    .width(800)
                    .height(600)
                    .title("Weekly Fuel Consumption Comparison")
                    .xAxisTitle("Week")
                    .yAxisTitle("Fuel Consumption (Liters)")
                    .build();

            // Add data series
            chart.addSeries("Fuel Consumption", weeks, consumptions);

            // Style the chart
            chart.getStyler().setLegendVisible(true);
            chart.getStyler().setChartBackgroundColor(Color.WHITE);
            chart.getStyler().setSeriesColors(new Color[]{new Color(254,215,221)});
            chart.getStyler().setXAxisLabelRotation(45); // Rotate x-axis labels to 45 degrees

            // Export chart to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating weekly fuel consumption comparison chart", e);
        }
    }

    public static byte[] generateFuelConsumptionComparisonChartForMonths(Map<String, Double> monthlyData) {
        try {
            // Data for chart
            List<String> months = new ArrayList<>(monthlyData.keySet());
            List<Double> consumptions = new ArrayList<>(monthlyData.values());
            List<String> categories = consumptions.stream()
                    .map(consumption -> categorizeFuelConsumption(consumption))
                    .collect(Collectors.toList());

            // Create a bar chart
            CategoryChart chart = new CategoryChartBuilder()
                    .width(800)
                    .height(600)
                    .title("Fuel Consumption Comparison")
                    .xAxisTitle("Month")
                    .yAxisTitle("Fuel Consumption (Liters)")
                    .build();

            // Add data series
            chart.addSeries("Fuel Consumption", months, consumptions);

            // Style chart
            chart.getStyler().setLegendVisible(true);
            chart.getStyler().setChartBackgroundColor(java.awt.Color.WHITE);
            chart.getStyler().setSeriesColors(new java.awt.Color[]{new Color(254,215,221)});
            chart.getStyler().setXAxisLabelRotation(45);// Light red for consumption

            // Convert to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating fuel consumption chart", e);
        }
    }



}
