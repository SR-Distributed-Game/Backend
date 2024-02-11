package org.esir.backend.IOFormat;

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
    private String[] AuthorizedRequestType = {
            "SpawnObject",
            "DestroyObject",
            "UpdateObject",
            "JoinRoom",
            "CreateRoom",
            "ClosingRoom",
            "LeavingRoom",
            "ConnectSucces",
            "FullState"
    };

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

        JSONObject enumJson = _jsonSchema.getJSONObject("Enum");
        validateEnum(enumJson);
    }


    private static void validateFormat(JSONObject format) throws JSONException {
        checkField(format, "Type");
        checkValue(format, "Type");

        checkField(format, "ClientID");
        checkValue(format, "ClientID");

        checkField(format, "RoomID");
        checkValue(format, "RoomID");

        checkField(format, "Metadata");
    }



    private static void validateEnum(JSONObject enumJson) throws JSONException {
        checkArray(enumJson, "Type");
        checkArrayOfString(enumJson, "Type");
    }

    private static void checkField(JSONObject json, String key) throws JSONException {
        if (!json.has(key)) {
            throw new JSONException("Le champ obligatoire '" + key + "' est manquant.");
        }
    }

    private static void checkArray(JSONObject json, String key) throws JSONException {
        if (!json.has(key)) {
            throw new JSONException("Le tableau '" + key + "' est manquant");
        }

        if (!(json.get(key) instanceof JSONArray)) {
            throw new JSONException("Le champ '" + key + "' n'est pas un tableau.");
        }
    }

    private static void checkValue(JSONObject json, String key) throws JSONException {

        if (!(json.get(key) instanceof String)) {
            throw new JSONException("Le champ '" + key + "' n'est pas une chaine de caractères.");
        }
    }

    private static void checkArrayOfString(JSONObject json, String key) throws JSONException {
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

        if (!isObjectMatch(jsonObject, _jsonSchema.getJSONObject("Format"))) {
            logger.error("Error reading JSON schema file: " + "Message format is not correct");
            return false;
        }

        if (!isTypeAndEnumValid(
                jsonObject,
                _jsonSchema.getJSONObject("Format"),
                _jsonSchema.getJSONObject("Enum").getJSONArray("Type"))
        ){
            return false;
        }


        return true;
    }

    private boolean isObjectMatch(JSONObject jsonObject, JSONObject schemaObject) {
        for (String key : schemaObject.keySet()) {
            if (!jsonObject.has(key)) {
                logger.error("Error request: " + "Missing key: " + key);
                return false;
            }
        }
        return true;
    }

    private boolean isTypeAndEnumValid(JSONObject jsonObject, JSONObject schemaObject, JSONArray enumObject) {

        //check if the value of Type is in the enum
        if (!enumObject.toList().contains(jsonObject.getString("Type"))) {
            logger.error("Error request: " + "Type is not in the enum");
            return false;
        }

        try{
            jsonObject.getJSONObject("Metadata");
        } catch (JSONException e) {
            logger.error("Error request: " + "Metadata is not a JSONObject");
            return false;
        }

        for (String key : jsonObject.keySet()) {
            if (!key.equals("Type") && !key.equals("Metadata")) {
                String requiredType = schemaObject.optString(key);
                if (requiredType == null || !isTypeValid(jsonObject.get(key), requiredType)) {
                    logger.error("Error request: " + "Type of " + key + " is not correct");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isTypeValid(Object value, String type) {
        Class<?> expectedType = stringToType(type);
        return expectedType != null && expectedType.isInstance(value);
    }

    private Class<?> stringToType(String type) {
        return switch (type) {
            case "String" -> String.class;
            case "Int" -> Integer.class;
            default -> null;
        };
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

        if (!Arrays.asList(AuthorizedRequestType).contains(packet.getType())) {
            logger.error("Error request: " + "Type is not authorized");
            throw new IllegalStateException("Unexpected value: " + packet.getType());
        }

        result = packet.toJSONObject().toString();

        return result;
    }

}
