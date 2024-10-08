package com.innerpeace.themoonha.domain.craft.controller;

import com.innerpeace.themoonha.domain.craft.dto.CraftMainResponse;
import com.innerpeace.themoonha.domain.craft.dto.SuggestionRequest;
import com.innerpeace.themoonha.domain.craft.dto.SuggestionResponse;
import com.innerpeace.themoonha.domain.craft.service.CraftService;
import com.innerpeace.themoonha.global.dto.CommonResponse;
import com.innerpeace.themoonha.global.util.Criteria;
import com.innerpeace.themoonha.global.util.MemberId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 문화공방 컨트롤러
 * @author 손승완
 * @since 2024.08.25
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.25  	손승완       최초 생성
 * 2024.08.26   손승완       프롤로그 상세 조회 구현
 * 2024.08.27   손승완       제안합니다 댓글 기능 구현
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/craft")
public class CraftController {
    private final CraftService craftService;
    @GetMapping("/list")
    public ResponseEntity<CraftMainResponse> craftMain(Criteria criteria, @MemberId Long memberId) {
        return ResponseEntity.ok(craftService.findCraftMain(criteria, memberId));
    }

    @GetMapping("/suggestion")
    public ResponseEntity<SuggestionResponse> suggestionList(Criteria criteria) {
        return ResponseEntity.ok(craftService.findSuggestionList(criteria));
    }

    @PostMapping("/suggestion")
    public ResponseEntity<CommonResponse> suggestionSave(@RequestBody SuggestionRequest suggestionRequest,
                                                         @MemberId Long memberId) {

        log.info("댓글 입력 값 : {}", suggestionRequest.getContent());
        return ResponseEntity.ok(craftService.addSuggestion(suggestionRequest, memberId));
    }

    @PostMapping("/prologue/{prologueId}")
    public ResponseEntity<CommonResponse> prologueLike(@PathVariable Long prologueId,
                                                       @MemberId Long memberId) {
        return ResponseEntity.ok(craftService.addPrologueLike(prologueId, memberId));
    }

    @PostMapping("/wishlesson/{wishLessonId}")
    public ResponseEntity<CommonResponse> wishLessonVote(@PathVariable Long wishLessonId,
                                                         @MemberId Long memberId) {
        return ResponseEntity.ok(craftService.addWishLessonVote(wishLessonId, memberId));
    }
}
