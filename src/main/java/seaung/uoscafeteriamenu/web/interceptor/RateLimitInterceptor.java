package seaung.uoscafeteriamenu.web.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import seaung.uoscafeteriamenu.global.ratelimter.BucketResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

/**
 * <a href="https://alkhwa-113.tistory.com/entry/TIL-Requset-Wrapping-ContentCachingRequestWrapper-RequestBodyAdviceAdapter-HeaderBody-%EC%9D%98-%EC%B0%A8%EC%9D%B4%EC%99%80-%EC%84%B1%EB%8A%A5%EC%9D%B4%EC%8A%88">참고</a>
 */
@Deprecated
@Slf4j
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final BucketResolver bucketResolver;

    /**
     * ContentCachingRequestWrapper 는 getReader() 나 getInputStream() 등을 통해서 읽은 요청 본문을 캐시하고
     * byte array 로 이를 읽을 수 있게 하는 HttpServletRequest 래퍼이다.
     * ContentCachingResponseWrapper의 경우 Body를 돌려주기 위해 copyBodyToResponse method를 호출해줘야 한다.
     *
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
//        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
//        BufferedReader reader = requestWrapper.getReader();
//
//        ObjectMapper om = new ObjectMapper();
//        JsonNode jsonNode = om.readTree(reader);
//        String userId = jsonNode.get("userRequest").get("user").get("id").asText();

//        JsonNode jsonNode = om.readTree(body);
//        String userId = jsonNode.get("userRequest").get("user").get("id").asText();

        return true;
    }
}
