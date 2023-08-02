package org.example;

import com.github.usingsky.calendar.KoreanLunarCalendar;

import java.time.LocalDate;
import java.util.*;

/**
 * https://www.law.go.kr/LSW//lsInfoP.do?lsId=002404&ancYnChk=0#0000
 *
 * 관련규제버튼생활법령버튼
 *  제3조(대체공휴일) ① 제2조제2호부터 제10호까지의 공휴일이 다음 각 호의 어느 하나에 해당하는 경우에는 그 공휴일 다음의 첫 번째 비공휴일(제2조 각 호의 공휴일이 아닌 날을 말한다. 이하 같다)을 대체공휴일로 한다. <개정 2023. 5. 4.>
 *
 * 1. 제2조제2호ㆍ제6호ㆍ제7호 또는 제10호의 공휴일이 토요일이나 일요일과 겹치는 경우
 *
 * 2. 제2조제4호 또는 제9호의 공휴일이 일요일과 겹치는 경우
 *
 * 3. 제2조제2호ㆍ제4호ㆍ제6호ㆍ제7호ㆍ제9호 또는 제10호의 공휴일이 토요일ㆍ일요일이 아닌 날에 같은 조 제2호부터 제10호까지의 규정에 따른 다른 공휴일과 겹치는 경우
 *
 * ② 제1항에 따른 대체공휴일이 같은 날에 겹치는 경우에는 그 대체공휴일 다음의 첫 번째 비공휴일까지 대체공휴일로 한다.
 *
 * ③ 제1항 및 제2항에 따른 대체공휴일이 토요일인 경우에는 그 다음의 첫 번째 비공휴일을 대체공휴일로 한다.
 *
 * [전문개정 2021. 8. 4.]
 *
 * 조문체계도버튼
 *  제4조(임시공휴일의 지정) 제2조제11호에 따른 공휴일을 지정하려는 경우에는 국무회의의 심의를 거쳐야 한다.
 *
 * [본조신설 2021. 8. 4.]
 */

public class LunarCalendar {
    public static final int LD_SUNDAY = 7;
    public static final int LD_SATURDAY = 6;
    public static final int LD_MONDAY = 1;
    static Map<Integer, Set<LocalDate>> map = new HashMap<>();

    private LocalDate Lunar2Solar(LocalDate lunar) {
        KoreanLunarCalendar lunarCalendar = KoreanLunarCalendar.getInstance();
        lunarCalendar.setLunarDate(lunar.getYear(), lunar.getMonthValue(), lunar.getDayOfMonth(), false);

        return LocalDate.of(lunarCalendar.getSolarYear(), lunarCalendar.getSolarMonth(), lunarCalendar.getSolarDay());
    }

    public Set<LocalDate> holidaySet(int year) {
        if (map.containsKey(year)) return map.get(year);
        Set<LocalDate> holidaysSet = new HashSet<>();

        // 양력 휴일
        holidaysSet.add(LocalDate.of(year, 1, 1));   // 신정
        holidaysSet.add(LocalDate.of(year, 3, 1));   // 삼일절
        holidaysSet.add(LocalDate.of(year, 5, 5));   // 어린이날
        holidaysSet.add(LocalDate.of(year, 6, 6));   // 현충일
        holidaysSet.add(LocalDate.of(year, 8, 15));   // 광복절
        holidaysSet.add(LocalDate.of(year, 10, 3));   // 개천절
        holidaysSet.add(LocalDate.of(year, 10, 9));   // 한글날
        holidaysSet.add(LocalDate.of(year, 12, 25));   // 성탄절

        // 음력 휴일
        holidaysSet.add(Lunar2Solar(LocalDate.of(year, 1, 1)).minusDays(1));  // 설날1
        holidaysSet.add(Lunar2Solar(LocalDate.of(year, 1, 1)));  // 설날2
        holidaysSet.add(Lunar2Solar(LocalDate.of(year, 1, 2)));  // 설날3
        holidaysSet.add(Lunar2Solar(LocalDate.of(year, 4, 8)));  // 석가 탄신일
        holidaysSet.add(Lunar2Solar(LocalDate.of(year, 8, 14)));  // 추석1
        holidaysSet.add(Lunar2Solar(LocalDate.of(year, 8, 15)));  // 추석2
        holidaysSet.add(Lunar2Solar(LocalDate.of(year, 8, 16)));  // 추석3

        // 어린이날, 석가 탄신일, 성탄절, 삼일절, 광복절, 개천절, 한글날 대체공휴일 검사 : 토요일, 일요일인 경우 그 다음 평일을 대체공유일로 지정
        holidaysSet.add(substituteHoliday(LocalDate.of(year, 5, 5)));
        holidaysSet.add(substituteHoliday(Lunar2Solar(LocalDate.of(year, 4, 8))));
        holidaysSet.add(substituteHoliday(LocalDate.of(year, 3, 1)));
        holidaysSet.add(substituteHoliday(LocalDate.of(year, 8, 15)));
        holidaysSet.add(substituteHoliday(LocalDate.of(year, 10, 3)));
        holidaysSet.add(substituteHoliday(LocalDate.of(year, 10, 9)));
        holidaysSet.add(substituteHoliday(LocalDate.of(year, 12, 25)));


        // 설날 대체공휴일 검사
        if (Lunar2Solar(LocalDate.of(year, 1, 1)).getDayOfWeek().getValue() == LD_SUNDAY) {    // 일
            holidaysSet.add(Lunar2Solar(LocalDate.of(year, 1, 3)));
        }
        if (Lunar2Solar(LocalDate.of(year, 1, 1)).getDayOfWeek().getValue() == LD_MONDAY) {    // 월
            holidaysSet.add(Lunar2Solar(LocalDate.of(year, 1, 3)));
        }
        if (Lunar2Solar(LocalDate.of(year, 1, 2)).getDayOfWeek().getValue() == LD_SUNDAY) {    // 일
            holidaysSet.add(Lunar2Solar(LocalDate.of(year, 1, 3)));
        }

        // 추석 대체공휴일 검사
        if (Lunar2Solar(LocalDate.of(year, 8, 14)).getDayOfWeek().getValue() == LD_SUNDAY) {
            holidaysSet.add(Lunar2Solar(LocalDate.of(year, 8, 17)));
        }
        if (Lunar2Solar(LocalDate.of(year, 8, 15)).getDayOfWeek().getValue() == LD_SUNDAY) {
            holidaysSet.add(Lunar2Solar(LocalDate.of(year, 8, 17)));
        }
        if (Lunar2Solar(LocalDate.of(year, 8, 16)).getDayOfWeek().getValue() == LD_SUNDAY) {
            holidaysSet.add(Lunar2Solar(LocalDate.of(year, 8, 17)));
        }

        map.put(year, holidaysSet);

        return holidaysSet;
    }

    private LocalDate substituteHoliday(LocalDate date) {
        if (date.getDayOfWeek().getValue() == LD_SUNDAY) { // 일요일 + 1 = 월
            return date.plusDays(1);
        }
        if (date.getDayOfWeek().getValue() == LD_SATURDAY) {  // 토요일 + 2 = 월
            return date.plusDays(2);
        }
        return date;
    }
}
