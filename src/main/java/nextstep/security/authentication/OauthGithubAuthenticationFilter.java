package nextstep.security.authentication;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class OauthGithubAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String requestURI = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();


        if (requestURI.startsWith("/oauth2/authorization/github") && method.equals("GET")) {
            String url = String.format(
                    "https://github.com/login/oauth/authorize?client_id=%s&response_type=code&scope=read:user&redirect_uri=http://localhost:8080/login/oauth2/code/github",
                    "Ov23lijfjg9lkGyYVDXN");
            httpServletResponse.setStatus(HttpServletResponse.SC_MULTIPLE_CHOICES);
            httpServletResponse.sendRedirect(url);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
