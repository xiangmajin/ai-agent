package com.xidian.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {

        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "恋爱大师.pdf";
        String content = "爱爱爱爱啊";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}
