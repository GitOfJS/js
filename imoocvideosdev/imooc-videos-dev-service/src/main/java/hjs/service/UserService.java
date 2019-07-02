package hjs.service;

import hjs.pojo.Users;

public interface UserService {
    boolean checkNameIsExists(String username);

    void saveUser(Users user);

    Users QueryUser(Users user);

    boolean updateUser(Users users);

    Users get(String id);

    boolean searchLike(String userId, String videoId);

    boolean queryIfFollow(String userId, String fanId);


    boolean addFans(String userId, String fanId);

    boolean removeFans(String userId, String fanId);
}
