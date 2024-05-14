package br.coinConverter.api;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiConnection{
    private String apiKey;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey(){
        return apiKey;
    }

    public ApiConnection(String apiKey) {
        setApiKey(apiKey);
    }

    public ApiResponse connect(String base, String target){
        URI url = URI.create("https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + base + "/" + target + "/");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), ApiResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter a conversão das moedas");
        }
    }
}
