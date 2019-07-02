package hjs.service.impl;

import hjs.mapper.UsersFansMapper;
import hjs.mapper.UsersLikeVideosMapper;
import hjs.mapper.UsersMapper;
import hjs.pojo.Users;
import hjs.pojo.UsersFans;
import hjs.pojo.UsersLikeVideos;
import hjs.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersFansMapper usersFansMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean checkNameIsExists(String username) {
        Users user = new Users();
        user.setUsername(username);
        Users users = usersMapper.selectOne(user);
        return users == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users user) {
        user.setId(sid.nextShort());
        usersMapper.insert(user);
    }


    @Override
    public Users QueryUser(Users user) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",user.getUsername());
        criteria.andEqualTo("password",user.getPassword());
        return usersMapper.selectOneByExample(example);
    }

    @Override
    public boolean updateUser(Users users) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",users.getId());
        int i = usersMapper.updateByExampleSelective(users, example);
        return i==0?false:true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users get(String id) {
        return usersMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean searchLike(String userId, String videoId) {
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        List<UsersLikeVideos> likeVideos = usersLikeVideosMapper.selectByExample(example);
        if (likeVideos != null && likeVideos.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean queryIfFollow(String userId, String fanId) {
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fanId);
        List<UsersFans> fans = usersFansMapper.selectByExample(example);
        if (fans != null && fans.size()>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean addFans(String userId, String fanId) {
        UsersFans usersFans = new UsersFans();
        usersFans.setUserId(userId);
        usersFans.setFanId(fanId);
        usersFans.setId(sid.nextShort());
        try {
            usersFansMapper.insert(usersFans);
            usersMapper.addFansCount(userId);
            usersMapper.addFollowCount(fanId);
            System.out.println(usersFans);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean removeFans(String userId, String fanId) {
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fanId);
        try {
            usersFansMapper.deleteByExample(example);
            usersMapper.reduceFansCount(userId);
            usersMapper.reduceFollowCount(fanId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
