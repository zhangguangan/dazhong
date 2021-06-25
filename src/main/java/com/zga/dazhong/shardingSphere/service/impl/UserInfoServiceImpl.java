package com.zga.dazhong.shardingSphere.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zga.dazhong.shardingSphere.entity.UserInfo;
import com.zga.dazhong.shardingSphere.mapper.UserInfoMapper;
import com.zga.dazhong.shardingSphere.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public int save(UserInfo entity) {
        return userInfoMapper.insert(entity);
    }

    @Override
    public List<UserInfo> getUserList(UserInfo userInfo) {
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.setEntity(userInfo);
        return userInfoMapper.selectList(queryWrapper);
    }
}
