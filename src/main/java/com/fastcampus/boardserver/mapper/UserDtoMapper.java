package com.fastcampus.boardserver.mapper;

import com.fastcampus.boardserver.dto.UserDto;
import org.apache.ibatis.annotations.Param;

public interface UserDtoMapper {

    public UserDto getUserProfile(@Param("id") String id);

    public int insertUserProfile(UserDto userDto);

    public int updateUserProfile(UserDto userDto);

    public int deleteUserProfile(@Param("id") String id);

    public UserDto findByIdAndPassword(@Param("id") String id, @Param("password") String password);

    public int idCheck(@Param("id") String id);

    public int updatePassword(UserDto userDto);


}
