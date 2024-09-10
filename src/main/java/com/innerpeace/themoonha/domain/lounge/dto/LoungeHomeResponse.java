package com.innerpeace.themoonha.domain.lounge.dto;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 라운지 홈 응답 DTO
 * @author 조희정
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	조희정       최초 생성
 * </pre>
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoungeHomeResponse {

    private LoungeInfoDTO loungeInfo;
    private List<LoungePostDTO> loungeNoticePostList;
    private List<LoungePostDTO> loungePostList;
    private AttendanceMembersResponse attendanceList;
    private List<LoungeMemberDTO> loungeMemberList;

    public static LoungeHomeResponse of(LoungeInfoDTO loungeInfo,List<LoungePostDTO> loungePostList,
                                        AttendanceMembersResponse attendanceList, List<LoungeMemberDTO> loungeMemberList) {

        // 공지 게시글 리스트 생성
        List<LoungePostDTO> loungeNoticePostList = loungePostList.stream()
                .filter(LoungePostDTO::isNoticeYn)
                .map(post -> LoungePostDTO.of(post, null))
                .collect(Collectors.toList());

        return LoungeHomeResponse.builder()
                .loungeInfo(loungeInfo)
                .loungeNoticePostList(loungeNoticePostList)
                .loungePostList(loungePostList)
                .attendanceList(attendanceList)
                .loungeMemberList(loungeMemberList)
                .build();
    }

}
