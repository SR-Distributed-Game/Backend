package org.esir.backend.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONFormat implements IOFormat {
    private static final Logger logger = LoggerFactory.getLogger(JSONFormat.class);
    private String _pathToFormatFile;
    private JSONObject _jsonSchema;

    public JSONFormat(String pathToFormatFile) {
        if (Objects.equals(pathToFormatFile, "deflaut")) {
            this._pathToFormatFile = "src/main/resources/IOSchema/JSONFormat.json";
        } else {
            this._pathToFormatFile = pathToFormatFile;
        }

        try {
            InputStream inputStream = Files.newInputStream(Paths.get(_pathToFormatFile));
            JSONTokener tokener = new JSONTokener(inputStream);
            this._jsonSchema = new JSONObject(tokener);

        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            System.out.println("Error reading JSON schema file: " + e.getMessage());
            throw new RuntimeException(e);
        }

        try {
            isSchemaCorrect();
        } catch (Exception e) {
            logger.error("Error reading JSON schema file: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }

        logger.info("JSON schema file is correct");
    }

    @Override
    public void isSchemaCorrect() throws JSONException {
        JSONObject format = _jsonSchema.getJSONObject("Format");
        validateFormat(format);

        // Valider la structure "Enum"
        JSONObject enumJson = _jsonSchema.getJSONObject("Enum");
        validateEnum(enumJson);
    }

    private static void validateFormat(JSONObject format) throws JSONException {
        checkField(format, "Type", true);
        checkValue(format, "Type");
        checkField(format, "ClientID", true);
        checkValue(format, "ClientID");
        checkField(format, "OnWhat", true);
        checkValue(format, "OnWhat");

        if (format.has("Metadata")) {
            JSONObject metadata = format.getJSONObject("Metadata");
            validateMetadata(metadata);
        }
    }

    private static void validateMetadata(JSONObject metadata) throws JSONException {
        checkField(metadata, "color (optional)", false);
        if (metadata.has("position (optional)")) {
            JSONObject position = metadata.getJSONObject("position (optional)");
            checkField(position, "x", true);
            checkValue(position, "x");
            checkField(position, "y", true);
            checkValue(position, "y");
        }
    }

    private static void validateEnum(JSONObject enumJson) throws JSONException {
        checkArray(enumJson, "Type", true);
        checkArrayOfString(enumJson, "Type");
        checkArray(enumJson, "OnWhat", true);
        checkArrayOfString(enumJson, "OnWhat");
    }

    private static void checkField(JSONObject json, String key, boolean required) throws JSONException {
        if (!json.has(key)) {
            throw new JSONException("Le champ obligatoire '" + key + "' est manquant.");
        }
        if (key.contains("(optional)") && required) {
            throw new JSONException("Le champ '" + key + "' ne peut pas être obligatoire.");
        }
    }

    private static void checkArray(JSONObject json, String key, boolean required) throws JSONException {
        if (!json.has(key)) {
            throw new JSONException("Le tableau '" + key + "' est manquant");
        }

        if (!(json.get(key) instanceof JSONArray)) {
            throw new JSONException("Le champ '" + key + "' n'est pas un tableau.");
        }

        if (key.contains("(optional)") && required) {
            throw new JSONException("Le tableau '" + key + "' ne peut pas être obligatoire.");
        }
    }

    private static void checkValue(JSONObject json, String key) throws JSONException {
        if (!json.has(key)) {
            throw new JSONException("Le champ '" + key + "' est manquant");
        }

        if (!(json.get(key) instanceof String)) {
            throw new JSONException("Le champ '" + key + "' n'est pas une chaine de caractères.");
        }
    }

    private static void checkArrayOfString(JSONObject json, String key) throws JSONException {
        if (!json.has(key)) {
            throw new JSONException("Le champ '" + key + "' est manquant");
        }

        if (!(json.get(key) instanceof JSONArray)) {
            throw new JSONException("Le champ '" + key + "' n'est pas un tableau.");
        }

        JSONArray array = json.getJSONArray(key);
        for (int i = 0; i < array.length(); i++) {
            if (!(array.get(i) instanceof String)) {
                throw new JSONException("Le champ '" + key + "' n'est pas un tableau de chaine de caractères.");
            }
        }
    }


    @Override
    public Boolean IsFormatCorrect(String json) {
        JSONObject jsonObject = new JSONObject(json);

        if(!jsonObject.has("Format")) {
            return false;
        }

        if (!_jsonSchema.has("Format")) {
            return false;
        }

        if (!_jsonSchema.has("Enum")) {
            return false;
        }

        if(!isObjectMatch(jsonObject.getJSONObject("Format"), _jsonSchema.getJSONObject("Format"))) {
            return false;
        }

        if(!isTypeAndEnumValid(jsonObject.getJSONObject("Format"), _jsonSchema.getJSONObject("Format"), _jsonSchema.getJSONObject("Enum"))) {
            return false;
        }


        return true;
    }

    private boolean isObjectMatch(JSONObject jsonObject, JSONObject schemaObject) {
        // Convert schema keys to a list for easier manipulation
        List<String> schemaKeysList = new ArrayList<>(schemaObject.keySet());

        // Iterate through all keys in the schema
        for (String schemaKey : schemaKeysList) {
            // Handle optional keys
            boolean isOptional = schemaKey.contains("(optional)");
            String actualKey = isOptional ? schemaKey.split("\\s")[0] : schemaKey;

            // Check presence of key
            if (!jsonObject.has(actualKey)) {
                if (!isOptional) {
                    // Key is missing and it's not optional
                    System.out.println("Missing key: " + actualKey);
                    return false;
                }
            } else {
                // If the key is present, and it's an object, we need to recursively verify its structure
                if (schemaObject.get(schemaKey) instanceof JSONObject) {
                    if (!isObjectMatch(jsonObject.getJSONObject(actualKey), schemaObject.getJSONObject(schemaKey))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isTypeAndEnumValid(JSONObject jsonObject, JSONObject schemaObject, JSONObject enumObject) {
          return true;
    }

}
