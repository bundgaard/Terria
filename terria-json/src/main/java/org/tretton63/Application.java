package org.tretton63;

import org.tretton63.json.JsonLexer;
import org.tretton63.json.JsonParser;
import org.tretton63.json.JsonToken;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Application {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException, IOException {

        /*

        var aformat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 44100f, Short.SIZE, 2, 16/8*2, 44100f,true);
        var ainputstrem = new AudioInputStream(null, aformat, 200);
        var clip = AudioSystem.getClip();
        clip.open(ainputstrem);
        clip.start();
        clip.drain();
        clip.close();*/

        //var jsonString = "[1, 2, 3, 4, 5]";
        var jsonString = "[{\"foo\": true, \"baz❤️\": [1, 2, 3, 4, 5, true, false, \"💫\"]}]";
        var parser = new JsonParser(jsonString);
        var obj = parser.parse();
        System.out.println(obj);

        var httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://www.reddit.com/r/pics.json?limit=26"))
                .build();
        var response = httpClient
                .sendAsync(
                        httpRequest,
                        HttpResponse.BodyHandlers.ofString(Charset.defaultCharset()));
        var responseString = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        var responseStatus = response.thenApply(HttpResponse::statusCode).get(5, TimeUnit.SECONDS);

        if (responseStatus < 200 || responseStatus >= 400) {
            System.err.printf("%d %s %s", responseStatus, "", responseString);
            return;
        }

        parser = new JsonParser(responseString);
        obj = parser.parse();
        System.out.println("=========================");
        System.out.println("model.data.children.size = " + obj.asDict().getValue("data").asDict().getValue("children").asArray().size());

    }
}
