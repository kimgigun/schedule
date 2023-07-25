package www.reservation.com.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KakaoNotificationService {

  protected static final String APP_TYPE_URL_ENCODED = "application/x-www-form-urlencoded;charset=utf-8";
  protected static final String APP_TYPE_JSON = "application/json;charset=UTF-8";

  private final String KAKAO_REST_API_KEY = "b39201a4f172cc52c37a0e02cf6c088b";
  private final RestTemplate restTemplate = new RestTemplate();

  public void sendNotification(String token, String message) throws JsonProcessingException {
    String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + token);
    headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

    Map<String, Object> params = new HashMap<>();
    Map<String, Object> hashMap = new HashMap<String,Object>();
    hashMap.put("web_url", "https://ssp.eco-i.or.kr/reservation/dive/diving.asp?mNo=MC030000000");
    params.put("object_type", "text");
    params.put("text", message);
    params.put("link", hashMap);

    ObjectMapper om = new ObjectMapper();
    String reqBody = om.writeValueAsString(params);
    try {
      reqBody = URLEncoder.encode(reqBody, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    reqBody = "template_object=" + reqBody;
    HttpEntity<String> requestEntity = new HttpEntity<>(reqBody, headers);
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    log.info("Kakao Message API Response: {}", response.getBody());
  }

  public void getKakaoAuthToken(String code) throws JsonMappingException, JsonProcessingException  {

    String AUTH_URL = "https://kauth.kakao.com/oauth/token";
    String accessToken = "";
    String refrashToken = "";

    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/x-www-form-urlencoded");


    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", KAKAO_REST_API_KEY);
    params.add("code", code);
    params.add("redirect_uri", "http://localhost:9080/authorize");

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, header);

    ResponseEntity<String> response = restTemplate.exchange(AUTH_URL, HttpMethod.POST, request, String.class);

    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> responseMap = mapper.readValue(response.getBody(), new TypeReference<HashMap<String, Object>>(){});

    accessToken = (String) responseMap.get("access_token");
    //refrashToken = (String) responseMap.get("refresh_token");
    if(accessToken.isEmpty() || refrashToken.isEmpty()) {
      log.debug("토큰발급에 실패했습니다.");
    }
    sendNotification(accessToken, "송도스포츠파크 예약가능");
  }

  public void kakaoLogin() throws UnsupportedEncodingException{
    String AUTH_URL = "https://kauth.kakao.com/oauth/authorize";

    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/x-www-form-urlencoded");

    String queryString = "";
    queryString += "?response_type=code";
    queryString += "&client_id=" + KAKAO_REST_API_KEY;
    queryString += "&redirect_uri=http%3A%2F%2Flocalhost%3A9080%2Fauthorize";
    queryString += "&scope=talk_message";

    String url = AUTH_URL+queryString;

    log.info("url:{}",url);

    HttpEntity<String> requestEntity = new HttpEntity<>(header);


    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
   // log.info(response.getBody().toString());
//    ObjectMapper mapper = new ObjectMapper();
//    Map<String, Object> responseMap = mapper.readValue(response.getBody(), new TypeReference<HashMap<String, Object>>(){});
  }



}
