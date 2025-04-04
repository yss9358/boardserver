package com.fastcampus.boardserver.controller;

import com.fastcampus.boardserver.dto.UserDto;
import com.fastcampus.boardserver.dto.request.UserDeleteId;
import com.fastcampus.boardserver.dto.request.UserLoginRequest;
import com.fastcampus.boardserver.dto.request.UserUpdatePasswordRequest;
import com.fastcampus.boardserver.dto.response.LoginResponse;
import com.fastcampus.boardserver.dto.response.UserInfoResponse;
import com.fastcampus.boardserver.service.impl.UserServiceImpl;
import com.fastcampus.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserServiceImpl userService;

    private static LoginResponse loginResponse;
//    public UserController (UserServiceImpl userService) {
//        this.userService = userService;
//    }


    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDto userDto){
        if(UserDto.hasNullDataBeforeRegister(userDto)){
            throw new RuntimeException("회원가입 정보를 확인해주세요.");
        }
        userService.register(userDto);
    }

    @PostMapping("sign-in")
    public HttpStatus login(@RequestBody UserLoginRequest userLoginRequest,
                            HttpSession session){
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = userLoginRequest.getUserId();
        String password = userLoginRequest.getPassword();
        UserDto userDto = userService.login(id, password);

        if(userDto == null){
            return HttpStatus.NOT_FOUND;
        } else if (userDto != null) {
            loginResponse = LoginResponse.success(userDto);
            if(userDto.getStatus() == (UserDto.Status.ADMIN)){
                SessionUtil.setLoginAdminId(session,id);
            } else {
                SessionUtil.setLoginMemberId(session,id);
            }

            responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            throw new RuntimeException("Login ERROR! 유저 정보가 없거나 지원되지 않는 유저입니다.");
        }

        return HttpStatus.OK;
    }

    @GetMapping("my-info")
    public UserInfoResponse memberInfo(HttpSession session){
        String id = SessionUtil.getLoginMemberId(session);
        if(id == null) id = SessionUtil.getLoginAdminId(session);
        UserDto memberInfo = userService.getUserInfo(id);
        return new UserInfoResponse(memberInfo);
    }

    @PutMapping("logout")
    public void logout(HttpSession session){
        SessionUtil.clear(session);
    }

    @PatchMapping("password")
    public ResponseEntity<LoginResponse> updateUserPassword(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
                                                           HttpSession session){
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = SessionUtil.getLoginMemberId(session);
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            userService.updatePassword(id, beforePassword, afterPassword);
            ResponseEntity.ok(new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK));
        }catch (IllegalArgumentException e){
            log.error("updatePassword 실패", e);
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @DeleteMapping()
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteId userDeleteId,
                                                 HttpSession session){
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = SessionUtil.getLoginMemberId(session);

        try{
            userService.deleteId(id, userDeleteId.getPassword());
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("deleteId 실패");
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }



}
