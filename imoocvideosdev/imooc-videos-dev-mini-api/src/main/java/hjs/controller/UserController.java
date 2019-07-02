package hjs.controller;

import hjs.component.Const;
import hjs.pojo.Users;
import hjs.pojo.UsersReport;
import hjs.pojo.vo.PublisherVideo;
import hjs.pojo.vo.UsersVO;
import hjs.service.UserService;
import hjs.service.UsersReportService;
import hjs.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


@Api(value = "用户操作接口",description = "用户操作的controller")
@RestController
@RequestMapping("/user")
public class UserController extends BasicController{
    @Autowired
    private UserService userService;
    @Autowired
    private UsersReportService usersReportService;

    @ApiOperation(value = "上传头像",notes = "上传头像接口")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "String",paramType = "query")
    @PostMapping("/uploadFace/{id}")
    public IMoocJSONResult uploadFace(@PathVariable String id, @RequestParam("file") MultipartFile[] files) throws IOException {
        if (StringUtils.isBlank(id)){
            return IMoocJSONResult.errorMsg("用户id不能为空！");
        }
        String fileNamespace = Const.FilePath.getValue();
        String uploadPathDb = "/"+id + "/face";
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try{
            if (files[0] != null || files.length > 0){
                String finalFilePath = fileNamespace + uploadPathDb +"/"+ files[0].getOriginalFilename();
                uploadPathDb = uploadPathDb + "/"+files[0].getOriginalFilename();
                File file = new File(finalFilePath);
                if (file.getParentFile() != null || !file.getParentFile().isDirectory()){
                    file.getParentFile().mkdirs();
                }
                fileOutputStream = new FileOutputStream(finalFilePath);
                inputStream = files[0].getInputStream();
                IOUtils.copy(inputStream,fileOutputStream);
            }else {
                return IMoocJSONResult.errorMsg("上传出错！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        }finally {
            if (fileOutputStream != null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        Users users = new Users();
        users.setFaceImage(uploadPathDb);
        users.setId(id);
        boolean res = userService.updateUser(users);
        return res == true?IMoocJSONResult.ok(uploadPathDb):IMoocJSONResult.errorMsg("上传出错");
    }

    @ApiOperation(value = "用户查询",notes = "用户查询接口")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "String",paramType = "query")
    @PostMapping("/get")
    public IMoocJSONResult get(String userId,String fanId){
        if (StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id为空！");
        }
        Users user = userService.get(userId);
        if (user == null){
            return IMoocJSONResult.errorMsg("用户不存在！");
        }
        UsersVO usersVO = new UsersVO();
        user.setPassword("");
        BeanUtils.copyProperties(user,usersVO);
        usersVO.setFollow(true);
        usersVO.setFollow( userService.queryIfFollow(userId, fanId));
        return IMoocJSONResult.ok(usersVO);
    }

    @PostMapping("/queryUserInfo")
    public IMoocJSONResult queryUserInfo(String loginId,String videoId,String publishId){
        Users user = userService.get(publishId);
        UsersVO usersVO = new UsersVO();
        user.setPassword("");
        BeanUtils.copyProperties(user,usersVO);
        boolean like = userService.searchLike(loginId, videoId);
        PublisherVideo publisherVideo = new PublisherVideo();
        publisherVideo.setPublisher(usersVO);
        publisherVideo.setUserLikeVideo(like);
        System.out.println(publisherVideo);
        return IMoocJSONResult.ok(publisherVideo);
    }

    @PostMapping("/addFans")
    public IMoocJSONResult addFans(String userId,String fanId){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("用户名或者粉丝名为空!");
        }
        boolean sign = userService.addFans(userId,fanId);
        return sign == true?IMoocJSONResult.ok():IMoocJSONResult.errorMsg("删除粉丝失败!");
    }

    @PostMapping("/removeFans")
    public IMoocJSONResult removeFans(String userId,String fanId){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("用户名或者粉丝名为空!");
        }
        boolean sign = userService.removeFans(userId,fanId);
        return sign == true?IMoocJSONResult.ok():IMoocJSONResult.errorMsg("删除粉丝失败!");
    }

    @PostMapping("/reportUser")
    public IMoocJSONResult reportUser(@RequestBody UsersReport usersReport){
        System.out.println(usersReport);
        boolean sign = usersReportService.addReport(usersReport);
        return sign == true ? IMoocJSONResult.okMsg("举报成功！"):IMoocJSONResult.errorMsg("举报失败！");
    }
}
