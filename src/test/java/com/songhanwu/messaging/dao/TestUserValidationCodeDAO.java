package com.songhanwu.messaging.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TestUserValidationCodeDAO { // data access object

    @Delete("DELETE FROM \"user_validation_code\"")
    void deleteAll();
}
