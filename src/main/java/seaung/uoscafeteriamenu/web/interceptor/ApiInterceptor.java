package seaung.uoscafeteriamenu.web.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import seaung.uoscafeteriamenu.domain.cache.service.CacheBotApikeyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * preHandle: 컨트롤러 호출전 호출
 * postHandle: 컨트롤러 호출 후 호출(컨트롤러에서 예외가 발생하면 postHandle은 호출 안됨)
 * afterCompletion: 뷰 렌더링 이후 호출(항상 호출)
 */
@Slf4j
@RequiredArgsConstructor
public class ApiInterceptor implements HandlerInterceptor {

    private final CacheBotApikeyService botApiKeyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        checkApikey(request);
        return true;
    }

    private void checkApikey(HttpServletRequest request) {
        String botApikeyHeader = request.getHeader("botApikey");
        log.info("botApikey 확인={}", botApikeyHeader);

        botApiKeyService.findBotApiKeyHeader(botApikeyHeader);
    }
}
