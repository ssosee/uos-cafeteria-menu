package seaung.uoscafeteriamenu.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UosRestaurantService {

    private final UosRestaurantRepository uosRestaurantRepository;

    public UosRestaurantMenuResponse getUosRestaurantMenu(UosRestaurantInput input) {
        UosRestaurant findUosRestaurant = uosRestaurantRepository.findByRestaurantNameAndMealType(input.getRestaurantName(), input.getMealType())
                .orElseThrow(() -> new IllegalArgumentException("해당 식당 메뉴가 없습니다."));

        return UosRestaurantMenuResponse.builder()
                .restaurantName(findUosRestaurant.getRestaurantName().getKrName())
                .menu(findUosRestaurant.getMenuDesc())
                .mealType(findUosRestaurant.getMealType().getKrName())
                .build();
    }
}
