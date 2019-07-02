package hjs.service.Impl;

import hjs.mapper.UsersReportMapper;
import hjs.pojo.vo.Reports;
import hjs.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private UsersReportMapper usersReportMapper;
    @Override
    public List<Reports> listReport() {
        return usersReportMapper.selectList();
    }
}
