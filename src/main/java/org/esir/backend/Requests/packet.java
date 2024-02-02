package org.esir.backend.Requests;

import java.util.Map;

public interface packet {

    public String getType();

    Map<String, String> toMap();
}
