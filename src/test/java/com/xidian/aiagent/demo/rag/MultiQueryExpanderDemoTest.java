package com.xidian.aiagent.demo.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MultiQueryExpanderDemoTest {
    @Resource
    private MultiQueryExpanderDemo multiQueryExpanderDemo;

    @Test
    void expandQuery() {
        List<Query> queries = multiQueryExpanderDemo.expandQuery("谁是马猴烧酒啊啊啊啊啊啊啊啊啊啊啊？");
        Assertions.assertNotNull(queries);
    }
}