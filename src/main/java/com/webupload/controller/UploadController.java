package com.webupload.controller;

import com.webupload.domain.CustomType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@RestController
@RequestMapping("/api")
public class UploadController {

    // 当前项目默认存储图片路径
    @Value("${path.pic}")
    private String picPath;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(MultipartFile file) throws IOException {
        CustomType customType = new CustomType(200, "上传成功！");
        try {
            File baseFile = new File(picPath);
            if (!baseFile.exists()) {
                baseFile.mkdirs();
            }
            File realFile = new File(baseFile, file.getOriginalFilename());
            System.out.println("路径：" + realFile.getAbsolutePath());
            System.out.println("文件名:" + file.getOriginalFilename());

            // ---------------------  1.IO流写入 ----------------------
            byte[] bytes = file.getBytes();
            BufferedOutputStream buffStream =
                    new BufferedOutputStream(new FileOutputStream(realFile));
            buffStream.write(bytes);
            buffStream.close();

            // ---------------------  2.直接转存 ----------------------
            //file.transferTo(realFile); // 这个不行，springboot会自动存到tomcat临时路径去
        } catch (IOException e) {
            e.printStackTrace();
            customType.setCode(400);
            customType.setResult("上传失败！");
        }
        return ResponseEntity.ok(customType);
    }
}
