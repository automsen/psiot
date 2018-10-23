package cn.com.tw.paas.monit.service.sys;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.sys.Dict;

public interface DictService extends IBaseSerivce<Dict>{

	List<Dict> selectDictAll(Dict dict);

	List<Dict> selectDictByPage(Page page);

}
