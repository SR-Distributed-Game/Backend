package org.esir.backend.GameEngine;

import org.json.JSONObject;

public interface JsonSerializable {
    void updateFromJson(JSONObject json);
    JSONObject toJson();
}
