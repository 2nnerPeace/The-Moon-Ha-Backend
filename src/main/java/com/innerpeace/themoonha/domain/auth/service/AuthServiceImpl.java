package com.innerpeace.themoonha.domain.auth.service;

import com.innerpeace.themoonha.domain.auth.dto.JwtDTO;
import com.innerpeace.themoonha.domain.auth.dto.LoginRequest;
import com.innerpeace.themoonha.domain.auth.dto.SignUpRequest;
import com.innerpeace.themoonha.domain.auth.jwt.JwtTokenProvider;
import com.innerpeace.themoonha.domain.auth.mapper.AuthMapper;
import com.innerpeace.themoonha.global.entity.Member;
import com.innerpeace.themoonha.global.exception.CustomException;
import com.innerpeace.themoonha.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원가입/로그인 서비스 구현체
 * @author 최유경
 * @since 2024.08.25
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.25  	최유경       최초 생성
 * 2024.08.26  	최유경       로그인 메서드 생성
 * </pre>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final AuthMapper authMapper;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public int signUp(SignUpRequest request) {
        if(!checkAvailableUsername(request.getUsername()))
            throw new CustomException(ErrorCode.MEMBER_DUPLICATE);

        // 비밀번호 암호화
        String encodedPassword = encoder.encode(request.getPassword());

        Member member = Member.of(request,encodedPassword);

        int result = authMapper.insertMember(member);

        return result;
    }

    @Override
    public boolean checkAvailableUsername(String userName) {
        return !authMapper.findByUsername(userName).isPresent();
    }

    @Override
    public JwtDTO login(LoginRequest request) {
        // 1. 회원인지 확인하기
        Member member =  authMapper.findByUsername(request.getUsername())
                .orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 비밀번호 일치 여부 확인하기
        if(encoder.matches(member.getPassword(),request.getPassword()))
            throw new CustomException(ErrorCode.MEMBER_INCORRECT_AUTH);

        // 3. 토큰 발급하기
        JwtDTO jwtDTO = jwtTokenProvider.generateToken(member);
        if(jwtDTO==null)
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        return jwtDTO;
    }
}
