package com.wwq.core.controller;

import com.wwq.core.pojo.entity.Result;
import com.wwq.core.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Mr.Wang
 * @create: 2019-05-03 18:28
 **/
@RestController
@RequestMapping("upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("/uploadFile")
    public Result upload(MultipartFile file){
        //1.取文件的完整名称
        String filename = file.getOriginalFilename();

        try {
            //创建FastDFS的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
            //执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), filename, file.getSize());
            //拼接返回url和ip地址，拼装成完整的URL
            String url = FILE_SERVER_URL + path;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"上传失败");
        }
    }
}
