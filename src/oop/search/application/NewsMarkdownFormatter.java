package oop.search.application;

import oop.search.domain.NewsResult;

import java.time.format.DateTimeFormatter;
import java.util.List;

public final class NewsMarkdownFormatter {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private NewsMarkdownFormatter() {
    }

    public static String format(String topic, List<NewsResult> newsResults) {
        StringBuilder markdown = new StringBuilder("# ")
                .append(topic)
                .append(" 뉴스\n\n");

        for (NewsResult newsResult : newsResults) {
            markdown.append("## ")
                    .append(newsResult.title())
                    .append("\n\n")
                    .append(newsResult.description())
                    .append("\n\n")
                    .append("- 링크: [기사 보기](")
                    .append(newsResult.url())
                    .append(")\n")
                    .append("- 발행일자: ")
                    .append(newsResult.pubDate().format(DATE_FORMATTER))
                    .append("\n\n");
        }

        return markdown.toString().trim();
    }
}
