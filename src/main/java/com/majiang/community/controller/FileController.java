package com.majiang.community.controller;

import com.majiang.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {
    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDTO upload(){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/avatar-max-img.png");
        return fileDTO;
    }
    @RequestMapping("/file/uploadTOPC")
    public FileDTO uploadTOPC(){
        FileDTO fileDTO = new FileDTO();
        return fileDTO;
    }
}
