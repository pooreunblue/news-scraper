package oop.search.infrastructure;

import oop.search.application.NewsPublisher;
import oop.search.domain.NewsResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HtmlNewsPublisher implements NewsPublisher {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final Path outputPath;

    public HtmlNewsPublisher() {
        String outputDirectory = System.getenv().getOrDefault("HTML_OUTPUT_DIR", "site");
        this.outputPath = Path.of(outputDirectory, "index.html");
    }

    @Override
    public void publish(String topic, List<NewsResult> newsResults) {
        try {
            Files.createDirectories(outputPath.getParent());
            Files.writeString(outputPath, createHtml(topic, newsResults));
            System.out.println("HTML 뉴스 페이지 생성: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException("HTML 뉴스 페이지를 생성하지 못했습니다.", e);
        }
    }

    private String createHtml(String topic, List<NewsResult> newsResults) {
        StringBuilder cards = new StringBuilder();
        for (NewsResult news : newsResults) {
            cards.append("""
                    <article class="news-card">
                      <p class="date">%s</p>
                      <h2>%s</h2>
                      <p class="description">%s</p>
                      <a href="%s" target="_blank" rel="noopener noreferrer">기사 원문 보기 <span aria-hidden="true">&rarr;</span></a>
                    </article>
                    """.formatted(
                    news.pubDate().format(DATE_FORMATTER),
                    escapeHtml(news.title()),
                    escapeHtml(news.description()),
                    escapeHtml(news.url())
            ));
        }

        return """
                <!doctype html>
                <html lang="ko">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>%s 뉴스 브리핑</title>
                  <style>
                    :root { --ink: #17221c; --paper: #f4f0e6; --accent: #d84b2a; --line: #c9c1ae; }
                    * { box-sizing: border-box; }
                    body { margin: 0; color: var(--ink); background: radial-gradient(circle at top right, #f1c97b 0, transparent 32rem), var(--paper); font-family: Georgia, "Noto Serif KR", serif; }
                    main { width: min(1080px, calc(100%% - 32px)); margin: 0 auto; padding: 72px 0 96px; }
                    header { border-bottom: 3px solid var(--ink); padding-bottom: 28px; margin-bottom: 32px; }
                    .eyebrow, .date { font-family: "Trebuchet MS", sans-serif; letter-spacing: .08em; text-transform: uppercase; }
                    .eyebrow { color: var(--accent); font-weight: 700; }
                    h1 { max-width: 800px; margin: 10px 0 14px; font-size: clamp(2.7rem, 7vw, 6rem); line-height: .95; }
                    .updated { margin: 0; color: #59635d; }
                    .news-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 20px; }
                    .news-card { display: flex; min-height: 290px; flex-direction: column; padding: 28px; border: 1px solid var(--line); background: rgba(255,255,255,.58); box-shadow: 6px 6px 0 var(--ink); }
                    .date { margin: 0 0 18px; color: var(--accent); font-size: .78rem; font-weight: 700; }
                    h2 { margin: 0 0 16px; font-size: clamp(1.35rem, 3vw, 2rem); line-height: 1.25; }
                    .description { color: #48514c; line-height: 1.7; }
                    a { align-self: flex-start; margin-top: auto; color: var(--ink); font-family: "Trebuchet MS", sans-serif; font-weight: 700; text-decoration-color: var(--accent); text-decoration-thickness: 2px; text-underline-offset: 5px; }
                    @media (max-width: 680px) { main { padding-top: 40px; } .news-grid { grid-template-columns: 1fr; } .news-card { min-height: 250px; } }
                  </style>
                </head>
                <body>
                  <main>
                    <header>
                      <p class="eyebrow">Naver News Scraper</p>
                      <h1>%s 뉴스 브리핑</h1>
                      <p class="updated">마지막 업데이트: %s KST · 총 %d건</p>
                    </header>
                    <section class="news-grid" aria-label="뉴스 목록">
                      %s
                    </section>
                  </main>
                </body>
                </html>
                """.formatted(
                escapeHtml(topic),
                escapeHtml(topic),
                ZonedDateTime.now(SEOUL).format(DATE_FORMATTER),
                newsResults.size(),
                cards
        );
    }

    private String escapeHtml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
