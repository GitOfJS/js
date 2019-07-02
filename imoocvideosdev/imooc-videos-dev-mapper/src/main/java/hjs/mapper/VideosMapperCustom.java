package hjs.mapper;

import hjs.pojo.Videos;
import hjs.pojo.vo.VideosVO;
import hjs.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<VideosVO> {

    List<VideosVO> queryAll(@Param("desc") String desc);

    void like(String videoId);

    void unLike(String videoId);

    List<Videos> selectVideosByUserId(String userId);

    List<Videos> selectFansByUserId(String userId);

    List<Videos> selectFollowByUserId(String userId);
}