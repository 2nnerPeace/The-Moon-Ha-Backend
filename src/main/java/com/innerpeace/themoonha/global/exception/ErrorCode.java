package com.innerpeace.themoonha.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    MEMBER_NOT_FOUND(404, "존재하지 않은 유저입니다."),
    LESSON_NOT_FOUND(404, "강좌가 존재하지 않습니다."),
    SHORTFORM_NOT_FOUND(404, "숏폼이 존재하지 않습니다.");
    private final int status;
    private final String message;

}
