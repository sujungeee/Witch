package com.ssafy.witch.support.docs;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.ContentModifyingOperationPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;

@TestConfiguration
public class RestDocsConfiguration {

  @Bean
  public RestDocumentationResultHandler restDocumentationResultHandler() {
    return MockMvcRestDocumentation.document(
        "{class-name}/{method-name}",  // 문서 이름 설정
        preprocessRequest(  // 공통 헤더 설정
            modifyUris()
                .scheme("https")
                .host("ssafy.com")
                .removePort(),
            modifyHeaders()
                .remove("Content-Length")
                .remove("Host"),
            prettyPrint()),  // pretty json 적용
        preprocessResponse(  // 공통 헤더 설정
            modifyHeaders()
                .remove("Content-Length")
                .remove("Content-Type")
                .remove("X-Content-Type-Options")
                .remove("X-XSS-Protection")
                .remove("Cache-Control")
                .remove("Pragma")
                .remove("Expires")
                .remove("X-Frame-Options")
                .remove("Vary"),
            customPrettyPrint())    // pretty json 적용
    );
  }

  private OperationPreprocessor customPrettyPrint() {
    return new ContentModifyingOperationPreprocessor(
        (content, contentType) -> {
          ObjectMapper objectMapper = new ObjectMapper();
          PrettyPrinter prettyPrinter = new DefaultPrettyPrinter()
              .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
          try {
            return objectMapper.writer(prettyPrinter)
                .writeValueAsBytes(objectMapper.readTree(content));
          } catch (IOException ex) {
            return content;
          }
        });
  }
}
