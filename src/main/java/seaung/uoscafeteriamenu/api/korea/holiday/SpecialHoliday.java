package seaung.uoscafeteriamenu.api.korea.holiday;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SpecialHoliday {
    X_MAX("기독탄신일") {
        @Override
        public String getSpecialMessage() {
            return "🎄메리메리 크리스마스이다 휴.먼!⛄️\n"
                    + "안타깝지만 오늘 학식은 제공되지 않는다.😿\n"
                    + "오늘은 ✨특별한 날이니 ⭐️특별한 휴먼들과 🌟특별한 식사를 즐기기 바란다.\n"
                    + "행복하고 따뜻한 연.말 보내라!\n\n"
                    + "사랑해.휴.먼! 🎅";
        }
    },
    KOREA_THANKS_GIVING("추석") {
        @Override
        public String getSpecialMessage() {
            return null;
        }
    },
    KOREA_NEW_YEAR("설날") {
        @Override
        public String getSpecialMessage() {
            return null;
        }
    },
    NEW_YEAR("1월1일") {
        @Override
        public String getSpecialMessage() {
            return null;
        }
    };

    private final String krName;
    abstract public String getSpecialMessage();
}
