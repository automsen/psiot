package cn.com.tw.saas.serv.mapper.read;

import java.util.List;
import java.util.Map;

import cn.com.tw.saas.serv.entity.db.read.ReadRecord;

public interface ReadRecordMapper {
//    int deleteByPrimaryKey(String id);

    int insert(ReadRecord record);

    List<ReadRecord> selectByAddr(ReadRecord record);
    
    List<ReadRecord> selectByAddrAndTd(ReadRecord record);
    
    ReadRecord selectByPrimaryKey(ReadRecord record);

	List<ReadRecord> selectByAddrAndTd1(ReadRecord param);
	
	List<ReadRecord> selectDemandByMeterAddr(Map<String, Object> paramMap);

//    int updateByPrimaryKeySelective(ReadRecord record);
}