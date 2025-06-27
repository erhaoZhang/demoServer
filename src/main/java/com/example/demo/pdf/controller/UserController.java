package com.example.demo.pdf.controller;

import com.example.demo.pdf.bean.User;
import com.example.demo.pdf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.List;
import java.util.Optional;

/**
 * ClassName: userController
 * Package: com.example.demo.pdf.controller
 * Description:
 *
 * @Author 张尔豪
 * @Create 2025/6/19 22:59
 * @Version 1.0
 */
@RestController
@RequestMapping(path="/api/v1")
public class UserController {
    private static final String UPLOAD_DIR = "D:/initTrainVue/uploads";
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "user",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public User addNewUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping(path = "users")
    public List<User> getAllUsers() {
        List<User> all = userRepository.findAll();
        return all;
    }
    @GetMapping(path = "byId")
    public User getUsersById(String id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    @PostMapping(path = "upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获取文件部分
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();

//        确保目录存在
        File uploads = new File(UPLOAD_DIR);
        if(!uploads.exists()){
            uploads.mkdir();
        }

//        保存文件
        File file = new File(uploads,fileName);
        try {
            InputStream inputStream = filePart.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int byteRead;
            while ((byteRead = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,byteRead);
            }
        }catch (Exception e){

        }

        response.getWriter().write("文件上传成功：" + fileName);

    }

    @GetMapping(path = "getImg")
    public void getImage(String imageName,HttpServletResponse response){
        File imageFile = new File(UPLOAD_DIR + "/" + imageName);//图片的实际路径
        response.setContentType("application/octet-stream");//设置响应内容类型JPEG图片
        try {
            FileInputStream in = new FileInputStream(imageFile);
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int byteRead;
            while ((byteRead = in.read(buffer)) != -1){
                out.write(buffer,0,byteRead);
            }
        }catch (IOException e){
            e.printStackTrace();//处理IO异常
        }
    }
}

