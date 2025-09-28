package com.songhanwu.messaging.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TestUserDAO { // data access object

    @Delete("DELETE FROM \"user\"")
    void deleteAll();
}

