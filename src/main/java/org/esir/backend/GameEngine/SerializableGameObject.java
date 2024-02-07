package org.esir.backend.GameEngine;

import org.esir.backend.IOFormat.JSONFormat;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class SerializableGameObject {
    public JSONObject toSerialized() {

        return serializeObject(this).put("Type", this.getClass().getSimpleName());
    }

    private JSONObject serializeObject(Object obj) {
        if (obj instanceof JsonSerializable) {
            return ((JsonSerializable) obj).toJson();
        }
        JSONObject jsonObject = new JSONObject();
        Field[] fields = getAllFields(obj.getClass());

        for (Field field : fields) {
            if (field.isAnnotationPresent(Serializable.class)) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    // Check if the value is a custom object or a built-in type
                    if (isCustomObject(value)) {
                        jsonObject.put(field.getName(), serializeObject(value));
                    } else {
                        jsonObject.put(field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        // Optionally add type information

        return jsonObject;
    }

    private boolean isCustomObject(Object value) {
        return value != null && !(value instanceof String || value instanceof Number || value instanceof Boolean || value.getClass().isPrimitive());
    }

    private static Field[] getAllFields(Class<?> type) {
        if (type == null || type == Object.class) {
            return new Field[0];
        }
        return Stream.concat(Arrays.stream(type.getDeclaredFields()), Arrays.stream(getAllFields(type.getSuperclass())))
                .toArray(Field[]::new);
    }

    public void updateFromRequest(JSONObject data) {
        Field[] fields = getAllFields(this.getClass());

        for (Field field : fields) {
            if (field.isAnnotationPresent(Serializable.class) && data.has(field.getName())) {
                try {
                    field.setAccessible(true);
                    if (JsonSerializable.class.isAssignableFrom(field.getType())) {
                        JsonSerializable subObj = (JsonSerializable) field.getType().getDeclaredConstructor().newInstance();
                        subObj.updateFromJson(data.getJSONObject(field.getName()));
                        field.set(this, subObj);
                    } else {
                        field.set(this, data.get(field.getName()));
                    }
                } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean isCustomObject(Class<?> type) {
        return !type.isPrimitive() && !String.class.isAssignableFrom(type) && !Number.class.isAssignableFrom(type) && !Boolean.class.isAssignableFrom(type);
    }

    public static <T extends SerializableGameObject> T fromSerialized(Class<T> clazz, JSONObject data) {
        T instance;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = getAllFields(clazz);
            for (Field field : fields) {
                if (field.isAnnotationPresent(Serializable.class) && data.has(field.getName())) {
                    field.setAccessible(true);
                    Object value = data.get(field.getName());

                    if (JsonSerializable.class.isAssignableFrom(field.getType())) {

                        JsonSerializable subObj = field.getType().asSubclass(JsonSerializable.class).getDeclaredConstructor().newInstance();
                        subObj.updateFromJson(new JSONObject(value.toString()));
                        field.set(instance, subObj);
                    } else if (isCustomObject(field.getType())) {

                    } else {

                        field.set(instance, value);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Error deserializing object", e);
        }
        return instance;
    }



}
