package hjs.service;

import hjs.pojo.Bgm;

import java.util.List;

public interface BgmService {
    List<Bgm> list();

    Bgm getById(String bgmId);
}
