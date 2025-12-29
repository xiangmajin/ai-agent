package com.xidian.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ResourceDownloadToolTest {

    @Test
    public void testDownloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://image2url.com/r2/bucket2/images/1766744134904-376c4ae2-eaf1-4303-9d12-7ab114ef08f3.jpeg";
        String fileName = "logo.jpeg";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
        System.out.println(result);
    }
}