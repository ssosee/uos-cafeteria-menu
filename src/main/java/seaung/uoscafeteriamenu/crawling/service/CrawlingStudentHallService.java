package seaung.uoscafeteriamenu.crawling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seaung.uoscafeteriamenu.crawling.crawler.CrawlingResponse;
import seaung.uoscafeteriamenu.domain.repository.StudentHallRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CrawlingStudentHallService {

    private final StudentHallRepository studentHallRepository;

    public void save(List<CrawlingResponse> responses) {

    }
}
