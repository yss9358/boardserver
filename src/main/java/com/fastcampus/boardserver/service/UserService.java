package com.fastcampus.boardserver.service;

import com.fastcampus.boardserver.dto.UserDto;

public interface UserService {

    void register(UserDto userDto); // 유저 등록

    UserDto login(String id, String password); // 유저 로그인

    boolean isDuplicatedId(String id); // 아이디 중복체크

    UserDto getUserInfo(String userId); // 유저정보 가져오기

    void updatePassword(String id, String beforePassword, String afterPassword); // 비밀번호 변경

    void deleteId(String id, String password); // 아이디 삭제


}
