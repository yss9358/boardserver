package com.fastcampus.boardserver.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserUpdatePasswordRequest {

    @NonNull
    private String beforePassword;
    @NonNull
    private String afterPassword;
}
