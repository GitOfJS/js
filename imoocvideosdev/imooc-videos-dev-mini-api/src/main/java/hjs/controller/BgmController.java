package hjs.controller;

import hjs.service.BgmService;
import hjs.utils.IMoocJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bgm")
public class BgmController {
    @Autowired
    private BgmService bgmService;

    @GetMapping("/list")
    public IMoocJSONResult list(){
        return IMoocJSONResult.ok(bgmService.list());
    }
}
