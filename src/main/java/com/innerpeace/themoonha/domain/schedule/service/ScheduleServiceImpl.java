package com.innerpeace.themoonha.domain.schedule.service;

import com.innerpeace.themoonha.domain.schedule.dto.ScheduleWeeklyResponse;
import com.innerpeace.themoonha.domain.schedule.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 스케줄 서비스 구현체
 * @author 조희정
 * @since 2024.09.07
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.09.07  	조희정       최초 생성
 * 2024.09.07  	조희정       스케줄 주간 보기 구현
 * </pre>
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleMapper scheduleMapper;

    /**
     * 주간 스케줄 가져오기 (지난주, 이번주, 다음주)
     * @param memberId
     * @param standardDates
     * @return
     */
    @Override
    public List<List<ScheduleWeeklyResponse>> findWeeklySchedules(Long memberId, List<String> standardDates) {
        // 지난주, 이번주, 다음주 기준 일요일 계산

        List<ScheduleWeeklyResponse> schedules = scheduleMapper.selectWeeklySchedules(memberId, standardDates);

        Map<String, List<ScheduleWeeklyResponse>> groupedSchedules = schedules.stream()
                .collect(Collectors.groupingBy(ScheduleWeeklyResponse::getStandardDate));

        return standardDates.stream()
                .map(standard -> groupedSchedules.getOrDefault(standard, new ArrayList<>()))
                .collect(Collectors.toList());
    }
}
