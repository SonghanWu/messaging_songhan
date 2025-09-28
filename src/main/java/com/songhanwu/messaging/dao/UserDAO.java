package com.songhanwu.messaging.dao;

import java.util.Date;
import java.util.List;

import com.songhanwu.messaging.dto.UserDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDAO { // data access object

    @Insert("INSERT INTO \"user\" (\"username\", \"nickname\", \"password\", \"register_time\", \"gender\", \"email\", \"is_valid\") VALUES " +
            "(#{username}, #{nickname}, #{password}, #{registerTime}, #{gender}, #{email}, #{isValid})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(UserDTO userDTO);

    @Select("SELECT * FROM \"user\" WHERE \"username\"=#{username}")
    UserDTO selectByUsername(String username);

    @Update("UPDATE \"user\" SET \"is_valid\" = 1 WHERE \"id\" = #{userId}")
    void updateToValid(int userId);

    @Update("UPDATE \"user\" SET \"login_token\" = #{loginToken}, \"last_login_time\" = #{loginTime} WHERE \"id\" = #{userId}")
    void login(String loginToken, Date loginTIme, int userId);

    @Select("SELECT * FROM \"user\" WHERE \"id\"=#{userId}")
    UserDTO selectByUserId(int userId);

    @Select("SELECT * FROM \"user\" WHERE \"username\" LIKE '%${keyword}%' OR \"nickname\" LIKE '%${keyword}%'")
    List<UserDTO> selectByKeyword(String keyword);
}

//public class UserDAOImpl implements UserDAO {
//
//    @Override
//    public void insert(UserDTO userDTO) {
//
//    }
//
//    @Override
//    public UserDTO selectByUsername(String username) {
//        return null;
//    }
//}
//
//// OOP = object oriented programming
