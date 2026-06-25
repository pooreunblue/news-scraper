package oop.search.infrastructure;

import oop.search.application.NewsPublisher;
import oop.search.domain.NewsResult;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GitHubNewsPublisher extends AbstractHttpClient implements NewsPublisher {

    private static final String GITHUB_API_URL = "https://api.github.com/repos/%s/issues";
    // %s : GITHUB_REPOSITORY <- GitHub Actions가 주는 것을 그대로 쓸 예정

    public GitHubNewsPublisher() {
        super(GITHUB_API_URL
                .formatted(System.getenv("GITHUB_REPOSITORY"))
        );
    }

    @Override
    public void publish(String topic, List<NewsResult> newsResults) {
//        httpClient
        String url = endpoint;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
//                .header("X-Naver-Client-Id", clientId)
                .build();

        try {
            httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}