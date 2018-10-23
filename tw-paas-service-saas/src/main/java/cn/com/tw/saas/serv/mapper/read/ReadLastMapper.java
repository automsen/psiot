package cn.com.tw.saas.serv.mapper.read;

import cn.com.tw.saas.serv.entity.db.read.ReadLast;

public interface ReadLastMapper {

	int replace(ReadLast record);

	int updateByAddrAndItem(ReadLast record);

	ReadLast selectByAddrAndItem(ReadLast record);

	ReadLast selectLastOne(String meterAddr);

	ReadLast selectReading(String meterAddr);

	ReadLast selectReadLast(ReadLast readLast);
	
}