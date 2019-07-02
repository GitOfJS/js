package hjs.service;

import hjs.pojo.Comments;
import hjs.pojo.Videos;
import hjs.utils.PagedResult;

import java.util.List;

public interface VideoService {

    String upload(Videos videos);

    void updateVideo(String id, String uploadDb);

    PagedResult queryAllVideos(Videos videos,Integer isRecord,Integer pageNo,Integer pageSize);

    List<String> getHotWords();

    void like(String userId,String videoId,String createVideoId);

    void unLike(String userId,String videoId,String createVideoId);

    PagedResult queryAllVideosByUserId(String userId, Integer pageNum, Integer pageSize);

    PagedResult queryFansVideosByUserId(String userId, Integer pageNum, Integer pageSize);

    PagedResult queryFollowVideosByUserId(String userId, Integer pageNum, Integer pageSize);

    void saveComment(Comments comments);

    PagedResult listComments(String videoId, Integer pageNum, Integer pageSize);
}
