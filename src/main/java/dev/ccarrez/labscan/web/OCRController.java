package dev.ccarrez.labscan.web;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.ccarrez.labscan.service.OCRService;

import java.io.IOException;

@RestController
public class OCRController {

    @Autowired
    private OCRService ocrService;

    @PostMapping("/ocr/image")
    public ResponseEntity<String> ocrImage(@RequestParam("file") MultipartFile file) {
        try {
            String text = ocrService.extractTextFromImage(file);
            return ResponseEntity.ok(text);
        } catch (IOException | TesseractException e) {
            return ResponseEntity.status(500).body("Error processing image: " + e.getMessage());
        }
    }

    @PostMapping("/ocr/pdf")
    public ResponseEntity<String> ocrPdf(@RequestParam("file") MultipartFile file) {
        try {
            String text = ocrService.extractTextFromPdf(file);
            return ResponseEntity.ok(text);
        } catch (IOException | TesseractException e) {
            return ResponseEntity.status(500).body("Error processing PDF: " + e.getMessage());
        }
    }
}
