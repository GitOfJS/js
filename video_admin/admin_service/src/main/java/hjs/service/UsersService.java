package hjs.service;

import hjs.pojo.Users;

import java.util.List;

public interface UsersService {

    List<Users> list(String username,String nickname);
}
