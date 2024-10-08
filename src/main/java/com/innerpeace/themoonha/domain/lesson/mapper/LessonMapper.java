package com.innerpeace.themoonha.domain.lesson.mapper;

import com.innerpeace.themoonha.domain.lesson.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 강좌 매퍼
 * @author 손승완
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	손승완       최초 생성
 * 2024.08.25   손승완       강좌 상세보기 기능 추가
 * 2024.08.26   손승완       강사 상세보기 기능 추가
 * 2024.08.26   손승완       장바구니 및 신청 기능 추가
 * </pre>
 */
public interface LessonMapper {
    List<LessonDTO> selectLessonList(LessonListRequest lessonListRequest);
    List<ShortFormDTO> selectShortFormList(Long branchId);
    Optional<LessonDetailResponse> selectLessonDetail(Long lessonId);
    List<TutorLessonDetailDTO> selectTutorDetail(Long tutorId);
    List<CartResponse> selectCartList(Long memberId);
    int insertCart(CartRequest cartRequest);
    int deleteCart(@Param("cartIdList") List<Long> cartIdList,
                   @Param("memberId") Long memberId);
    int insertSugang(@Param("cartIdList") List<Long> cartIdList,
                     @Param("memberId") Long memberId);
    List<LessonEnrollResponse> selectLessonEnrollList(Long memberId);
    List<LessonEnrollResponse> selectLessonFieldEnrollList(Long memberId);
    List<TutorLessonResponse> selectTutorLessonList(Long memberId);
    int updateShortFormViewCount(ShortFormViewCountDTO viewCountDTO);
    int deleteCartById(@Param("cartId") Long cartId,
                       @Param("memberId") Long memberId);
}
