package com.zga.dazhong.shardingSphere.service;

import com.zga.dazhong.shardingSphere.entity.UserInfo;

import java.util.List;

public interface UserInfoService {
    /**
     * 保存用户信息
     *
     * @param entity
     * @return
     */

    int save(UserInfo entity);

    /**
     * 查询所以用户信息
     *
     * @return
     */
    List<UserInfo> getUserList(UserInfo userInfo);

}
