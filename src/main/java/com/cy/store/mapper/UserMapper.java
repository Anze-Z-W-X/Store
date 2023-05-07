package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Date;

//用户模块的接口
public interface UserMapper {

    @Insert("""
            insert into t_user (username, password, salt, phone, email, gender, avatar,is_delete, created_user, created_time, modified_user, modified_time)
                    VALUES(#{username}, #{password}, #{salt}, #{phone}, #{email}, #{gender},
                    #{avatar}, #{isDelete}, #{createdUser}, #{createdTime}, #{modifiedUser}, #{modifiedTime})
                """)
    Integer insert(User user);

    @Select("""
            SELECT * FROM t_user
            WHERE username = #{username}
            """)
    @Results(id="UserMap",value = {
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "is_delete",property = "isDelete"),
            @Result(column = "created_user",property = "createdUser"),
            @Result(column = "created_time",property = "createdTime"),
            @Result(column = "modified_user",property = "modifiedUser"),
            @Result(column = "modified_time",property = "modifiedTime")
    })
    User findByUserName(String username);

    @Select("""
        select * from t_user where uid=#{uid}
            """)
    @ResultMap("UserMap")
    User findByUid(Integer uid);

    @Update("""
        update t_user set password=#{password},modified_user=#{modifiedUser},
        modified_time=#{modifiedTime}
        where uid=#{uid}
            """)
    Integer updatePasswordByUid(Integer uid, String password, String modifiedUser, Date modifiedTime);

    @Update("<script> " +
            "UPDATE t_user SET " +
            "<if test='phone!=null '> phone=#{phone},</if>" +
            "<if test='gender!=null '> gender=#{gender},</if>" +
            "<if test='email!=null'> email=#{email},</if> "+
            "modified_user=#{modifiedUser},modified_time=#{modifiedTime} where uid=#{uid}"+
            "</script>")
    @ResultMap("UserMap")
    Integer updateInfoByUid(User user);

    @Update("""
        update t_user
        set
        avatar = #{avatar},
        modified_user=#{modifiedUser},
        modified_time=#{modifiedTime}
        where uid=#{uid};
            """)
    Integer updateAvatarByUid(@Param("uid")Integer uid,
                              @Param("avatar")String avatar,
                              @Param("modifiedUser")String modifiedUser,
                              @Param("modifiedTime")Date modifiedTime);
}
