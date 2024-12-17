package com.example;

import com.example.errorhandler.DriverNotFoundException;
import com.example.errorhandler.ReportGenerationException;
import com.example.model.Repository.DriverRepository;
import com.example.model.model.Driver;
import com.example.model.model.Manager;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final PdfReportService pdfReportService;
    private final DriverRepository driverRepository;

    public ReportController(PdfReportService pdfReportService, DriverRepository driverRepository) {
        this.pdfReportService = pdfReportService;
        this.driverRepository = driverRepository;
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<byte[]> generateDriverReport(@PathVariable Long driverId, @RequestParam String groupBy) {
        try {
            // Fetch driver and manager data
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new DriverNotFoundException("Driver not found with ID: " + driverId));

            Manager manager = driver.getManager();
            if (manager == null) {
                throw new DriverNotFoundException("Manager not found for driver ID: " + driverId);
            }

            // Generate the report
            byte[] pdfBytes = pdfReportService.generateDriverReport(driver, manager, groupBy);

            // Dynamic file name
            String driverName = driver.getUser().getFirstName() + "_" +
                    driver.getUser().getLastName() + "_" +
                    driver.getUser().getFamilyName();
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String fileName = driverName + "_" + currentDate + ".pdf";

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .builder("attachment")
                    .filename(fileName)
                    .build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (DriverNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ReportGenerationException("Failed to generate driver report", e);
        }
    }
}
