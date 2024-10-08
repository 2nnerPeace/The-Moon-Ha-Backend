package com.innerpeace.themoonha.domain.admin.dto;

import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 어드민 강좌 조회 응답 dto
 * @author 최유경
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	최유경       최초 생성
 * 2024.09.09  	최유경       강좌 상세 조회
 * </pre>
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LessonDetailAdminResponse {
    private Long lessonId;
    private Long branchId;
    private String branchName;
    private Long categoryId;
    private String category;
    private Long memberId;
    private String tutorName;
    private String title;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String summary;
    private int cnt;
    private int cost;
    private String curriculum;
    private String supply;
    private String previewVideoUrl;
    private String thumbnailUrl;
    private String place;
    private String day;
    private int target;
    private int isOnline;
    private int onlineCost;
    private Date createdAt;
}

