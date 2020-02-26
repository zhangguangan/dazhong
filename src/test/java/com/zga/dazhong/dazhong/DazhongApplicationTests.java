package com.zga.dazhong.dazhong;

import com.zga.dazhong.wxs.mapper.WxMaterialMapper;
import com.zga.dazhong.wxs.modules.entity.WxMaterial;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DazhongApplicationTests {
    @Resource
    private WxMaterialMapper wxMaterialMapper;

    @Test
    void contextLoads() {
        WxMaterial wxMaterial = wxMaterialMapper.selectByPrimaryKey(1L);

    }

}
