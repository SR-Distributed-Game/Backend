package org.esir.backend;

import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONObject;


import org.esir.backend.IO.JSONFormat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class JSONFormatTests {

    @Test
    public void testIsFormatCorrect() {
        String path = "/resources/IOMessage/JSONMessageAll.json";
        assertTrue(true);
    }
}
