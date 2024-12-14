package dev.ccarrez.labscan.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class OCRService {

    @Value("${pathToTessdata}")
    private String PATH_TO_TESSDATA;

    public String extractTextFromImage(MultipartFile file) throws IOException, TesseractException {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(PATH_TO_TESSDATA);  // Update this path to your tessdata folder
        tesseract.setTessVariable("user_defined_dpi", "300");

        // Save the uploaded file to a temporary location
        Path tempFile = Files.createTempFile("tempImage", file.getOriginalFilename());
        file.transferTo(tempFile.toFile());

        // Perform OCR on the image
        String result = tesseract.doOCR(tempFile.toFile());
        Files.delete(tempFile);  // Clean up the temporary file
        return result;
    }

    public String extractTextFromPdf(MultipartFile file) throws IOException, TesseractException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder text = new StringBuilder();

        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300);
            File tempImageFile = File.createTempFile("temp_page_" + page, ".png");
            ImageIO.write(image, "png", tempImageFile);
            text.append(extractTextFromImage(tempImageFile));
            tempImageFile.delete();
        }

        document.close();
        return text.toString();
    }

    private String extractTextFromImage(File imageFile) throws TesseractException {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(PATH_TO_TESSDATA);  // Update this path to your tessdata folder
        return tesseract.doOCR(imageFile);
    }
}