package hjs.service.Impl;

import hjs.mapper.VideosMapper;
import hjs.pojo.Videos;
import hjs.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideosMapper videosMapper;
    @Override
    public void forbidVideo(String videoId) {
        Videos videos = new Videos();
        videos.setId(videoId);
        videos.setStatus(2);
        videosMapper.updateByPrimaryKeySelective(videos);
    }
}
