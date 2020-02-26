package com.zga.dazhong.controller;

import com.baidu.ueditor.ActionEnter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;


@RestController
@RequestMapping("/api/ueditor")
@Log4j2
public class UEditroController {

    @Autowired
    private HttpServletRequest request;
    @RequestMapping("/ueditorConfig")
    public void getUEditorConfig(HttpServletResponse response){
        String rootPath = "src/main/resources/static";
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取Ueditor的配置文件
     * @return
     */
    @GetMapping("/config")
    public String getConfig() {
        return "{\n" +
                "        \"imageActionName\": \"uploadimage\",\n" +
                "            \"imageFieldName\": \"file\",\n" +
                "            \"imageMaxSize\": 2048000,\n" +
                "            \"imageAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"],\n" +
                "        \"imageCompressEnable\": true,\n" +
                "            \"imageCompressBorder\": 1600,\n" +
                "            \"imageInsertAlign\": \"none\",\n" +
                "            \"imageUrlPrefix\": \"\",\n" +
                "            \"imagePathFormat\": \"/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\",\n" +
                "\n" +
                "    \"videoActionName\": \"uploadvideo\",\n" +
                "    \"videoFieldName\": \"file\",\n" +
                "    \"videoPathFormat\": \"/ueditor/jsp/upload/video/{yyyy}{mm}{dd}/{time}{rand:6}\",\n" +
                "    \"videoUrlPrefix\": \"\",\n" +
                "    \"videoMaxSize\": 102400000,\n" +
                "    \"videoAllowFiles\": [\n" +
                "        \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                "        \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\", \".webm\", \".mp3\", \".wav\", \".mid\"],\n" +
                "\n" +
                "        /* 上传文件配置 */\n" +
                "        \"fileActionName\": \"uploadfile\",\n" +
                "            \"fileFieldName\": \"file\",\n" +
                "            \"filePathFormat\": \"/ueditor/jsp/upload/file/{yyyy}{mm}{dd}/{time}{rand:6}\",\n" +
                "            \"fileUrlPrefix\": \"\",\n" +
                "            \"fileMaxSize\": 51200000,\n" +
                "            \"fileAllowFiles\": [\n" +
                "        \".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\",\n" +
                "                \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                "                \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\", \".webm\", \".mp3\", \".wav\", \".mid\",\n" +
                "                \".rar\", \".zip\", \".tar\", \".gz\", \".7z\", \".bz2\", \".cab\", \".iso\",\n" +
                "                \".doc\", \".docx\", \".xls\", \".xlsx\", \".ppt\", \".pptx\", \".pdf\", \".txt\", \".md\", \".xml\"]\n" +
                "    }";
    }

    @PostMapping("/uploadvideo")
    public String uploadVidio(MultipartFile file, HttpServletRequest request) {
        String result = "";
        if(!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();

            // 这里写你的文件上传逻辑
            // String imgPath = fileUtil.uploadImg(file);

            String imgPath = uploadImg(file, request);
            result = "{\n" +
                    "    \"state\": \"SUCCESS\",\n" +
                    "    \"url\": \"" + imgPath + "\",\n" +
                    "    \"title\": \"" + originalFileName + "\",\n" +
                    "    \"original\": \"" + originalFileName + "\"\n" +
                    "}";
        }
        return result;
    }

    /**
     * Ueditor上传文件
     * 这里以上传图片为例，图片上传后，imgPath将存储图片的保存路径，返回到编辑器中做展示
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String result = "";
        if(!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();

            // 这里写你的文件上传逻辑
            // String imgPath = fileUtil.uploadImg(file);

            String imgPath = uploadImg(file, request);
            result = "{\n" +
                    "    \"state\": \"SUCCESS\",\n" +
                    "    \"url\": \"" + imgPath + "\",\n" +
                    "    \"title\": \"" + originalFileName + "\",\n" +
                    "    \"original\": \"" + originalFileName + "\"\n" +
                    "}";
        }
        return result;
    }
    public String uploadImg(MultipartFile file,
                            HttpServletRequest request) {
        try {
            String path = request.getSession().getServletContext()
                    .getRealPath("ueditor/jsp/upload/image");
            String ct = file.getContentType() ;
            String fileType = "";
            if (ct.indexOf("/")>0) {
                fileType = ct.substring(ct.indexOf("/")+1);
            }
//            String fileName = UUID.randomUUID() + "." + fileType;
            String fileName = file.getOriginalFilename();
            File targetFile = new File(path);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            File targetFile2 = new File(path+"/"+fileName);
            if (!targetFile2.exists()) {
                targetFile2.createNewFile();
            }
            // 保存
            try {
                file.transferTo(targetFile2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "/dazhong/ueditor/jsp/upload/image/"+fileName;
        } catch (Exception e) {
            log.error("上传文件错误：", e);
            return null;
        }
    }
}
