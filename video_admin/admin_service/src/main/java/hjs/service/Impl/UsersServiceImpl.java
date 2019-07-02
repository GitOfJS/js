package hjs.service.Impl;

import hjs.mapper.UsersMapper;
import hjs.pojo.Users;
import hjs.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersMapper usersMapper;
    @Override
    public List<Users> list(String username,String nickname) {
        List<Users> list = usersMapper.selectAll(username,nickname);
        System.out.println(list);
        return list;
    }
}
