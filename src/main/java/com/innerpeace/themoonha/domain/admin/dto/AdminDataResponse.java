package com.innerpeace.themoonha.domain.admin.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminDataResponse {
    List<LessonBranchDTO> lessonBranchDTOList;

    public static AdminDataResponse from(List<LessonBranchDTO> lessonBranchList){
        return AdminDataResponse.builder()
                .lessonBranchDTOList(lessonBranchList)
                .build();
    }
}
