package hjs.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import hjs.mapper.BgmMapper;
import hjs.pojo.Bgm;
import hjs.service.BgmService;
import hjs.service.VideoService;
import hjs.utils.IMoocJSONResult;
import hjs.utils.PagedResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private Sid sid;
    @Autowired
    private BgmService bgmService;
    @Autowired
    private VideoService videoService;

    @GetMapping("/showAddBgm")
    public String showAddBgm() {
        return "/video/addBgm";
    }

    @ResponseBody
    @PostMapping("/bgmUpload")
    public IMoocJSONResult bgmUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileNamespace = "D:" + File.separator + "project_video";
        String uploadPathDb = File.separator + "bgm" + File.separator;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            if (file != null) {
                String finalFilePath = fileNamespace + uploadPathDb + file.getOriginalFilename();
                uploadPathDb = uploadPathDb + file.getOriginalFilename();
                File files = new File(finalFilePath);
                if (files.getParentFile() != null || !files.getParentFile().isDirectory()) {
                    files.getParentFile().mkdirs();
                }
                fileOutputStream = new FileOutputStream(finalFilePath);
                inputStream = file.getInputStream();
                IOUtils.copy(inputStream, fileOutputStream);
            } else {
                return IMoocJSONResult.errorMsg("上传出错！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return IMoocJSONResult.ok(uploadPathDb);
    }

    @ResponseBody
    @PostMapping("/addBgm")
    public IMoocJSONResult addBgm(String path, String name, String author) throws IOException {
        if (StringUtils.isBlank(path) || StringUtils.isBlank(name) || StringUtils.isBlank(author)) {
            return IMoocJSONResult.errorMsg("路径或者名字或者作者名不能为空！");
        }
        Bgm bgm = new Bgm();
        bgm.setId(sid.nextShort());
        bgm.setPath(path);
        bgm.setName(name);
        bgm.setAuthor(author);
        bgmService.uploadBgm(bgm);
        return IMoocJSONResult.ok();
    }

    @GetMapping("/showBgmList")
    public String showBgmList() {
        return "/video/bgmList";
    }

    @ResponseBody
    @PostMapping("/queryBgmList")
    public PagedResult queryBgmList(Integer rows, Integer page){
        PageHelper.startPage(page, rows);
        List<Bgm> bgms = bgmService.listAllBgm();
        PageInfo<Bgm> pageInfo = new PageInfo<>(bgms);
        PagedResult result = new PagedResult();
        result.setTotal(pageInfo.getPages());
        result.setRecords(pageInfo.getTotal());
        result.setPage(pageInfo.getPageNum());
        result.setRows(pageInfo.getList());
        return result;
    }

    @ResponseBody
    @PostMapping("/delBgm")
    public IMoocJSONResult delBgm(String bgmId){
        if (StringUtils.isBlank(bgmId)){
            return IMoocJSONResult.errorMsg("id为空！");
        }
        bgmService.deleteBgm(bgmId);
        return IMoocJSONResult.ok();
    }

}
