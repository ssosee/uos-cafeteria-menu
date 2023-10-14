package seaung.uoscafeteriamenu.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <a href="https://meetup.nhncloud.com/posts/44">참고</a>
 * request 와 마찬가지로 response 도 body를 한번만 읽을 수 있다.
 * 만약에 인터셉터에서 꺼내 읽게 되면 이후 받는 사람 즉, 클라이언트가 Body 값을 꺼내지 못 할 수도 있다.
 * ContentCachingRequestWrapper 는 객체 생성과 동시에 바디값을 저장하지만 ContentCachingResponseWrapper 는 객체 생성을 하면,
 * 내부적으로 원래 response 를 super(response) 로 셋팅할 뿐이다.
 * 이 메서드를 통해 이 ContentCachingResponseWrapper 의 content 라는 필드에 복사를 해놓는 과정이 따로 필요하다.
 */
@Slf4j
public class ServletCachingWrapperFilter extends OncePerRequestFilter {
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
//        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
//
//        log.info("필터 전");
//        chain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);
//        log.info("필터 후");
//        contentCachingResponseWrapper.copyBodyToResponse();
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);

        log.info("필터 전");
        chain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);
        log.info("필터 후");
        contentCachingResponseWrapper.copyBodyToResponse();
    }
}
