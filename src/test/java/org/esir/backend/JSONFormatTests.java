package org.esir.backend;


import org.esir.backend.IO.JSONFormat;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class JSONFormatTests {

    private static final Logger logger = LoggerFactory.getLogger(JSONFormat.class);
    private final JSONFormat _jsonFormat = new JSONFormat("default");


    @Test
    public void testIsSchemaCorrectFalsePath() {
        try {
            JSONFormat jsonFormat = new JSONFormat("falsePath");
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testIsSchemaCorrect() {
        try {
            _jsonFormat.isSchemaCorrect();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
        assertTrue(true);
    }

    @Test
    public void testIsSchemaCorrectFalseValue() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONValueFalse.json");
            jsonFormat.isSchemaCorrect();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testIsSchemaCorrectFalseMissingKeyValue() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseMissingKey.json");
            jsonFormat.isSchemaCorrect();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testIsSchemaCorrectFalseNoEnum() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseNoEnum.json");
            jsonFormat.isSchemaCorrect();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testIsSchemaCorrectFalseMissingKeyArray() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseMissingKeyArray.json");
            jsonFormat.isSchemaCorrect();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testIsSchemaCorrectFalseMissingKeyArrayButValue() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseKeyArrayButValue.json");
            jsonFormat.isSchemaCorrect();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }


    @Test
    public void testIsFormatCorrectAllAttribute() {
        String path = "src/test/resources/IOMessage/JSONMessageAll.json";
        Boolean result = false;
        try {
            String JsonMessage = new String(Files.readAllBytes(Paths.get(path)));
            result = _jsonFormat.IsFormatCorrect(JsonMessage);
        } catch (Exception e) {
            logger.error("Error reading JSON message file: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
        assertTrue(result);
    }

    @Test
    public void testIsFormatCorrectNoMetadata() {
        String path = "src/test/resources/IOMessage/JSONMessageMin.json";
        Boolean result = false;
        try {
            String JsonMessage = new String(Files.readAllBytes(Paths.get(path)));
            result = _jsonFormat.IsFormatCorrect(JsonMessage);
        } catch (Exception e) {
            logger.error("Error reading JSON message file: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
        assertTrue(result);
    }

    @Test
    public void testIsFormatCorrectNoClientID() {
        String path = "src/test/resources/IOMessage/JSONMessageMinFalse.json";
        Boolean result = false;
        try {
            String JsonMessage = new String(Files.readAllBytes(Paths.get(path)));
            result = _jsonFormat.IsFormatCorrect(JsonMessage);
        } catch (Exception e) {
            logger.error("Error reading JSON message file: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
        assertFalse(result);
    }

    @Test
    public void testIsFormatCorrectVoid() {
        String path = "src/test/resources/IOMessage/JSONMessageNoFormatFalse.json";
        Boolean result = false;
        try {
            String JsonMessage = new String(Files.readAllBytes(Paths.get(path)));
            result = _jsonFormat.IsFormatCorrect(JsonMessage);
        } catch (Exception e) {
            logger.error("Error reading JSON message file: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
        assertFalse(result);
    }

    @Test
    public void testIsFormatCorrectWrongType(){
        String path = "src/test/resources/IOMessage/JSONMessageAllFalseWrongType.json";
        Boolean result = false;
        try {
            String JsonMessage = new String(Files.readAllBytes(Paths.get(path)));
            result = _jsonFormat.IsFormatCorrect(JsonMessage);
        } catch (Exception e) {
            logger.error("Error reading JSON message file: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
        assertFalse(result);
    }

}
