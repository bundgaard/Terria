package org.tretton63.json;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import static org.tretton63.json.JsonToken.Type.Colon;
import static org.tretton63.json.JsonToken.Type.Comma;

public class JsonLexer {

    private final PushbackReader reader;
    private int currentChar;
    private boolean peeking;
    private int peekChar;

    public JsonLexer(String jsonString) {
        this.reader = new PushbackReader(new StringReader(jsonString));
    }

    public JsonToken nextToken() throws IOException {
        currentChar = readChar();
        skipWhitespace();
        return switch (currentChar) {
            case ':' -> readColon();
            case ',' -> readComma();
            case 't' -> readTrue();
            case 'f' -> readFalse();
            case 'n' -> readNull();
            case '{' -> new JsonToken(JsonToken.Type.OpenObject, "{");
            case '}' -> new JsonToken(JsonToken.Type.CloseObject, "}");
            case '[' -> new JsonToken(JsonToken.Type.OpenArray, "[");
            case ']' -> new JsonToken(JsonToken.Type.CloseArray, "]");
            case '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> readNumber();
            case '"' -> readString();
            case -1 -> new JsonToken(JsonToken.Type.EOF, "<EOF>");
            default -> throw new IllegalStateException("was not expecting '" + (char) currentChar+"'");
        };
    }

    private JsonToken readColon() {
        return new JsonToken(Colon);
    }

    private JsonToken readComma() {
        return new JsonToken(Comma);
    }

    private JsonToken readTrue() throws IOException {
        skipWhitespace();
        currentChar = readChar(); // eat T
        if (currentChar != 'r') {
            return null;
        }
        currentChar = readChar();
        if (currentChar != 'u') {
            return null;
        }
        currentChar = readChar();
        if (currentChar != 'e') {
            return null;
        }
        consumeChar('e');
        return new JsonToken(JsonToken.Type.True);
    }

    private void consumeChar(char r) throws IOException {
        if (currentChar == r) {
            currentChar = readChar();
        }
        reader.unread(currentChar);
    }

    private JsonToken readFalse() throws IOException {
        skipWhitespace();
        currentChar = readChar();
        if (currentChar != 'a') {
            return null;
        }
        currentChar = readChar();
        if (currentChar != 'l') {
            return null;
        }
        currentChar = readChar();
        if (currentChar != 's') {
            return null;
        }
        currentChar = readChar();
        if (currentChar != 'e') {
            return null;
        }
        consumeChar('e');
        return new JsonToken(JsonToken.Type.False);
    }

    private JsonToken readNull() throws IOException {
        skipWhitespace();
        currentChar = readChar();
        if (currentChar != 'u') {
            return null;
        }
        currentChar = readChar();
        if (currentChar != 'l') {
            return null;
        }
        currentChar = readChar();
        if (currentChar != 'l') {
            return null;
        }

        consumeChar('l');
        return new JsonToken(JsonToken.Type.Null);
    }


    private void skipWhitespace() throws IOException {
        while (Character.isSpaceChar(currentChar)) {
            currentChar = this.reader.read();
        }

    }

    private int readChar() throws IOException {

        return reader.read();
    }

    private JsonToken readNumber() throws IOException {
        var out = new StringBuilder();

        if (currentChar == '-') {
            out.append((char) currentChar);
            currentChar = readChar();
        }


        while (Character.isDigit(currentChar) || currentChar == '.') {
            out.append((char) currentChar);

            currentChar = readChar();
        }
        reader.unread((char) currentChar);

        return new JsonToken(JsonToken.Type.Number, out.toString());
    }

    private JsonToken readString() throws IOException {
        currentChar = readChar(); // "
        var out = new StringBuilder();

        while (currentChar != '"') { // TODO implement escape logic
            out.append((char) currentChar);
            currentChar = readChar();
        }

        return new JsonToken(JsonToken.Type.String, out.toString());
    }
}
