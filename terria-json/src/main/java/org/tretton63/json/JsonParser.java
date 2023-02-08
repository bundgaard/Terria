package org.tretton63.json;

import java.io.IOException;
import java.util.ArrayList;


public class JsonParser {

    private final JsonLexer lexer;
    private JsonToken currentToken;
    private JsonToken peekToken;

    public static JsonValue.Boolean TRUE = new JsonValue.Boolean(true);
    public static JsonValue.Boolean FALSE = new JsonValue.Boolean(false);

    public static JsonValue.Null NULL = new JsonValue.Null();

    public JsonParser(String jsonString) throws IOException {
        this.lexer = new JsonLexer(jsonString);
        nextToken();
        nextToken();
    }

    public JsonValue parse() throws IOException {
        var token = currentToken;
        return switch (token.getType()) {
            case OpenArray -> parseArray();
            case OpenObject -> parseObject();
            case String -> parseString();
            case Number -> parseNumber();
            case True, False -> parseBoolean();
            case Null -> parseNull();
            default -> throw new IllegalStateException("Unexpected value: " + token.getType());
        };
    }

    private JsonValue parseString() throws IOException {
        var token = currentToken;
        nextToken();
        return new JsonValue.String(token.getValue());
    }

    private JsonValue parseNumber() throws IOException {
        var token = currentToken;
        nextToken();
        return new JsonValue.Number(Float.parseFloat(token.getValue()));
    }

    private JsonValue parseNull() throws IOException {
        nextToken();
        return NULL;
    }

    private JsonValue parseBoolean() throws IOException {
        var token = currentToken;
        nextToken();
        var result = Boolean.parseBoolean(token.getValue());
        if (result) {
            return TRUE;
        }
        return FALSE;
    }


    private JsonValue parseArray() throws IOException {
        nextToken(); // eat the [
        var items = new ArrayList<JsonValue>();
        while (currentToken.getType() != JsonToken.Type.CloseArray) {
            items.add(parse());
            if (currentToken.getType() == JsonToken.Type.Comma) {
                nextToken();
            }
        }
        nextToken(); // Eat the ]
        return new JsonValue.Array(items);
    }

    private JsonValue parseObject() throws IOException {

        var obj = new JsonValue.Dict();
        nextToken(); // eat {
        while (currentToken.getType() != JsonToken.Type.CloseObject) {
            var key = parse();
            if (currentToken.getType() != JsonToken.Type.Colon) {
                return null;
            }
            nextToken();

            var value = parse();
            obj.add(key, value);
            if (currentToken.getType() == JsonToken.Type.Comma) {
                nextToken(); // Eat comma
            }
        }
        nextToken(); // eat }
        return obj;
    }

    private void nextToken() throws IOException {
        currentToken = peekToken;
        peekToken = lexer.nextToken();
    }
}
