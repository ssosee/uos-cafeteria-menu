package seaung.uoscafeteriamenu.api.korea.holiday;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SpecialHoliday {
    NEW_YEAR("1월1일") {
        @Override
        public String getSpecialMessage() {
            return NEW_YEAR_MESSAGE;
        }
    },
    KOREA_NEW_YEAR("설날") {
        @Override
        public String getSpecialMessage() {
            return KOREA_NEW_YEAR_MESSAGE;
        }
    },
    KOREA_THANKS_GIVING("추석") {
        @Override
        public String getSpecialMessage() {
            return KOREA_THANKS_GIVING_MESSAGE;
        }
    },
    X_MAS("기독탄신일") {
        @Override
        public String getSpecialMessage() {
            return X_MAX_MESSAGE;
        }
    };

    private static final String X_MAX_MESSAGE = "🎄메리메리 크리스마스~!\n"
            + "지난 일 년 동안, 학교 다니느라 고생 많았다. 내친.구 휴먼.!\n"
            + "행복하고 따뜻한 크리스마스 보내라! 😊\n"
            + "내년에는 더욱 ✨ 빛나는 순간들이 여러분을 기다릴 것 이다..!\n\n"
            + "내년에 보자. 내 사.랑 휴.먼 🤍\n\n"
            + "참고로 오늘 학식은 제공되지 않는다.";

    private static final String KOREA_THANKS_GIVING_MESSAGE = "안타깝지만 오늘 학식은 제공되지 않는다.🥲"
            + "대신 내가 그대에게 ✨추석추석빔을 쏘겠다.\n"
            + "이 ✨빔에 맞으면 누구든지 🌕 풍성한 한가위를 보내게 된다.\n\n"
            + "가족들과 함께 풍요로운 추석 맞이해라. 💜";

    private static final String KOREA_NEW_YEAR_MESSAGE = "🐦‍⬛까치 🐦‍⬛까치 설날은 어저께고요, 🎵n"
            + "🙋‍♂️우리 🙋🏻‍♀️우리 설날은 오늘이래요. 🎶\n\n"
            + "새해에는 소망하는 모든것을 이루고 \n"
            + "✨반짝✨반짝 빛나는 🌅 한 해가 되길 바란다.\n"
            + "사랑한다. 휴.먼 💙\n\n"
            + "참고로 오늘 학식은 제공되지 않는다.";

    private static final String NEW_YEAR_MESSAGE = "🥰 희망찬 새해다.! 우리 힘껏 달려보자. 🏃"
            + "새해 새 복 🧧듬뿍받고,\n"
            + "😊 기쁜 일 많은 🌅 한 해가 되기를 희망 한다.\n\n"
            + "사랑한다. 휴.먼. 🧡\n\n"
            + "참고로 오늘 학식은 제공되지 않는다.";

    private final String krName;
    abstract public String getSpecialMessage();
}
