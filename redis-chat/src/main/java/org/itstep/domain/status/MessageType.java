package org.itstep.domain.status;

public enum MessageType {
    ENTER(0, "enter"),
    EXIT(1, "exit"),
    TALK(2, "talk"),
    PROFILE_REQUEST(3, "profile");

    private final int code;
    private final String name;

    MessageType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
