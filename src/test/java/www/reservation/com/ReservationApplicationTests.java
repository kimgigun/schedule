package www.reservation.com;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import www.reservation.com.service.KakaoNotificationService;
import www.reservation.com.service.송도스포츠파크예약_serivce;

@SpringBootTest
class ReservationApplicationTests {

  @Autowired
  private 송도스포츠파크예약_serivce reservationCheckerService;
  @Autowired
  private KakaoNotificationService kakaoNotificationService;

	@Test
	void contextLoads() {
	  reservationCheckerService.isReservationAvailable();
	}

	@Test
	void contextLoads1() throws UnsupportedEncodingException {
	  kakaoNotificationService.kakaoLogin();
	}

}
