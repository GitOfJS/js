package hjs.controller;

import hjs.pojo.Users;
import hjs.pojo.vo.UsersVO;
import hjs.service.UserService;
import hjs.utils.IMoocJSONResult;
import hjs.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@Api(value = "用户注册登录接口",description = "注册和登录的controller")
@RestController
public class RegistLoginController extends BasicController{
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册",notes = "用户注册接口")
    @PostMapping("/regist")
    public IMoocJSONResult regist(@RequestBody Users user) throws Exception {
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return IMoocJSONResult.errorMsg("用户名或者密码不能为空！");
        }
        if (userService.checkNameIsExists(user.getUsername())){
            return IMoocJSONResult.errorMsg("用户名已存在，请换一个再试！");
        }else {
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setNickname(user.getUsername());
            user.setReceiveLikeCounts(0);
            user.setFansCounts(0);
            userService.saveUser(user);
        }
        user.setPassword("");
        return IMoocJSONResult.ok(getVo(user));
    }

    @ApiOperation(value = "用户登陆",notes = "用户登陆接口")
    @PostMapping("/login")
    public IMoocJSONResult login(@RequestBody Users user) throws Exception {
        if (StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())){
            return IMoocJSONResult.errorMsg("用户名或者密码不能为空！");
        }
        user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
        Users res = userService.QueryUser(user);
        if (res != null){
            res.setPassword("");
        }
        return res != null?IMoocJSONResult.ok(getVo(res)):IMoocJSONResult.errorMsg("用户不存在");
    }

    @ApiOperation(value = "用户登陆",notes = "用户登陆接口")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "String",paramType = "query")
    @PostMapping("/logout/{id}")
    public IMoocJSONResult logout(@PathVariable String id){
        redis.del(USER_REDIS_SESSION+":"+id);
        return IMoocJSONResult.ok();
    }


    private UsersVO getVo(Users user){
        String uniqueToken = UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION+":"+user.getId(),uniqueToken,1000*60*30);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user,usersVO);
        usersVO.setUserToken(uniqueToken);
        return usersVO;
    }
}
