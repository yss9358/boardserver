package com.fastcampus.boardserver.service.impl;

import com.fastcampus.boardserver.dto.UserDto;
import com.fastcampus.boardserver.exception.DuplicatedIdException;
import com.fastcampus.boardserver.mapper.UserDtoMapper;
import com.fastcampus.boardserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.fastcampus.boardserver.utils.SHA256Util.encryptSHA256;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Override
    public void register(UserDto userDto) {

        boolean duplicatedIdResult = isDuplicatedId(userDto.getUserId());

        if(duplicatedIdResult){
            throw new DuplicatedIdException("중복된 아이디입니다.");
        }
        userDto.setCreateTime(new Date());
        userDto.setPassword(encryptSHA256(userDto.getPassword()));

        int insertCount = userDtoMapper.insertUserProfile(userDto);

        if(insertCount != 1){
            log.error("insertMember Error : {}", userDto.toString());
            throw new RuntimeException(
                    "insertUser ERROR! 회원가입 메서드를 확인해주세요 \n" + "Params : " + userDto
            );
        }

    }

    @Override
    public UserDto login(String id, String password) {
        String cryptPassword = encryptSHA256(password);
        UserDto userDto = userDtoMapper.findByIdAndPassword(id, cryptPassword);
        return userDto;
    }

    @Override
    public boolean isDuplicatedId(String id) {
        return userDtoMapper.idCheck(id) == 1;
    }

    @Override
    public UserDto getUserInfo(String userId) {
        return null;
    }

    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        String cryptPaasword = encryptSHA256(beforePassword);
        UserDto userDto = userDtoMapper.findByIdAndPassword(id, cryptPaasword);

        if(userDto != null){
            userDto.setPassword(encryptSHA256(afterPassword));
            int insertCount = userDtoMapper.updateUserProfile(userDto);
        } else {
            log.error("updatePassword Error : {}", userDto.toString());
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public void deleteId(String id, String password) {
        String cyryptPassword = encryptSHA256(password);
        UserDto userDto = userDtoMapper.findByIdAndPassword(id, cyryptPassword);
        if(userDto != null){
            int deleteCount = userDtoMapper.deleteUserProfile(id);
        } else {
            log.error("deleteId Error : {}", userDto.toString());
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }
}
