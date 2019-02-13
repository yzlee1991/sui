package com.yzlee.sui.server.db.mapper;

import com.yzlee.sui.server.db.entity.Auth;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:54
 */
public interface AuthMapper {

    @Select("select * from auth where name=#{name} and password=#{password}")
    Auth get(@Param("name") String name, @Param("password") String password);

}
