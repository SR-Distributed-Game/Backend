package org.esir.backend.IO;

import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONFormat implements IOFormat {
    private static final Logger logger = LoggerFactory.getLogger(JSONFormat.class);
    private String _pathToFormatFile;
    private JSONObject _jsonSchema;

    public JSONFormat() {
        this._pathToFormatFile = "src/main/resources/IOSchema/JSONFormat.json";

        try {
            InputStream inputStream = Files.newInputStream(Paths.get(_pathToFormatFile));
            JSONTokener tokener = new JSONTokener(inputStream);
            this._jsonSchema = new JSONObject(tokener);

        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            System.out.println("Error reading JSON schema file: " + e.getMessage());
        }

        try {
            isSchemaCorrect();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            System.exit(1);
        }

        logger.info("JSON schema file is correct");
    }

    @Override
    public void isSchemaCorrect() {

        String[] rootKeys = {"Format", "Enum"};
        for (String key : rootKeys) {
            if (!_jsonSchema.has(key)) {
                throw new RuntimeException("JSON schema file is not correct, missing key: " + key);
            }
            if (!(_jsonSchema.get(key) instanceof JSONObject)) {
                throw new RuntimeException("JSON schema file is not correct, " + key + " is not a JSONObject");
            }
        }


        JSONObject format = _jsonSchema.getJSONObject("Format");
        String[] formatKeys = {"Type", "ClientID", "OnWhat", "Metadata"};
        for (String key : formatKeys) {
            if (!format.has(key)) {
                throw new RuntimeException("JSON schema file is not correct, missing key in Format: " + key);
            }

            // Vérifier le type de chaque clé dans "Format"
            if ("Metadata".equals(key)) {
                if (!(format.get(key) instanceof JSONObject)) {
                    throw new RuntimeException("JSON schema file is not correct, " + key + " in Format is not a JSONObject");
                }
            } else {
                if (!(format.get(key) instanceof String)) {
                    throw new RuntimeException("JSON schema file is not correct, " + key + " in Format is not a String");
                }
            }
        }

        JSONObject Enum = _jsonSchema.getJSONObject("Enum");
        String[] enumKeys = {"Type", "OnWhat"};

        for (String key : enumKeys) {
            if (!Enum.has(key)) {
                throw new RuntimeException("JSON schema file is not correct, missing key in Enum: " + key);
            }

            if (!(Enum.get(key) instanceof JSONArray)) {
                throw new RuntimeException("JSON schema file is not correct, " + key + " in Enum is not a JSONArray");
            }
        }
    }


    @Override
    public Boolean IsFormatCorrect(String json) {
        JSONObject jsonObject = new JSONObject(json);

        String[] jsonKeys = JSONObject.getNames(jsonObject);
        String[] schemaKeys = JSONObject.getNames(_jsonSchema.getJSONObject("Format"));

        List<String> schemaKeysList = Arrays.asList(schemaKeys);

        for (String key : jsonKeys) {
            if (!schemaKeysList.contains(key)) {
                boolean isOptional = false;
                for (String schemaKey : schemaKeys) {
                    if (schemaKey.contains("(optional)") && schemaKey.startsWith(key)) {
                        isOptional = true;
                        break;
                    }
                }
                if (!isOptional) {
                    return false;
                }
            }
        }
        return true;
    }
}
