package hjs.service;

import hjs.pojo.Bgm;

import java.util.List;

public interface BgmService {

    void uploadBgm(Bgm bgm);

    List<Bgm> listAllBgm();

    void deleteBgm(String bgmId);
}
