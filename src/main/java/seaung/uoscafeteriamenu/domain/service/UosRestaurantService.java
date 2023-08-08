package seaung.uoscafeteriamenu.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UosRestaurantService {

    private final UosRestaurantRepository uosRestaurantRepository;

    public UosRestaurantMenuResponse getUosRestaurantMenu(UosRestaurantInput input) {
        UosRestaurant findUosRestaurant = uosRestaurantRepository.findByRestaurantNameAndMealType(input.getRestaurantName(), input.getMealType())
                . orElseThrow(() -> new UosRestaurantMenuException(UosRestaurantMenuException.NOT_FOUND_MENU));

        return UosRestaurantMenuResponse.builder()
                .restaurantName(findUosRestaurant.getRestaurantName().getKrName())
                .menu(findUosRestaurant.getMenuDesc())
                .mealType(findUosRestaurant.getMealType().getKrName())
                .build();
    }
}
