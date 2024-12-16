package com.example;

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
//@CrossOrigin(origins = "http://localhost:4200")
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
        // Fetch driver and manager data
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        Manager manager =driver.getManager();

        // Generate the report
        byte[] pdfBytes = pdfReportService.generateDriverReport(driver, manager , groupBy);

        // Create a dynamic file name: [DriverName]_[dd-MM-yyyy].pdf
        String driverName = driver.getUser().getFirstName() + "_" + driver.getUser().getLastName() +  "_" + driver.getUser().getFamilyName();
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String fileName = driverName + "_" + currentDate + ".pdf";

        // Set Content-Disposition header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename(fileName)
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
