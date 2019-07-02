package hjs.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import hjs.pojo.Bgm;
import hjs.pojo.UsersReport;
import hjs.pojo.vo.Reports;
import hjs.service.BgmService;
import hjs.service.ReportService;
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
public class ReportController {
    @Autowired
    private Sid sid;
    @Autowired
    private ReportService reportService;
    @Autowired
    private VideoService videoService;
    @GetMapping("/showReportList")
    public String showAddBgm() {
        return "/video/reportList";
    }

    @ResponseBody
    @PostMapping("/reportList")
    public PagedResult reportList(Integer rows, Integer page){
        PageHelper.startPage(page, rows);
        List<Reports> reports = reportService.listReport();
        PageInfo<Reports> pageInfo = new PageInfo<>(reports);
        PagedResult result = new PagedResult();
        result.setTotal(pageInfo.getPages());
        result.setRecords(pageInfo.getTotal());
        result.setPage(pageInfo.getPageNum());
        result.setRows(pageInfo.getList());
        return result;
    }

    @ResponseBody
    @PostMapping("/forbidVideo")
    public IMoocJSONResult forbidVideo(String videoId){
        if (StringUtils.isBlank(videoId)){
            return IMoocJSONResult.errorMsg("id为空！");
        }
        videoService.forbidVideo(videoId);
        return IMoocJSONResult.ok();
    }

}
