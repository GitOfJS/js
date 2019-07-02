package hjs.service.impl;

import hjs.mapper.UsersReportMapper;
import hjs.pojo.UsersReport;
import hjs.service.UsersReportService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UsersReportServiceImpl implements UsersReportService {
    @Autowired
    private UsersReportMapper usersReportMapper;
    @Autowired
    private Sid sid;
    @Override
    public boolean addReport(UsersReport usersReport) {
        try{
            String id = sid.nextShort();
            usersReport.setId(id);
            usersReport.setCreateDate(new Date());
            usersReportMapper.insert(usersReport);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
