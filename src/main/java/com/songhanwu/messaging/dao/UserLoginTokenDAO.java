package com.songhanwu.messaging.dao;

import com.songhanwu.messaging.dto.UserLoginTokenDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserLoginTokenDAO { // data access object

    @Insert("INSERT INTO \"user_login_token\" (\"user_id\", \"login_token\", \"login_time\") VALUES " +
            "(#{userId}, #{loginToken}, #{loginTime})")
    void insert(UserLoginTokenDTO userLoginTokenDTO);


    @Select("SELECT * FROM \"user_login_token\" WHERE \"login_token\" = #{loginToken}")
    UserLoginTokenDTO selectByLoginToken(String loginToken);

    @Delete("DELETE FROM \"user_login_token\" WHERE \"id\" = #{id}")
    void deleteByUserLoginTokenId(int id);
}
