package hjs.mapper;

import hjs.pojo.SearchRecords;
import hjs.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    List<String> getHotWords();
}