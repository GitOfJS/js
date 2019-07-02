package hjs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import hjs.mapper.*;
import hjs.pojo.Comments;
import hjs.pojo.SearchRecords;
import hjs.pojo.UsersLikeVideos;
import hjs.pojo.Videos;
import hjs.pojo.vo.CommentsVO;
import hjs.pojo.vo.VideosVO;
import hjs.service.VideoService;
import hjs.utils.PagedResult;
import hjs.utils.TimeAgoUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private VideosMapperCustom videosMapperCustom;
    @Autowired
    private SearchRecordsMapper recordsMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private CommentsMapperCustom commentsMapperCustom;
    @Autowired
    private CommentsMapper commentsMapper;
    @Override
    public String upload(Videos videos) {
        videos.setId(sid.nextShort());
        videosMapper.insertSelective(videos);
        return videos.getId();
    }

    @Override
    public void updateVideo(String id, String uploadDb) {
        Videos videos = new Videos();
        videos.setId(id);
        videos.setCoverPath(uploadDb);
        videosMapper.updateByPrimaryKeySelective(videos);
    }


    @Override
    public PagedResult queryAllVideos(Videos videos, Integer isRecord, Integer pageNo, Integer pageSize) {
        String desc = null;
        if (videos != null){
            desc = videos.getVideoDesc();
        }
        System.out.println(desc);
        if (isRecord != null && isRecord != 0){
            SearchRecords searchRecords = new SearchRecords();
            searchRecords.setContent(desc);
            searchRecords.setId(sid.nextShort());
            recordsMapper.insert(searchRecords);
        }
        PageHelper.startPage(pageNo,pageSize);
        List<VideosVO> vos = videosMapperCustom.queryAll(desc);
        PageInfo<VideosVO> pageInfo = new PageInfo<>(vos);
        PagedResult result = new PagedResult();
        result.setPage(pageInfo.getPageNum());
        result.setRecords(pageInfo.getTotal());
        result.setRows(pageInfo.getList());
        result.setTotal(pageInfo.getPages());
        return result;
    }

    @Override
    public List<String> getHotWords() {
        return recordsMapper.getHotWords();
    }

    @Override
    public void like(String userId,String videoId,String createVideoId) {
        String id = sid.nextShort();
        UsersLikeVideos likeVideos = new UsersLikeVideos();
        likeVideos.setId(id);
        likeVideos.setUserId(userId);
        likeVideos.setVideoId(videoId);
        videosMapperCustom.like(videoId);
        usersLikeVideosMapper.insert(likeVideos);
        usersMapper.addLikeCounts(createVideoId);
    }

    @Override
    public void unLike(String userId,String videoId,String createVideoId) {
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        videosMapperCustom.unLike(videoId);
        usersLikeVideosMapper.deleteByExample(example);
        usersMapper.reduceLikeCounts(createVideoId);
    }

    @Override
    public PagedResult queryAllVideosByUserId(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Videos> list = videosMapperCustom.selectVideosByUserId(userId);
        PageInfo<Videos> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(pageInfo.getPageNum());
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(pageInfo.getList());
        pagedResult.setTotal(pageInfo.getPages());
        return pagedResult;
    }

    public PagedResult queryFansVideosByUserId(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Videos> list = videosMapperCustom.selectFansByUserId(userId);
        PageInfo<Videos> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(pageInfo.getPageNum());
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(pageInfo.getList());
        pagedResult.setTotal(pageInfo.getPages());
        return pagedResult;
    }

    public PagedResult queryFollowVideosByUserId(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Videos> list = videosMapperCustom.selectFollowByUserId(userId);
        PageInfo<Videos> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(pageInfo.getPageNum());
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(pageInfo.getList());
        pagedResult.setTotal(pageInfo.getPages());
        return pagedResult;
    }

    @Override
    public void saveComment(Comments comments) {
        comments.setId(sid.nextShort());
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);
    }

    @Override
    public PagedResult listComments(String videoId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<CommentsVO> comments = commentsMapperCustom.ListComments(videoId);
        for (CommentsVO vo :comments){
            vo.setTimeAgoStr(TimeAgoUtils.format(vo.getCreateTime()));
        }
        PageInfo<CommentsVO> pageInfo = new PageInfo<>(comments);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(pageInfo.getPageNum());
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(pageInfo.getList());
        pagedResult.setTotal(pageInfo.getPages());
        return pagedResult;
    }
}
