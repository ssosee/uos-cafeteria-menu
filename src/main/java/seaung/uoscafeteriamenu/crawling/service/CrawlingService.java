package seaung.uoscafeteriamenu.crawling.service;

import seaung.uoscafeteriamenu.domain.entity.CrawlingTarget;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.CrawlingTargetRepository;

public abstract class CrawlingService {

    private final CrawlingTargetRepository uosRestaurantsCrawlingInfoRepository;

    protected CrawlingService(CrawlingTargetRepository uosRestaurantsCrawlingInfoRepository) {
        this.uosRestaurantsCrawlingInfoRepository = uosRestaurantsCrawlingInfoRepository;
    }

    public CrawlingTarget findCrawlingTargetBy(UosRestaurantName restaurantsName) {
        return uosRestaurantsCrawlingInfoRepository.findByRestaurantsName(restaurantsName)
                .orElseThrow(() -> new IllegalArgumentException("식당 정보가 없습니다."));
    }
}
