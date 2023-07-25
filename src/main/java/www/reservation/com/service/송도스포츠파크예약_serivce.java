package www.reservation.com.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class 송도스포츠파크예약_serivce {

  private static final String URL = "https://ssp.eco-i.or.kr/reservation/dive/diving.asp?mNo=MC030000000#close";

  public boolean isReservationAvailable() {
    boolean flag = false;
    Set<String> dateSet = new HashSet<String>();
    dateSet.add("월");
    dateSet.add("화");
    dateSet.add("수");
    dateSet.add("목");
    dateSet.add("금");
    dateSet.add("토");
    dateSet.add("일");
      try {
          Document document = Jsoup.connect(URL).get();
          Elements reservationBtns = document.select("#appForm > ul > li > dl > dd");
          for (Element element : reservationBtns) {
            Element parent = element.parent();
            boolean numberCheckFlag = numberFlag(element.text(), 3);
            boolean dateCheckFlag = dateCheck(parent.text(), dateSet);

            if (element.text().contains("구분없이 예약") && numberCheckFlag && dateCheckFlag){
              log.info(element.text());
              log.info("예약가능 발생");
              log.info("부모: {}",parent.text());
              flag = true;
              break;
            }
            if (element.text().contains("프리다이버") && numberCheckFlag && dateCheckFlag){
              log.info(element.text());
              log.info("프리다이버 예약가능 발생");
              log.info("부모: {}",parent.text());
              flag = true;
              break;
            }
          }
          return flag;

      } catch (IOException e) {
          // 예외 처리
          e.printStackTrace();
          return false;
      }
  }

  public boolean numberFlag(String input, int num) {
    input = input.replaceAll("\\d+부","").trim();
    //String input = "구분없이 예약 (38명/40명) 예약마감";
    String regex = "\\d+";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(input);

    int[] numbers = new int[2];
    int index = 0;
    while (matcher.find() && index < 2) {
      int number = Integer.parseInt(matcher.group());
      numbers[index++] = number;
    }

    int result = numbers[1] - numbers[0];
//    log.info("input: {}",input);
//    log.info("num1:{}, num0:{}",numbers[1],numbers[0]);
//    log.info("result:{}",result);
    return result >= num;
  }

  public boolean dateCheck(String str, Set<String> dateSet) {
    boolean flag = false;
    for(String date : dateSet) {
      if(str.contains(date)) {
        flag= true;
        break;
      }
    }
    return flag;

  }

}
