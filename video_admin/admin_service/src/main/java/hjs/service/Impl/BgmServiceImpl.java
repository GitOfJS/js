package hjs.service.Impl;

import hjs.mapper.BgmMapper;
import hjs.pojo.Bgm;
import hjs.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {
    @Autowired
    private BgmMapper bgmMapper;
    @Override
    public void uploadBgm(Bgm bgm) {
        bgmMapper.insert(bgm);
    }

    public List<Bgm> listAllBgm(){
        return bgmMapper.selectAll();
    }

    @Override
    public void deleteBgm(String bgmId) {
        bgmMapper.deleteByPrimaryKey(bgmId);
    }
}
