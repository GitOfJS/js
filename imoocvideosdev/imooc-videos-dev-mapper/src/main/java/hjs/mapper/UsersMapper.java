package hjs.mapper;

import hjs.pojo.Users;
import hjs.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {
    void addLikeCounts(String userId);

    void reduceLikeCounts(String userId);

    void addFansCount(String userId);

    void reduceFansCount(String userId);

    void addFollowCount(String userId);

    void reduceFollowCount(String userId);
}