package com.zga.dazhong.wxs.wxs;

import com.zga.dazhong.common.MaterialEnums;
import com.zga.dazhong.wxs.gws.GwsMaterialFacade;
import com.zga.dazhong.wxs.modules.entity.WxMaterial;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * Created by zhangguangan on 2020/2/26
 * description:
 */
@Service
public class MaterialFacade {
    @Resource
    private GwsMaterialFacade gwsMaterialFacade;


    public WxMaterial uploadFile(byte[] fileBytes, String fileName, Integer fileType) {
        // 1、上传到微信
        String materialId = gwsMaterialFacade.uploadMaterial(fileBytes, fileName, MaterialEnums.getByType(fileType).getMaterialDesc());

        // 2、获取素材的url
        String url = gwsMaterialFacade.getMaterial(materialId);

        // 3、存放到数据库
        WxMaterial wxMaterial = new WxMaterial();
        wxMaterial.setMaterialType(fileType);
        wxMaterial.setMaterialWxId(materialId);
        wxMaterial.setMaterialWxUrl(url);
        return wxMaterial;
    }
}
