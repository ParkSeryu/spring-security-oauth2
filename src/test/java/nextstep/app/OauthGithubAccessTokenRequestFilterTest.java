package nextstep.app;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 8089)
class OauthGithubAccessTokenRequestFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setupMockServer() throws Exception {
        stubForAccessToken();
    }

    @Test
    void test() throws Exception {
        String requestUri = "/login/oauth2/code/github?code=mock_code";

        mockMvc.perform(MockMvcRequestBuilders.get(requestUri))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    private static void stubForAccessToken() throws JsonProcessingException {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("access_token", "mock_access_token");
        responseBody.put("token_type", "bearer");
        String jsonResponse = new ObjectMapper().writeValueAsString(responseBody);

        stubFor(post(urlEqualTo("/login/oauth/access_token"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .withBody(jsonResponse)));
    }
}