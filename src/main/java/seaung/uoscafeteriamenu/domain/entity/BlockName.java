package seaung.uoscafeteriamenu.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlockName {
    recommend_menu_block("메뉴_추천하기_블록");

    private final String krName;
}
