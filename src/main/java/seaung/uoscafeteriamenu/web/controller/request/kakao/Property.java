package seaung.uoscafeteriamenu.web.controller.request.kakao;

import lombok.Builder;
import lombok.Data;

/**
 * 추가적으로 제공하는 사용자의 속성 정보입니다.
 */
@Data
public class Property {
    /**
     * 카오톡 채널에서 제공하는 사용자 식별키 입니다.
     * 카카오톡 채널의 자동응답 API에서 제공하던 user_key와 같습니다. (https://github.com/plusfriend/auto_reply#specification-1)
     * 모든 사용자에게 제공되는 값으로, botUserKey와 마찬가지로 봇에서 사용자를 식별하는데 사용할 수 있습니다.
     */
    private String plusfriendUserKey;

    /**
     * 봇 설정에서 앱키를 설정한 경우에만 제공되는 사용자 정보입니다.
     * 앱키를 설정하기 위해서는 카카오 디벨로퍼스 사이트에서 앱을 생성해야 합니다.
     * 카카오 디벨로퍼스 앱 생성하기 : (https://developers.kakao.com/docs/latest/ko/getting-started/app)
     * 앱 키가 정상적으로 등록된 경우, 카카오 로그인으로 받는 사용자 식별자와 동일한 값을 얻을 수 있습니다.
     */
    private String appUserId;

    /**
     * 사용자가 봇과 연결된 카카오톡 채널을 추가한 경우 제공되는 식별키입니다.
     * 채널을 추가한 경우만 True 값이 전달되며, 채널을 추가하지 않은 경우/차단한 경우에는 값이 전달되지 않습니다.
     */
    private Boolean isFriend;

    @Builder
    private Property(String plusfriendUserKey, String appUserId, Boolean isFriend) {
        this.plusfriendUserKey = plusfriendUserKey;
        this.appUserId = appUserId;
        this.isFriend = isFriend;
    }
}
