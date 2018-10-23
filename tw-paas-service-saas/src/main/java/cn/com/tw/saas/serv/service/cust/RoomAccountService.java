package cn.com.tw.saas.serv.service.cust;

import java.util.List;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.view.serve.RoomExtend;

public interface RoomAccountService {

	List<RoomExtend> selectForExtendList(CustRoomParam param);

	List<RoomExtend> selectForAllExtendList(CustRoomParam param);

}
