package com.github.zipzou.chat.chatback.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

import com.github.zipzou.chat.chatback.dao.bean.MessageType;
import com.github.zipzou.chat.chatback.service.MessageService;
import com.github.zipzou.chat.chatback.service.WsSessionSupport;
import com.github.zipzou.chat.chatback.vo.UserBasicVo;
import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.res.AppStatus;
import com.github.zipzou.chat.chatback.vo.res.EmptyVo;
import com.github.zipzou.chat.chatback.vo.res.MessageToSendVo;
import com.github.zipzou.chat.chatback.vo.res.ResponseVo;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;

/**
 * 
 */
@Log4j2
@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "*")
public class FileController {
  
  private @Autowired MessageService msgSender;
  private @Autowired UserController userController;

  @PostMapping("/upload")
  public ResponseVo<ValueObject> upload(@RequestParam("file") MultipartFile file, @RequestParam("accessToken") String ak, @RequestParam("from") String fromUUID, @RequestParam("to") String toUUID) {

    UserBasicVo toCheckStatusUser = new UserBasicVo();
    toCheckStatusUser.setAccessToken(ak);
    toCheckStatusUser.setUserUUID(fromUUID);
    ValueObject status = userController.checkStatus(toCheckStatusUser);

    if (null == status || !(status instanceof ResponseVo) || 200 != ((ResponseVo) status).getCode() || ((ResponseVo<Integer>) status).getData() != AppStatus.Ready.code()) {
      return ResponseVo.fail("无权限访问", "当前用户未登录，无法上传文件");
    }

    String filename = file.getOriginalFilename();

    File folderPath = new File(System.getProperty("user.dir"), "files");
    if (!folderPath.exists()) {
      log.info("创建目录: " + folderPath.getAbsolutePath());
      folderPath.mkdirs();
    }

    File destFile = new File(folderPath, filename);
    try {
      if (destFile.exists()) {
        destFile.delete();
      }
      file.transferTo(destFile);
      MessageToSendVo<EmptyVo> resData = new MessageToSendVo<EmptyVo>();

      int extIndex = filename.lastIndexOf("."); // 获取最后后缀名
      String ext = "";
      if (-1 < extIndex) {
        ext = filename.substring(extIndex + 1);
      }
      DateTime now = DateTime.now();
      resData.setFileType(0L);
      resData.setContent("文件发送成功");
      resData.setCreateDate(now.toDate());
      resData.setExtension(ext);
      resData.setFileUrl("http://127.0.0.1:8080/file/" + filename);
      resData.setFrom(WsSessionSupport.sharedInstance().getUsername(fromUUID));
      resData.setTo(WsSessionSupport.sharedInstance().getUsername(toUUID));
      resData.setId(now.getMillis());
      resData.setReadStatus(false);
      resData.setType(MessageType.Binary.code());

      msgSender.send(fromUUID, toUUID, (MessageToSendVo) resData);

      return ResponseVo.success("上传成功", resData);
    } catch (IllegalStateException | IOException e) {
      e.printStackTrace();
    }

    return ResponseVo.fail("上传失败", "文件上传过程中发现错误，请稍后重试");
  }
  
  @GetMapping("/{filename}")
  public ResponseEntity<FileSystemResource> getFile(@PathVariable String filename) {
    File targetFile = new File(new File(System.getProperty("user.dir"), "files"), filename);
    int lastIndex = filename.lastIndexOf(".");
    String ext = "";
    if (-1 < lastIndex) {
      ext = filename.substring(lastIndex + 1);
    }
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");
    headers.add("Last-Modified", new Date().toString());
    if (Pattern.matches("svg", ext)) {
      headers.add("Content-Type", "image/svg+xml");
    } else if (Pattern.matches("png", ext)) {
      headers.add("Content-Type", MediaType.IMAGE_PNG_VALUE);
    } else if (Pattern.matches("jpg|jpeg", ext)) {
      headers.add("Content-Type", MediaType.IMAGE_JPEG_VALUE);
    } else if (Pattern.matches("png", ext)) {
      headers.add("Content-Type", MediaType.IMAGE_PNG_VALUE);
    } else {
      // headers.add("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
      headers.add("Content-Disposition", "attachment; filename=" + filename);
      if (targetFile.exists()) {
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(targetFile.length()).body(new FileSystemResource(targetFile));
      }
    }
    if (targetFile.exists()) {
      return ResponseEntity.ok().headers(headers).contentLength(targetFile.length()).body(new FileSystemResource(targetFile));
    } else {
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
  } 
}
