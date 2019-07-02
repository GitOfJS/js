package hjs.controller;

import hjs.bean.AdminUser;
import hjs.pojo.Users;
import hjs.utils.IMoocJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserLoginController {
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public IMoocJSONResult login(Users users, HttpSession session){
        String userName = users.getUsername();
        String password = users.getPassword();
        if (StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
            return IMoocJSONResult.errorMsg("用户名或者密码不能为空！");
        }
        else if (userName.equals("123456") && password.equals("1234")){
            String token = UUID.randomUUID().toString();
            AdminUser adminUser = new AdminUser();
            adminUser.setUsertoken(token);
            adminUser.setUsername(userName);
            session.setAttribute("sessionUser",adminUser);
            return IMoocJSONResult.ok();
        }
        return IMoocJSONResult.errorMsg("登录失败!");
    }
}
