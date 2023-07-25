package www.reservation.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import www.reservation.com.service.KakaoNotificationService;

@Controller
@Slf4j
public class reservationController {

  @Autowired
  private KakaoNotificationService kakaoNotificationService;

  @GetMapping("/authorize")
  public String getAuthorize(HttpServletRequest request) throws JsonMappingException, JsonProcessingException {

    log.info("code:{}",request.getParameter("code") );
    String code = (String) request.getParameter("code");
    kakaoNotificationService.getKakaoAuthToken(code);
    return "";
  }
  @GetMapping("/error")
  public void error(String error_description) {
    log.info("error_description:{}",error_description);
  }

}
