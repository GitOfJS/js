package hjs.mapper;

import hjs.pojo.vo.CommentsVO;
import hjs.utils.MyMapper;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<CommentsVO> {

    List<CommentsVO> ListComments(String videoId);
}