package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        LunarCalendar lc = new LunarCalendar();

        List<LocalDate> localDates = new ArrayList<>(lc.holidaySet(2023));
        Collections.sort(localDates);

        for (LocalDate localDate : localDates) {
            System.out.println(localDate);
        }
    }
}


//당초 공휴일	종복되는 날	대체공휴일
//3.1절, 어린이날, 부천님오신날, 광복절, 개천절, 한글날, 성탄일	토요일, 일요일, 다른 공휴일과 겹친 경우	공휴일 다음의 첫 번째 비공휴일
//설 연휴(3일), 추석 연휴(3일)	일요일, 다른 공휴일과 겹친 경우
// (참고) 대체공휴일이 적용되지 않는 날
//1월 1일
//현충일(6.6)
//제헌절(7.17)