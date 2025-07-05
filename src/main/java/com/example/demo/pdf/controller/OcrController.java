package com.example.demo.pdf.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * ClassName: OcrController
 * Package: com.example.demo.pdf.controller
 * Description:
 *
 * @Author 张尔豪
 * @Create 2025/7/5 19:07
 * @Version 1.0
 */
@RestController
@RequestMapping(path="/api/ocr")
public class OcrController {

    @Value("${ocr.apikey}")
    private String clientId;
    @Value("${ocr.secretkey}")
    private String clientSecret;

    private static final String UPLOAD_DIR = "D:/initTrainVue/uploads/OCR";

    private String ACCESS_TOKEN;

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

//        将图片文件转base64为方便识别
        String base64Img = toBase64(file);

//        1.先去获取access_token
        getAccessToken();
//        2.开始识别银行卡
        JSONObject brand = getBrand(base64Img);


//        设置返回的编码格式
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type","text/html; charset=UTF-8");
        response.getWriter().write(brand + "");

    }

//    获取OCR中ACCESS_token (通过百度AI技术平台官网自己的应用的API KEY与 Secret Key)
    private void getAccessToken(){
//        1.获取请求token地址(基础地址)
        String authHost= "https://aip.baidubce.com/oauth/2.0/token";

        try {
            URL realUrl = new URL(authHost);
//            打开和URL的之间的链接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
//            百度推荐使用的post请求
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            //设置其Header的Content-Type参数为application/x-www-form-urlencoded
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//            获取输出流
            OutputStream os = connection.getOutputStream();
//            写入参数
            String parameter = "grant_type=" + URLEncoder.encode("client_credentials","utf-8") + "&client_id=" + URLEncoder.encode(clientId + "","utf-8")
                    + "&client_secret=" + URLEncoder.encode(clientSecret + "","utf-8");
            os.write(parameter.getBytes("UTF-8"));
//            关闭输出流
            os.close();
//            获取输入流
            InputStream inputStream = connection.getInputStream();

//            定义 BufferdReader输入流来读取URL响应
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String result ="";
            String line;
            while ((line = in.readLine()) != null){
                result += line;
            }
//            关闭输入流
            inputStream.close();
            System.out.println("accessResult" + result);
            JSONObject jsonObject = new JSONObject(result);
            ACCESS_TOKEN = jsonObject.getString("access_token");
        }catch (Exception e){
            System.err.printf("获取token失败");
            e.printStackTrace(System.err);
        }

    }
//    图片转base64
    private String toBase64(File imgFile){
//        将图片文件转化为字节数组字符串，并对其进行base64编码处理
//        进行base64编码处理
        byte[] data = null;
//        读取图片数组
        try{
            InputStream in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
//      对字符数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
//        返回Base64编码过的字节数组字符串
        return  encoder.encode(data);
    }


//      获取Accesstoken后开始识别银行卡号
    private JSONObject getBrand(String imgeBase64){
        //        1.获取请求token地址(基础地址)
        String ocrBrandHost= "https://aip.baidubce.com/rest/2.0/ocr/v1/bankcard?access_token=" + ACCESS_TOKEN;

        try {
            URL realUrl = new URL(ocrBrandHost);
//            打开和URL的之间的链接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
//            百度推荐使用的post请求
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            //设置其Header的Content-Type参数为application/x-www-form-urlencoded
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//            获取输出流
            OutputStream os = connection.getOutputStream();
//            写入参数
            String parameter = "image=" + URLEncoder.encode(imgeBase64,"utf-8");
            os.write(parameter.getBytes("UTF-8"));
//            关闭输出流
            os.close();
//            获取输入流
            InputStream inputStream = connection.getInputStream();

//            定义 BufferdReader输入流来读取URL响应
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String result ="";
            String line;
            while ((line = in.readLine()) != null){
                result += line;
            }
//            关闭输入流
            inputStream.close();
            System.out.println("ocrBrandResult" + result);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject ocrBrand= jsonObject.getJSONObject("result");

            return ocrBrand;
        }catch (Exception e){
            System.err.printf("获取token失败");
            e.printStackTrace(System.err);
            return null;
        }
    }

}
