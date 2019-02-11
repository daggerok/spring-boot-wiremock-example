package com.github.daggerok;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
class TestingRequest {
  String input;
}

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
class TestingResponse {
  String output;
}

@Log4j2
@Component
@RequiredArgsConstructor
class RestClient {

  final RestTemplate restTemplate;

  public TestingResponse post(TestingRequest request) {
    log.info("sending: {}", request);
    String url = "http://127.0.0.1:8080/api/v1/up/";
    TestingResponse response = restTemplate.postForEntity(url, request, TestingResponse.class)
                                           .getBody();
    log.info("received: {}", response);
    return response;
  }
}

@SpringBootApplication
public class SpringBootWireMockExampleApplication {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  /*@Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper());
    return builder.additionalMessageConverters(converter)
                  .build();
  }*/

  public static void main(String[] args) {
    SpringApplication.run(SpringBootWireMockExampleApplication.class, args);
  }
}
