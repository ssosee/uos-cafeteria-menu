package seaung.uoscafeteriamenu.crawling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheUosRestaurant;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheUosRestaurantRepository;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrawlingCacheUosRestaurantService {

    private final CacheUosRestaurantRepository cacheUosRestaurantRepository;

    public void saveAllCrawlingDataInRedis(List<UosRestaurant> uosRestaurants) {
        // Redis에 저장
        List<CacheUosRestaurant> cacheUosRestaurants = uosRestaurants.stream()
                .map(CacheUosRestaurant::of)
                .collect(Collectors.toList());

        cacheUosRestaurantRepository.saveAll(cacheUosRestaurants);
    }
}
