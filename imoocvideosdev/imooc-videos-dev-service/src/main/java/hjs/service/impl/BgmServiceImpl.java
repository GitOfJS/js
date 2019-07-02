package hjs.service.impl;

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
    public List<Bgm> list() {
        return bgmMapper.selectAll();
    }

    @Override
    public Bgm getById(String bgmId) {
        return bgmMapper.selectByPrimaryKey(bgmId);
    }
}
