package com.spoticket.payment.domain.payment.model;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardCompany {
   IBK("3K", "기업 BC", "기업비씨", "IBK_BC"),
   GWANGJU("46", "광주은행", "광주", "GWANGJUBANK"),
   LOTTE("71", "롯데카드", "롯데", "LOTTE"),
   KDB("30", "KDB산업은행", "산업", "KDBBANK"),
   BC("31", "BC카드", "비씨", "BC"),
   SAMSUNG("51", "삼성카드", "삼성", "SAMSUNG"),
   SAEMAEUL("38", "새마을금고", "새마을", "SAEMAUL"),
   SHINHAN("41", "신한카드", "신한", "SHINHAN"),
   SHINHYEOP("62", "신협", "신협", "SHINHYEOP"),
   CITI("36", "씨티카드", "씨티", "CITI"),
   WOORI_BC("33", "우리BC카드(BC 매입)", "우리", "WOORI"),
   WOORI("W1", "우리카드(우리 매입)", "우리", "WOORI"),
   POST("37", "우체국예금보험", "우체국", "POST"),
   SAVING_BANK("39", "저축은행중앙회", "저축", "SAVINGBANK"),
   JEONBUK("35", "전북은행", "전북", "JEONBUKBANK"),
   JEJU("42", "제주은행", "제주", "JEJUBANK"),
   KAKAO("15", "카카오뱅크", "카카오뱅크", "KAKAOBANK"),
   KBANK("3A", "케이뱅크", "케이뱅크", "KBANK"),
   TOSS("24", "토스뱅크", "토스뱅크", "TOSSBANK"),
   HANA("21", "하나카드", "하나", "HANA"),
   HYUNDAI("61", "현대카드", "현대", "HYUNDAI"),
   KOOKMIN("11", "KB국민카드", "국민", "KOOKMIN"),
   NONGHYEOP("91", "NH농협카드", "농협", "NONGHYEOP"),
   SUHYEOP("34", "Sh수협은행", "수협", "SUHYEOP");

   private final String code;
   private final String fullName;
   private final String shortName;
   private final String englishName;



   public static String getShortNameByCode(String code) {
       return Arrays.stream(CardCompany.values())
               .filter(company -> company.code.equals(code))
               .map(CardCompany::getShortName)
               .findFirst()
               .orElse("알 수 없음");
   }


}