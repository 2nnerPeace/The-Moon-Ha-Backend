package com.innerpeace.themoonha.domain.auth.jwt;

import com.innerpeace.themoonha.domain.auth.dto.JwtDTO;
import com.innerpeace.themoonha.global.entity.Member;
import com.innerpeace.themoonha.global.exception.CustomException;
import com.innerpeace.themoonha.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * JWT 생성,추출
 * @author 최유경
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	최유경       최초 생성
 * 2024.08.27  	최유경       getSigningKey refactor, 토큰 재발급 로직
 * 2024.09.06   최유경       액세스 토큰 cookie로 변경
 * </pre>
 */
@Slf4j
@Component
@PropertySource(value={"classpath:application.properties"})
public class JwtTokenProvider {
    @Value("${jwt.access.expire.time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.refresh.expire.time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     *  JWT 비밀 키를 초기화하는 메서드
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 생성
     *
     * @param member 토큰 발행 시 필요한 Member
     * @return JwtDTO
     */
    public JwtDTO generateToken(Member member){
        long now = (new Date()).getTime();

        // AccessToken 생성
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(member.getMemberId()))
                .claim("memberId", member.getMemberId())
                .claim("username", member.getUsername())
                .claim("auth", member.getMemberRole().getRole())
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        // RefreshToken 생성
        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(member.getMemberId()))
                .claim("memberId", member.getMemberId())
                .claim("username", member.getUsername())
                .claim("auth", member.getMemberRole().getRole())
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return JwtDTO.of(accessToken,refreshToken);
    }

    /**
     * 토큰 재발급
     *
     * @param refreshToken 리프레스 토큰
     * @return
     */
    public JwtDTO regenerateAccessToken(String refreshToken){
        Claims claims = parseClaims(refreshToken);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        long now = (new Date()).getTime();

        // AccessToken 생성
        String accessToken = Jwts.builder()
                .setSubject(claims.getSubject())
                .claim("memberId", claims.get("memberId"))
                .claim("username", claims.get("username"))
                .claim("auth", claims.get("auth"))
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return JwtDTO.of(accessToken,refreshToken);
    }

    /**
     * JWT 복호화
     *
     * @param accessToken
     * @return Authentication
     */
    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져옴
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        log.info("getAuthentication ROLE : {}", authorities);

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * JWT Claim 추출
     *
     * @apiNote JWT 토큰 안의 Claim 정보를 추출
     * @param token
     * @return
     */
    public Claims parseClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(this.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    /**
     * JWT Claim 추출
     *
     * @apiNote JWT 토큰 안의 Claim 정보를 추출
     * @param token
     * @return
     */
    public Claims parseMemberId(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(this.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    /**
     * JWT 검증
     *
     * @param token accessToken
     * @return boolean
     */
    public boolean validateToken(String token) {
        try{
            log.info("validateToken : {}", token);
            Jwts.parserBuilder()
                    .setSigningKey(this.getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 JWT 토큰입니다. ", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("제공되지 않은 JWT 토큰입니다. ", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT Claim 문자열이 비어있습니다. ", e);
        } catch (Exception e){
            log.info("예상치 못한 오류가 발생했습니다. ", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return false;
    }

    /**
     * Request Header 로부터 JWT 토큰을 추출
     *
     * @param request
     * @return token
     */
    public JwtDTO resolveToken(HttpServletRequest request){
        String accessToken = null;
        String refreshToken = null;

        // Token 추출
        for(Cookie cookie : request.getCookies()){
            if("accessToken".equals(cookie.getName())) {
                accessToken = cookie.getValue();
            }
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }
        }
        log.info("resolveToken : {}, {}" , accessToken, refreshToken);
        return JwtDTO.of(accessToken,refreshToken);
    }

}
