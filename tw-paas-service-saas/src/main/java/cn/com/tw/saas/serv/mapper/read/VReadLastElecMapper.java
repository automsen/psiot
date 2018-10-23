package cn.com.tw.saas.serv.mapper.read;

import cn.com.tw.saas.serv.entity.db.read.VReadLastElec;
import feign.Param;

public interface VReadLastElecMapper{
    
    VReadLastElec selectElecAll(VReadLastElec vReadLastElec);

	VReadLastElec selectElecAll1(@Param("meterAddr") String meterAddr,@Param("readTime") String readTime);
    
}