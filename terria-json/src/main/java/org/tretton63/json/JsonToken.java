package org.tretton63.json;

public class JsonToken {

    public enum Type {
        OpenArray("["),
        CloseArray("]"),
        OpenObject("{"),
        CloseObject("}"),
        Number("digit"),
        String("string"),
        True("true"),
        False("false"),
        Null("null"),
        EOF("EOF"),
        Error("Error"),
        Comma(","),
        Colon(":");

        private final String value;

        Type(String value) {
            this.value = value;
        }
    }

    private final Type type;
    private final String value;

    public JsonToken(Type type, String value) {
        this.type = type;
        this.value = value;
    }
    public JsonToken(Type type){
        this.type = type;
        this.value = type.value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "JsonToken{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
