package org.esir.backend.IO;

import org.esir.backend.Requests.packet;
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
    private String[] AuthorizedRequestType = {"SpawnObject", "DestroyObject", "UpdateObject", "JoinRoom", "CreateRoom", "ClosingRoom", "LeavingRoom", "ConnectSucces"};

    public JSONFormat(String pathToFormatFile) {
        if (Objects.equals(pathToFormatFile, "default")) {
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

    @Override
    public packet FromString(String message) {
        JSONObject jsonObject = new JSONObject(message);
        String type = jsonObject.getString("Type");

        if (!Arrays.asList(AuthorizedRequestType).contains(type)) {
            logger.error("Error request: " + "Type is not authorized");
            throw new IllegalStateException("Unexpected value: " + jsonObject.get("Type"));
        }
        return getPacketFromJsonObject(jsonObject);
    }

    private packet getPacketFromJsonObject(JSONObject jsonObject){
        return new packet(
                jsonObject.getString("Type"),
                jsonObject.getInt("ClientID"),
                jsonObject.getInt("RoomID"),
                jsonObject.getJSONObject("Metadata"));
    }

    @Override
    public String FromPacket(packet packet) {
        String result = "";

        switch(packet.getType()) {
            case "SpawnObject" -> {
                logger.error("Error request: " + "SpawnObject is not implemented");
                return null;
            }
            case "DestroyObject" -> {
                logger.error("Error request: " + "DestroyObject is not implemented");
                return null;
            }
            case "UpdateObject" -> {
                logger.error("Error request: " + "UpdateObject is not implemented");
                return null;
            }
            case "JoinRoom" -> {
                result = packet.toJSONObject().toString();
            }
            case "CreateRoom" -> {
                logger.error("Error request: " + "CreateRoom is not implemented");
                return null;
            }
            case "ClosingRoom" -> {
                logger.error("Error request: " + "ClosingRoom is not implemented");
                return null;
            }
            case "LeavingRoom" -> {
                logger.error("Error request: " + "LeavingRoom is not implemented");
                return null;
            }
            case "ConnectSucces" -> {
                result = packet.toJSONObject().toString();
            }
            default -> throw new IllegalStateException("Unexpected value: " + packet.getType());
        }

        return result;
    }

    private static void validateFormat(JSONObject format) throws JSONException {
        checkField(format, "Type", true);
        checkValue(format, "Type");
        checkField(format, "ClientID", true);
        checkValue(format, "ClientID");
        checkField(format, "RoomID", true);
        checkValue(format, "RoomID");

        checkField(format, "Metadata", true);
    }



    private static void validateEnum(JSONObject enumJson) throws JSONException {
        checkArray(enumJson, "Type", true);
        checkArrayOfString(enumJson, "Type");
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
    public Boolean IsFormatCorrect(String message) {
        JSONObject jsonObject = new JSONObject(message);

        if (!_jsonSchema.has("Format")) {
            logger.error("Error reading JSON schema file: " + "Missing key: Format");
            return false;
        }

        if (!_jsonSchema.has("Enum")) {
            logger.error("Error reading JSON schema file: " + "Missing key: Enum");
            return false;
        }

        if (!isObjectMatch(jsonObject, _jsonSchema.getJSONObject("Format"))) {
            logger.error("Error reading JSON schema file: " + "Message format is not correct");
            return false;
        }

        if (!isTypeAndEnumValid(jsonObject, _jsonSchema.getJSONObject("Format"), _jsonSchema.getJSONObject("Enum"))) {
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
                    logger.error("Error reading JSON schema file: " + "Missing key: " + actualKey);
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
        // TODO: Complete this method
        return true;
    }

}
