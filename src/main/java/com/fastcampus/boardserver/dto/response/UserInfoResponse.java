package com.fastcampus.boardserver.dto.response;

import com.fastcampus.boardserver.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private UserDto userDto;
}
