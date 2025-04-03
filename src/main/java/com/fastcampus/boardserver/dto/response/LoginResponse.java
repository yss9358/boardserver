package com.fastcampus.boardserver.dto.response;

import com.fastcampus.boardserver.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponse {
    enum LoginStatus {
        SUCCESS, FAIL, DELETED
    }

    @NonNull
    private LoginStatus loginStatus;
    private UserDto userDto;

    private static final LoginResponse FAIL = new LoginResponse(LoginStatus.FAIL);

    public static LoginResponse success(UserDto userDto) {
        return new LoginResponse(LoginStatus.SUCCESS, userDto);
    }
}
