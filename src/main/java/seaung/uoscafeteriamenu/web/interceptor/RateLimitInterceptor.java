package seaung.uoscafeteriamenu.web.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import seaung.uoscafeteriamenu.global.ratelimter.BucketResolver;
import seaung.uoscafeteriamenu.web.controller.request.kakao.SkillPayload;
import seaung.uoscafeteriamenu.web.controller.request.kakao.User;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <a href="https://alkhwa-113.tistory.com/entry/TIL-Requset-Wrapping-ContentCachingRequestWrapper-RequestBodyAdviceAdapter-HeaderBody-%EC%9D%98-%EC%B0%A8%EC%9D%B4%EC%99%80-%EC%84%B1%EB%8A%A5%EC%9D%B4%EC%8A%88">참고</a>
 */
@Slf4j
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final BucketResolver bucketResolver;

    /**
     * 인터셉터와 컨트롤러는 같은 레벨이기 때문에,
     * 아무리 인터셉터에서 request 를 래핑해서 사용한다 한들 반환해서 아래에서 사용하는 것이 아니기 때문에,
     * 아래의 handle 메서드에서는 래핑한 객체가 아닌 그냥 request 를 사용하게 된다.
     *
     * Call By Value 로 의해 래핑한 객체는 사라지고, 그냥 원래 객체를 사용하는 것이다.
     *
     * 결국 그럼 DispatcherServlet 에 들어오기 전에 래핑을 해야 한다는 것인데, 이에 적절한게 filter 이다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
        byte[] body = requestWrapper.getContentAsByteArray();
//        ServletInputStream inputStream = requestWrapper.getInputStream();
//        byte[] body = StreamUtils.copyToByteArray(inputStream);

        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(body);
        String userId = jsonNode.get("userRequest").get("user").get("id").asText();

        return bucketResolver.checkBucketCounter(userId);
    }
}
