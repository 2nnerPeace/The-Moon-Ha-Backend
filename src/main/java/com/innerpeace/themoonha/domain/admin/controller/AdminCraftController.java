package com.innerpeace.themoonha.domain.admin.controller;

import com.innerpeace.themoonha.domain.admin.dto.PrologueListAdminResponse;
import com.innerpeace.themoonha.domain.admin.dto.PrologueRegisterAdminRequest;
import com.innerpeace.themoonha.domain.admin.dto.PrologueRegisterV2AdminRequest;
import com.innerpeace.themoonha.domain.admin.dto.PrologueThemeListAdminResponse;
import com.innerpeace.themoonha.domain.admin.service.AdminCraftService;
import com.innerpeace.themoonha.domain.craft.dto.SuggestionDTO;
import com.innerpeace.themoonha.global.dto.CommonResponse;
import com.innerpeace.themoonha.global.util.Criteria;
import com.innerpeace.themoonha.global.util.MemberId;
import com.innerpeace.themoonha.global.vo.SuccessCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 어드민 문화공방 관리 컨트롤러
 * @author 최유경
 * @since 2024.08.30
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.30  	최유경       최초 생성
 * 2024.08.31   최유경       프롤로그 테마 기획 변경
 * 2024.09.01   최유경       제안합니다 조회
 * 2024.09.17   최유경       프롤로그 S3 업로드 분리
 * </pre>
 */
@RestController
@RequestMapping("/admin/craft")
@RequiredArgsConstructor
@Slf4j
public class AdminCraftController {
    private final AdminCraftService adminCraftService;

    /**
     * 테마 및 프롤로그 등록
     *
     * @param registerAdminRequest 테마 정보
     * @param thumbnailFile 영상 썸네일 사진 리스트
     * @param prologueVideoFile 영상 파일 리스
     * @return
     */
    @PostMapping("/prologue/register")
    public ResponseEntity<CommonResponse> PrologueAdd(@RequestPart("registerRequest") PrologueRegisterAdminRequest registerAdminRequest,
                                                      @RequestPart("thumbnailFile") List<MultipartFile> thumbnailFile,
                                                      @RequestPart("prologueVideoFile")List<MultipartFile> prologueVideoFile,
                                                      @MemberId Long memberId){
        adminCraftService.addPrologue(memberId, registerAdminRequest, thumbnailFile, prologueVideoFile);

        return ResponseEntity.ok(CommonResponse.from(SuccessCode.ADMIN_PROLOGUE_REGISTER_SUCCESS.getMessage()));
    }

    @PostMapping("/prologue/register/v2")
    public ResponseEntity<CommonResponse> PrologueAdd(@RequestPart("registerRequest") PrologueRegisterV2AdminRequest registerAdminRequest,
                                                      @MemberId Long memberId){
        adminCraftService.addPrologue(memberId, registerAdminRequest);

        return ResponseEntity.ok(CommonResponse.from(SuccessCode.ADMIN_PROLOGUE_REGISTER_SUCCESS.getMessage()));
    }


    /**
     * 테마 전체 조회
     *
     * @return 테마 리스트
     */
    @GetMapping("/prologue/theme/list")
    public ResponseEntity<List<PrologueThemeListAdminResponse>> PrologueThemeList(){
        List<PrologueThemeListAdminResponse> prologueThemeList = adminCraftService.findPrologueThemeList();
        return ResponseEntity.ok(prologueThemeList);
    }

    /**
     * 테마별 프롤로그 조회
     *
     * @param prologueThemeId 테마 ID
     * @return 프롤로그 리스트
     */
    @GetMapping("/prologue/theme/{prologueThemeId}")
    public ResponseEntity<List<PrologueListAdminResponse>> PrologueThemeList(@PathVariable Long prologueThemeId){
        List<PrologueListAdminResponse> prologueList = adminCraftService.findPrologueList(prologueThemeId);
        return ResponseEntity.ok(prologueList);
    }


    /**
     * 제안합니다 조회
     *
     * @param criteria 페이징처리
     * @return 제안합니다 리스트
     */
    @GetMapping("/suggestion/list")
    public ResponseEntity<List<SuggestionDTO>> SuggestionList(Criteria criteria){
        List<SuggestionDTO> suggestionDTOList = adminCraftService.findSuggestionList(criteria);
        return ResponseEntity.ok(suggestionDTOList);
    }

}
