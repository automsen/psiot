package cn.com.tw.paas.monit.service.sys;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.paas.monit.entity.db.sys.Area;

public interface AreaService extends IBaseSerivce<Area> {

	List<Area> selectAreaByParentId(String parentId);

}
