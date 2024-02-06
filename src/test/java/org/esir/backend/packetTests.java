package org.esir.backend;

import org.esir.backend.IOFormat.JSONFormat;
import org.esir.backend.Requests.packet;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class packetTests {
    private static final Logger logger = LoggerFactory.getLogger(JSONFormat.class);

    @Test
    public void testToJSONObject() {
        try {
            JSONFormat jsonFormat = new JSONFormat("default");
            packet packet = new packet("Type", 1, 1, new JSONObject());
            packet.toJSONObject();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            fail();
        }
    }

}
