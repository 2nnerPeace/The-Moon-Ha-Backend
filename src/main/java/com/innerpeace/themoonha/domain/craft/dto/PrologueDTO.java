package com.innerpeace.themoonha.domain.craft.dto;

import lombok.*;

/**
 * 문화공방 메인 페이지 프롤로그 DTO
 * @author 손승완
 * @since 2024.08.25
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.25  	손승완       최초 생성
 * </pre>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PrologueDTO {
    private Long prologueId;
    private Long prologueThemeId;
    private String tutorName;
    private String themeName;
    private String themeDescription;
    private String title;
    private String thumbnailUrl;
    private String videoUrl;
    private int likeCnt;
    private boolean alreadyLiked;
}
