package seaung.uoscafeteriamenu.crawling.crawler;

import org.jsoup.Connection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StudentHallCrawlerTest {

    @Autowired
    StudentHallCrawler studentHallCrawler;

    @Test
    @DisplayName("서울시립대학교 학생회관 1층 식당 주간식단표를 크롤링 한다.")
    void studentHallCrawler() throws IOException {
        // given
        String cssQuery = "div.listType02#week table tbody tr";
        String studentHallUrl = "https://english.uos.ac.kr/food/placeList.do";

        // when
        List<CrawlingResponse> responses = studentHallCrawler.crawlingFrom(studentHallUrl, cssQuery);

        // then
        assertThat(responses).isNotEmpty();
        responses.stream()
                .forEach(r -> System.out.println(r.getDate()));
        responses.stream()
                .forEach(r -> r.getMenu().entrySet().forEach(System.out::println));
    }
}