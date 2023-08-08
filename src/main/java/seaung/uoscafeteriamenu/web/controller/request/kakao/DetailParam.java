package seaung.uoscafeteriamenu.web.controller.request.kakao;

import lombok.Data;

/**
 * 블록의 발화에서 설정한 파라미터를 추출하면 추출값 뿐만 아니라 추가적인 정보를 얻을 수 있습니다.
 * 그 예로, 요일을 sys.date 라는 시스템 엔티티로 추출하면 단순히 ‘금요일’ 이라는 요일 뿐만 아니라 구체적인 날짜까지 포함합니다.
 *
 * params은 봇 시스템에서 분석하여 추가적인 정보를 채운 값입니다.
 * detailParams는 봇 시스템에서 분석한 값 뿐만 아니라, 원래 발화에 담겨 있었던 origin을 포함합니다.
 */
@Data
public class DetailParam {

}
