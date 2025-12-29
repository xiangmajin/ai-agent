package com.xidian.aiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapingToolTest {

    @Test
    public void testScrapeWebPage() {
        WebScrapingTool tool = new WebScrapingTool();
        String url = "https://bgm.tv/group";
        String result = tool.scrapeWebPage(url);
        assertNotNull(result);
        System.out.println(result);
    }
}