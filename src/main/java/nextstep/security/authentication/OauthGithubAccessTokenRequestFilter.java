package nextstep.security.authentication;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OauthGithubAccessTokenRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String requestURI = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();
        RestTemplate restTemplate = new RestTemplate();

        if (requestURI.startsWith("/login/oauth2/code/github") && method.equals("GET")) {
            String[] codes = httpServletRequest.getParameterMap().get("code");
            String code = codes[0];
            HashMap<Object, Object> bodyParam = new HashMap<>();
            bodyParam.put("client_id", "Ov23lijfjg9lkGyYVDXN");
            bodyParam.put("client_secret", "867b739c848ca39ae89c8a08a011107d3e249aa1");
            bodyParam.put("code", code);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    "http://localhost:8089/login/oauth/access_token", bodyParam, String.class);

            String token = null;
            if(responseEntity.getStatusCode().is2xxSuccessful()){
                token = responseEntity.getBody();
            }

        }

    }
}
