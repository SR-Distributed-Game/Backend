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

    private Field[] getAllFields(Class<?> type) {
        if (type == null || type == Object.class) {
            return new Field[0];
        }
        return Stream.concat(Arrays.stream(type.getDeclaredFields()), Arrays.stream(getAllFields(type.getSuperclass())))
                .toArray(Field[]::new);
    }

    // Update instance from JSONObject data
    public void updateFromRequest(JSONObject data) {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Serializable.class) && data.has(field.getName())) {
                try {
                    field.setAccessible(true);
                    field.set(this, data.get(field.getName()));
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Create an instance from JSONObject data
    public static <T extends SerializableGameObject> T fromSerialized(Class<T> clazz, JSONObject data) {
        T instance = null;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
            instance.updateFromRequest(data);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }
}
