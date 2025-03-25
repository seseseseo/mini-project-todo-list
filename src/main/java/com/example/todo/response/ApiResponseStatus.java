package com.example.todo.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiResponseStatus {

    SUCCESS(200, "목록 조회 성공"),
    CREATED(201, "일정 등록 성공"),
    BAD_REQUEST(400, "잘못된 요청"),
    NOT_FOUND(404, "페이지를 찾을 수 없음"),
    INTERNAL_SERVER_ERROR(500, "서버 에러");

    private final int statusCode;
    private final String message;
}