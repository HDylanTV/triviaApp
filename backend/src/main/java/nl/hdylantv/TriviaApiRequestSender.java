package nl.hdylantv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TriviaApiRequestSender {

    private String getApiUrl(QuizConfig config) {
        if (!config.isValid()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("https://opentdb.com/api.php?");

        sb.append("amount=").append(config.getAmount());

        Integer categoryId = config.getCategoryId();
        if (categoryId != null) {
            sb.append("&category=").append(categoryId);
        }
        QuizConfig.Difficulty difficulty = config.getDifficulty();
        if (difficulty != QuizConfig.Difficulty.ANY) {
            sb.append("&=difficulty=").append(difficulty.getValue());
        }
        QuizConfig.Type type = config.getType();
        if (type != QuizConfig.Type.ANY) {
            sb.append("&=type=").append(type.getValue());
        }

        return sb.toString();
    }


    public String fetch(QuizConfig config) {
        String url = getApiUrl(config);
        if (url == null) {
            return null;
        }

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .timeout(Duration.ofSeconds(10))
            .build();

        HttpResponse<String> response;
        try {
            response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            System.out.println("Could not send request");

            return null;
        }

        return response.body();
    }
}
