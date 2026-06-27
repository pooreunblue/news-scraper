package oop.search.presentation;

import oop.search.application.NewsPublisher;
import oop.search.application.NewsMarkdownFormatter;
import oop.search.domain.NewsResult;

import java.time.ZonedDateTime;
import java.util.List;

public class ConsoleNewsPublisher implements NewsPublisher {
    @Override
    public void publish(String topic, List<NewsResult> newsResults) {
        System.out.println(NewsMarkdownFormatter.format(topic, newsResults));
    }

    public static void main(String[] args) {
        NewsPublisher cnp = new ConsoleNewsPublisher();
        cnp.publish("창억떡", List.of(new NewsResult(
                "창억떡 맛있다",
                "창억떡 먹어봤니?",
                "https://naver.com",
                ZonedDateTime.now()
        ), new NewsResult(
                "창억떡 무봤나",
                "창억떡 직이네...",
                "https://naver2.com",
                ZonedDateTime.now()
        )));
    }
}
