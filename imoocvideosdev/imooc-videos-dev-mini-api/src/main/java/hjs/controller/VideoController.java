package hjs.controller;

import hjs.component.Const;
import hjs.pojo.Bgm;
import hjs.pojo.Comments;
import hjs.pojo.Videos;
import hjs.pojo.vo.VideosVO;
import hjs.service.BgmService;
import hjs.service.VideoService;
import hjs.utils.FetchVideoCover;
import hjs.utils.IMoocJSONResult;
import hjs.utils.MergeVideoMp3;
import hjs.utils.PagedResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private BgmService bgmService;

    @PostMapping("/upload")
    public IMoocJSONResult upload(VideosVO vo, @RequestParam("file")MultipartFile file, String bgmId) throws IOException {
        String namespace = Const.FilePath.getValue();
        String uploadDb = "/" + vo.getUserId() + "/video";
        String converDb = "/" + vo.getUserId() + "/video";
        if (StringUtils.isBlank(vo.getUserId())){
            return IMoocJSONResult.errorMsg("用户id不存在!");
        }
        if (file == null){
            return IMoocJSONResult.errorMsg("文件上传出错！");
        }
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        String finalPath = null;
        try{
            uploadDb = uploadDb + "/" + file.getOriginalFilename();
            converDb = converDb + "/" + UUID.randomUUID().toString()+".jpg";
            finalPath = namespace + uploadDb;
            File dir = new File(finalPath);
            if (dir.getParentFile() != null || !dir.getParentFile().isDirectory()){
                dir.getParentFile().mkdirs();
            }
            inputStream = file.getInputStream();
            fileOutputStream = new FileOutputStream(dir);
            IOUtils.copy(inputStream,fileOutputStream);
            FetchVideoCover videoCover = new FetchVideoCover("D:\\ffmpeg-4.1.3-win64-static\\bin\\ffmpeg.exe");
            if (StringUtils.isNoneBlank(bgmId)) {
                Bgm bgm = bgmService.getById(bgmId);
                String id = UUID.randomUUID().toString();
                String mp4InputPath = finalPath;
                String mp3InputPath = Const.FilePath.getValue()+bgm.getPath();
                uploadDb = "/" + vo.getUserId() + "/video" + "/" + id + ".mp4";
                String map4OutputPath = namespace + uploadDb;
                System.out.println("mp4InputPath:"+mp4InputPath+"\n"+"mp3InputPath:"+mp3InputPath+"\n"+"map4OutputPath:"+map4OutputPath);
                MergeVideoMp3 mergeVideoMp3 = new MergeVideoMp3("D:\\ffmpeg-4.1.3-win64-static\\bin\\ffmpeg.exe");
                mergeVideoMp3.convertor(mp4InputPath,mp3InputPath,1000,map4OutputPath);
                videoCover.getCover(map4OutputPath,namespace+converDb);
            }else {
                videoCover.getCover(finalPath,namespace+converDb);
            }
        }catch (Exception e){
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错！");
        }finally {
            if (fileOutputStream !=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        Videos videos = new Videos();
        BeanUtils.copyProperties(vo,videos);
        videos.setStatus(1);
        videos.setAudioId(bgmId);
        videos.setVideoPath(uploadDb);
        videos.setCreateTime(new Date());
        videos.setCoverPath(converDb);
        return IMoocJSONResult.ok(videoService.upload(videos));
    }

    @PostMapping("/list")
   public IMoocJSONResult list(@RequestBody Videos videos,Integer isRecord,@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "5") Integer pageSize){
        return IMoocJSONResult.ok(videoService.queryAllVideos(videos,isRecord,pageNum,pageSize));
   }

    @PostMapping("/listVideo")
    public IMoocJSONResult listVideo(String userId,Integer pageNum,@RequestParam(defaultValue = "5") Integer pageSize){
        return IMoocJSONResult.ok(videoService.queryAllVideosByUserId(userId,pageNum,pageSize));
    }

    @PostMapping("/listFansVideo")
    public IMoocJSONResult listFansVideo(String userId,Integer pageNum,@RequestParam(defaultValue = "5") Integer pageSize){
        return IMoocJSONResult.ok(videoService.queryFansVideosByUserId(userId,pageNum,pageSize));
    }

    @PostMapping("/listFollowVideo")
    public IMoocJSONResult listFollowVideo(String userId,Integer pageNum,@RequestParam(defaultValue = "5") Integer pageSize){
        return IMoocJSONResult.ok(videoService.queryFollowVideosByUserId(userId,pageNum,pageSize));
    }

   @PostMapping("/hot")
   public IMoocJSONResult hot(){
        return IMoocJSONResult.ok(videoService.getHotWords());
   }

    @PostMapping("/like")
    public IMoocJSONResult like(String userId,String videoId,String createVideoId){
        videoService.like(userId,videoId,createVideoId);
        return IMoocJSONResult.ok();
    }

    @PostMapping("/unLike")
    public IMoocJSONResult unLike(String userId,String videoId,String createVideoId){
        videoService.unLike(userId,videoId,createVideoId);
        return IMoocJSONResult.ok();
    }
    @PostMapping("/saveComment")
    public IMoocJSONResult saveComment(@RequestBody Comments comments,String toUserId,String fatherCommentId){
        comments.setToUserId(toUserId);
        comments.setFatherCommentId(fatherCommentId);
        videoService.saveComment(comments);
        return  IMoocJSONResult.ok();
    }

    @PostMapping("/listComments")
    public IMoocJSONResult listComments(String videoId,@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "5") Integer pageSize){
        PagedResult pagedResult = videoService.listComments(videoId,pageNum,pageSize);
        return  IMoocJSONResult.ok(pagedResult);
    }
}
