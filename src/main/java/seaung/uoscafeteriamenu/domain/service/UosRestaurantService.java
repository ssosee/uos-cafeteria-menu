package seaung.uoscafeteriamenu.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantsInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UosRestaurantService {

    private final UosRestaurantRepository uosRestaurantRepository;

    // 학교식당의 식사종류의 학식 메뉴 조회
    // e.g) 학생회관 조식 라면
    public UosRestaurantMenuResponse getUosRestaurantMenu(UosRestaurantInput input) {
        // 학식 조회
        UosRestaurant findUosRestaurant = uosRestaurantRepository.findByCrawlingDateAndRestaurantNameAndMealType(input.getDate(), input.getRestaurantName(), input.getMealType())
                . orElseThrow(() -> new UosRestaurantMenuException(UosRestaurantMenuException.NOT_FOUND_MENU));

        return UosRestaurantMenuResponse.builder()
                .restaurantName(findUosRestaurant.getRestaurantName().getKrName())
                .menu(findUosRestaurant.getMenuDesc())
                .mealType(findUosRestaurant.getMealType().getKrName())
                .build();
    }

    // 금일 식사종류별 학식 조회
    // [학생회관 조식 라면, 양식당 조식 돈까스, 자연과학관 조식 제육]
    public List<UosRestaurantMenuResponse> getUosRestaurantsMenu(UosRestaurantsInput input) {
        // 학식들 조회
        List<UosRestaurant> findUosRestaurants = uosRestaurantRepository.findByCrawlingDateAndAndMealType(input.getDate(), input.getMealType());

        return findUosRestaurants.stream()
                .map(r -> UosRestaurantMenuResponse.builder()
                        .restaurantName(r.getRestaurantName().getKrName())
                        .mealType(r.getMealType().getKrName())
                        .menu(r.getMenuDesc())
                        .build())
                .collect(Collectors.toList());
    }
}
