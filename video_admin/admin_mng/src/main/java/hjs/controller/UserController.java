package hjs.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import hjs.pojo.Bgm;
import hjs.pojo.Users;
import hjs.service.UsersService;
import hjs.utils.PagedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UsersService usersService;

    @GetMapping("/center")
    public String center(){
        return "center";
    }

    @GetMapping("/showList")
    public String showAddBgm() {
        return "/users/usersList";
    }

    @ResponseBody
    @PostMapping("/list")
    public PagedResult list(Integer rows, Integer page,String username,String nickname){
        System.out.println("username:"+username+",nickname:"+nickname);
        if (username == "" || username == null){
            username = null;
        }
        if (nickname == "" || nickname == null){
            nickname = null;
        }
        PageHelper.startPage(page, rows);
        List<Users> users = usersService.list(username,nickname);
        System.out.println(users);
        PageInfo<Users> pageInfo = new PageInfo<>(users);
        PagedResult result = new PagedResult();
        result.setTotal(pageInfo.getPages());
        result.setRecords(pageInfo.getTotal());
        result.setPage(pageInfo.getPageNum());
        result.setRows(pageInfo.getList());
        return result;
    }
}
