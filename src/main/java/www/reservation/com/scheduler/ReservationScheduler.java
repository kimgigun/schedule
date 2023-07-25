package www.reservation.com.scheduler;

import java.io.UnsupportedEncodingException;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;
import www.reservation.com.service.KakaoNotificationService;
import www.reservation.com.service.송도스포츠파크예약_serivce;


@Component
@EnableScheduling
@Slf4j
public class ReservationScheduler {
  private final 송도스포츠파크예약_serivce reservationCheckerService;
  private final KakaoNotificationService kakaoNotificationService;

  public ReservationScheduler(송도스포츠파크예약_serivce reservationCheckerService
      ,KakaoNotificationService kakaoNotificationService
      ) {
      this.reservationCheckerService = reservationCheckerService;
      this.kakaoNotificationService = kakaoNotificationService;
  }

  @Scheduled(fixedDelay = 1000 * 10) // 1분마다 실행
  public void checkReservationAvailability() throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
      if (reservationCheckerService.isReservationAvailable()) {
        log.info("메시지발송");
        kakaoNotificationService.kakaoLogin(); // 예약 정보 확인 후 알림 전송
      }
  }
}
