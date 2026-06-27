package oop.search.application;

import oop.search.domain.NewsResult;

import java.util.List;

public class CompositeNewsPublisher implements NewsPublisher {

    private final List<NewsPublisher> publishers;

    public CompositeNewsPublisher(NewsPublisher... publishers) {
        this.publishers = List.of(publishers);
    }

    @Override
    public void publish(String topic, List<NewsResult> newsResults) {
        for (NewsPublisher publisher : publishers) {
            publisher.publish(topic, newsResults);
        }
    }
}
