package com.github.daggerok;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(port = 8080)
@SpringBootTest(webEnvironment = NONE)
class SpringBootWireMockExampleApplicationTests {

  @Autowired RestClient restClient;
  @Autowired ObjectMapper objectMapper;

  @Test
  void test() throws Exception {
    // given:
    TestingResponse expected = TestingResponse.of("trololo");
    String body = objectMapper.writeValueAsString(expected);
    // when:
    WireMock.stubFor(post(urlEqualTo("/api/v1/up/"))
                         .withRequestBody(matchingJsonPath("$.input"))
                         .willReturn(aResponse()
                                         .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                         .withBody(body)
                                         .withStatus(OK.value())))
    ;
    // then:
    TestingRequest request = TestingRequest.of("ololo");
    TestingResponse response = restClient.post(request);
    assertThat(response).isEqualTo(expected);
  }
}
