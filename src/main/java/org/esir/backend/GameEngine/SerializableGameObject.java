package org.esir.backend.GameEngine;

import org.json.JSONObject;
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
                        field.set(this, data.get(field.getName())); // Handle primitives and Strings directly
                    }
                } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean isCustomObject(Class<?> type) {
        // This is a simplistic check; you might need to extend this to handle your specific logic
        return !type.isPrimitive() && !String.class.isAssignableFrom(type) && !Number.class.isAssignableFrom(type) && !Boolean.class.isAssignableFrom(type);
    }

    public static <T extends SerializableGameObject> T fromSerialized(Class<T> clazz, JSONObject data) {
        T instance = null;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = getAllFields(clazz);

            for (Field field : fields) {
                if (field.isAnnotationPresent(Serializable.class) && data.has(field.getName())) {
                    Object value = data.get(field.getName());
                    field.setAccessible(true);

                    // Determine if the field is a custom object and needs special handling
                    if (isCustomObject(field.getType())) {
                        // For custom objects, recursively deserialize
                        JSONObject subData = data.getJSONObject(field.getName());
                        Object subInstance = fromSerialized(field.getType().asSubclass(SerializableGameObject.class), subData);
                        field.set(instance, subInstance);
                    } else {
                        // For simple fields, just set the value directly
                        field.set(instance, value);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }



}
