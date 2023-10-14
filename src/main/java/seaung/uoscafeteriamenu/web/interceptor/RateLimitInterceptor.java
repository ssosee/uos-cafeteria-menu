package seaung.uoscafeteriamenu.web.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import seaung.uoscafeteriamenu.global.ratelimter.BucketResolver;
import seaung.uoscafeteriamenu.web.controller.request.kakao.UserRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

@Slf4j
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final BucketResolver bucketResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        BufferedReader reader = request.getReader();
        String string = reader.readLine();


        return bucketResolver.checkBucketCounter(string);
    }
}
