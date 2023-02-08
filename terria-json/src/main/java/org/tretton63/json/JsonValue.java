package org.tretton63.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class JsonValue {


    private final Type type;


    public JsonValue(Type type) {
        this.type = type;
    }

    enum Type {
        Array,
        Object,
        String,
        Number,
        Boolean,
        Null
    }

    public Type getType() {
        return type;
    }

    public boolean isArray() {
        return type == Type.Array;
    }

    public boolean isObject() {
        return type == Type.Object;
    }

    public boolean isString() {
        return type == Type.String;
    }

    public boolean isNumber() {
        return type == Type.Number;
    }

    public boolean isBoolean() {
        return type == Type.Boolean;
    }

    public boolean isNull() {
        return type == Type.Null;
    }

    public Dict asDict() {
        return (Dict)this;
    }

    public Array asArray() {
        return (Array) this;
    }

    public static class String extends JsonValue {
        private java.lang.String value;

        public String(java.lang.String value) {
            super(Type.String);
            this.value = value;
        }


        @Override
        public java.lang.String toString() {
            return "String{" +
                    "value='" + value + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            String string = (String) o;
            return Objects.equals(value, string.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    public static class Boolean extends JsonValue {
        private boolean value;

        public Boolean(boolean inValue) {
            super(Type.Boolean);
            this.value = inValue;
        }

        @Override
        public java.lang.String toString() {
            return "Boolean{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Boolean aBoolean = (Boolean) o;
            return value == aBoolean.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    public static class Number extends JsonValue {
        private final float floatValue;


        public Number(float inValue) {
            super(Type.Number);
            floatValue = inValue;
        }

        @Override
        public java.lang.String toString() {
            return "Number{" +
                    "floatValue=" + floatValue +
                    '}';
        }
    }

    public static class Array extends JsonValue {

        private final ArrayList<JsonValue> items = new ArrayList<>();

        public Array(ArrayList<JsonValue> items) {
            super(Type.Array);
            this.items.addAll(items);
        }

        @Override
        public java.lang.String toString() {
            return "Array{" +
                    "items=" + items + '}';
        }

        public int size() {
            return items.size();
        }
    }

    public static class Dict extends JsonValue {
        private final HashMap<JsonValue, JsonValue> items = new HashMap<>();
        public Dict() {
            super(Type.Object);
        }

        public void add(JsonValue key, JsonValue val) {
            items.put(key, val);
        }

        public JsonValue getValue(JsonValue key) {
            return items.get(key);
        }
        public JsonValue getValue(java.lang.String key) {
            return items.get(new String(key));
        }

        public int size() {
            return items.size();
        }

        @Override
        public java.lang.String toString() {
            return "Dict{" +
                    "items=" + items +
                    '}';
        }
    }

    public static class Null extends JsonValue {
        public Null() {
            super(Type.Null);
        }

        @Override
        public java.lang.String toString() {
            return "Null{}";
        }
    }


}
