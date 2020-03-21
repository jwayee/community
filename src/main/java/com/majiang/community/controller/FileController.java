package com.majiang.community.controller;

import com.majiang.community.dto.FileDTO;
import com.majiang.community.provider.AliyunOSSProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class FileController {
    @Autowired
    AliyunOSSProvider OSSProvider;
    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDTO upload(MultipartHttpServletRequest request){
        MultipartFile file = request.getFile("editormd-image-file");
        String imgUrl = OSSProvider.checkImage(file);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl(imgUrl);
        return fileDTO;
    }
}

