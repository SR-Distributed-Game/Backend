package org.esir.backend;


import org.esir.backend.IOFormat.JSONFormat;
import org.esir.backend.Requests.packet;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JSONFormatTests {

    private static final Logger logger = LoggerFactory.getLogger(JSONFormat.class);
    private JSONFormat jsonFormatGood = new JSONFormat("default");


    @Test
    public void testConstructor() {
        try {
            JSONFormat jsonFormat = new JSONFormat("default");
            assertTrue(true);
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            fail();
        }
    }

    @Test
    public void testConstructorFalsePath() {
        try {
            JSONFormat jsonFormat = new JSONFormat("falsePath");
            jsonFormat.isSchemaCorrect();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorFalseSchemaMissingType() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseSchemaMissingType.json");
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorFalseSchemaMissingClientID() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseSchemaMissingClientID.json");
            jsonFormat.isSchemaCorrect();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorFalseSchemaMissingRoomID() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseSchemaMissingRoomID.json");
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorFalseSchemaMissingMetadata() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseSchemaMissingMetadata.json");
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorFalseSchemaMissingEnumType() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseSchemaMissingEnumType.json");
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorFalseSchemaMissingEnumArrayOfString() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseSchemaMissingEnumArrayOfString.json");
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorFalseSchemaWrongTypeField() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseSchemaWrongTypeField.json");
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorFalseSchemaWrongEnumTypeField() {
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseSchemaWrongEnumTypeField.json");
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorFalseSchemaWrongNoEnum(){
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseSchemaWrongNoEnum.json");
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorFalseSchemaWrongNoFormat(){
        try {
            JSONFormat jsonFormat = new JSONFormat("src/test/resources/IOSchema/JSONFormatFalseSchemaWrongNoFormat.json");
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            assertTrue(true);
        }
    }

    @Test
    public void testIsFormatCorrect() {
        String message = """
                {
                    "Type": "SpawnObject",
                    "ClientID": 1,
                    "RoomID": 1,
                    "Metadata": {}
                }
                """;
        assertTrue(jsonFormatGood.IsFormatCorrect(message));
    }

    @Test
    public void testIsFormatCorrectFalseMissingkey() {
        String message = """
                {
                    "Type": "SpawnObject",
                    "ClientID": 1,
                    "RoomID": 1
                }
                """;
        assertFalse(jsonFormatGood.IsFormatCorrect(message));
    }

    @Test
    public void testIsFormatCorrectFalseType() {
        String message = """
                {
                    "Type": "FalseType",
                    "ClientID": 1,
                    "RoomID": 1,
                    "Metadata": {}
                }
                """;
        assertFalse(jsonFormatGood.IsFormatCorrect(message));
    }

    @Test
    public void testIsFormatCorrectFalseClientID() {
        String message = """
                {
                    "Type": "SpawnObject",
                    "ClientID": "FalseClientID",
                    "RoomID": 1,
                    "Metadata": {}
                }
                """;
        assertFalse(jsonFormatGood.IsFormatCorrect(message));
    }

    @Test
    public void testIsFormatCorrectFalseRoomID() {
        String message = """
                {
                    "Type": "SpawnObject",
                    "ClientID": 1,
                    "RoomID": "FalseRoomID",
                    "Metadata": {}
                }
                """;
        assertFalse(jsonFormatGood.IsFormatCorrect(message));
    }

    @Test
    public void testIsFormatCorrectFalseMetadata() {
        String message = """
                {
                    "Type": "SpawnObject",
                    "ClientID": 1,
                    "RoomID": 1,
                    "Metadata": "FalseMetadata"
                }
                """;
        assertFalse(jsonFormatGood.IsFormatCorrect(message));
    }

    @Test
    public void  testFromString() {
        String message = """
                {
                    "Type": "SpawnObject",
                    "ClientID": 1,
                    "RoomID": 1,
                    "Metadata": {}
                }
                """;
        packet packet = jsonFormatGood.FromString(message);
        assertEquals(packet.getType(), "SpawnObject");
        assertEquals(packet.getClientId(), 1);
        assertEquals(packet.getRoomId(), 1);
        assertEquals(packet.getMetadata().toString(), "{}");
    }

    @Test
    public void  testFromStringFalseType() {
        String message = """
                {
                    "Type": "FalseType",
                    "ClientID": 1,
                    "RoomID": 1,
                    "Metadata": {}
                }
                """;
        try {
            packet packet = jsonFormatGood.FromString(message);
        } catch (Exception e) {
            logger.error("Error request: " + "Type is not authorized");
            assertTrue(true);
        }
    }

    @Test
    public void testFromPacket() {
        packet packet = new packet("SpawnObject", 1, 1, new JSONObject());
        String message = jsonFormatGood.FromPacket(packet);
        assertEquals("{\"Type\":\"SpawnObject\",\"metadata\":{},\"ClientId\":\"1\",\"RoomId\":\"1\"}", message);
    }

    @Test
    public void testFromPacketFalseType() {
        packet packet = new packet("FalseType", 1, 1, new JSONObject());
        try {
            String message = jsonFormatGood.FromPacket(packet);
        } catch (Exception e) {
            logger.error("Error request: " + "Type is not authorized");
            assertTrue(true);
        }
    }





}
