package cn.com.tw.saas.serv.service.terminal.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.http.HttpGetReq;
import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.utils.tools.security.MD5Utils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.saas.serv.common.utils.MapUtil;
import cn.com.tw.saas.serv.common.utils.SignatureUtil;
import cn.com.tw.saas.serv.common.utils.cons.EnergyCategEum;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.dao.TerminalLastDataDao;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.read.ReadLast;
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;
import cn.com.tw.saas.serv.entity.db.rule.RuleAlarm;
import cn.com.tw.saas.serv.entity.excel.MeterExcel;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.MeterExtend;
import cn.com.tw.saas.serv.entity.terminal.MeterHis;
import cn.com.tw.saas.serv.mapper.read.ReadLastMapper;
import cn.com.tw.saas.serv.mapper.room.SaasRegionMapper;
import cn.com.tw.saas.serv.mapper.room.SaasRoomMeterMapper;
import cn.com.tw.saas.serv.mapper.rule.EnergyPriceMapper;
import cn.com.tw.saas.serv.mapper.rule.RuleAlarmMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterHisMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.terminal.MeterService;
import cn.com.tw.saas.serv.service.terminal.SaasMeterService;

@Service
public class MeterServiceImpl implements MeterService {
	
	private String URL = Env.getVal("paas.url");

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String table = "h_ss_term_last";

	private static String APPKEY = Env.getVal("paas.appKey");

	private static String SECRET = Env.getVal("paas.secret");

	@Autowired
	private MeterMapper meterMapper;
	@Autowired
	private EnergyPriceMapper priceMapper;	
	@Autowired
	private RuleAlarmMapper ruleAlarmMapper;
	@Autowired
	private ReadLastMapper readLastMapper;
	
	@Autowired
	private MeterHisMapper meterHisMapper;

	@Autowired
	private TerminalLastDataDao terminalLastDataDao;

	@Override
	public int deleteById(String arg0) {
		/**
		 * 判断仪表是否已经与房间绑定
		 */
		Meter meter = meterMapper.selectByPrimaryKey(arg0);
		if (!StringUtils.isEmpty(meter.getRoomId())) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该表已经与房间绑定");
		}
		
		/**
		 * 仪表不存在历史记录 则物理删除
		 */
		MeterHis meterHis = new MeterHis();
		meterHis.setMeterAddr(meter.getMeterAddr());
		List<Meter> meters = meterHisMapper.selectByEntity(meterHis);
		if(meters == null || meters.size() <= 0){
			meterMapper.deleteByPrimaryKey(arg0);
		}else{
			//改为废弃
			meter.setSubAccountStatus((byte)3);
			meterMapper.updateByPrimaryKeySelective(meter);
		}
		
		

		return /*meterMapper.deleteByPrimaryKey(arg0)*/0;
	}

	@Override
	public Meter selectById(String arg0) {
		// TODO Auto-generated method stub
		return meterMapper.selectByPrimaryKey(arg0);
	}
	
	@Override
	public List<Meter> selectByCustomer(Customer param) {
		// TODO Auto-generated method stub
		return meterMapper.selectByCustomer(param);
	}
	
	@Override
	public List<MeterExtend> selectByCustomerForWechat(Customer param) {
		// TODO Auto-generated method stub
		return meterMapper.selectByCustomerForWechat(param);
	}
	
	@Override
	public MeterExtend selectByAddrForWechat(Meter param) {
		// TODO Auto-generated method stub
		return meterMapper.selectByAddrForWechat(param);
	}

	@Override
	public List<Meter> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return meterMapper.selectByPage(arg0);
	}

	@Override
	public int updateSelect(Meter arg0) {
		// TODO Auto-generated method stub
		return meterMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public List<Meter> selectByRoomId(String roomId) {
		// TODO Auto-generated method stub
		return meterMapper.selectByRoomId(roomId);
	}
	

	@Override
	public String insert(Meter meter) {
		Meter temp = new Meter();
		temp.setMeterAddr(meter.getMeterAddr());
		List<Meter> meterList = meterMapper.selectByEntity(temp);
		// 仪表未录入
		if (null==meterList||meterList.size()==0) {
			// 访问paas获取数据
			Map<String, String> requestMap = new HashMap<String, String>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			requestMap.put("equipNumber", meter.getMeterAddr());
			requestMap.put("appKey", Env.getVal("paas.appKey"));
			requestMap.put("timestamp", sdf.format(new Date()));
			String businessNo = CommUtils.getUuid();
			requestMap.put("businessNo", businessNo);
			String sign = SignatureUtil.generateSign(requestMap, SECRET);
			requestMap.put("sign", sign);
			/*Response<?> data = feignMeterService.selectAbnorDate(meter.getMeterAddr(), APPKEY,
					requestMap.get("timestamp"), businessNo, sign);*/
			String uri = "/usepaas/terminalinfo/";
			StringEntity entity = new StringEntity(JsonUtils.objectToJson(requestMap),"utf-8");//解决中文乱码问题
	        entity.setContentEncoding("UTF-8");    
	        entity.setContentType("application/json");
	        HttpPostReq req = new HttpPostReq(URL+"/"+uri, null, entity);
	        String result1;
			try {
				result1 = req.excuteReturnStr();
				Response response = JsonUtils.jsonToPojo(result1, Response.class);
				// paas查到结果
				if (null != response.getData()) {
					Map<String, Object> map = (Map<String, Object>) response.getData();
					meter = Meter.splicing(meter, map);
					// 初始化账户未激活，余额为0，不计费
					meter.setSubAccountStatus((byte) 0);
					meter.setBalance(BigDecimal.ZERO);
					meter.setPriceModeCode("1304");
					// 设置默认计价规则
					EnergyPrice energyPrice = new EnergyPrice();
					energyPrice.setOrgId(meter.getOrgId());
					energyPrice.setIsDefault((byte) 1);
					energyPrice.setEnergyType(meter.getEnergyType());
					List<EnergyPrice> energyPrices = priceMapper.selectByBean(energyPrice);
					if (null != energyPrices && energyPrices.size()>0){
						meter.setPriceId(energyPrices.get(0).getPriceId());
					}
					// 设置默认预警规则
					RuleAlarm ruleAlarm = new RuleAlarm();
					ruleAlarm.setOrgId(meter.getOrgId());
					ruleAlarm.setIsDefault((byte) 1);
					ruleAlarm.setEquipType(meter.getEnergyType());
					List<RuleAlarm> ruleAlarms = ruleAlarmMapper.selectAlarmAll(ruleAlarm);
					if (null != ruleAlarms && ruleAlarms.size()>0){
						meter.setAlarmId(ruleAlarms.get(0).getAlarmId());
					}
					// 安装时初始读数默认值
					if (null==meter.getInstallRead()){
						meter.setInstallRead(BigDecimal.ZERO);
					}
					int result = meterMapper.insert(meter);
					// 插入成功
					if (result == 1) {
						return "ok";
					}
					// 插入失败
					else {
						return "未知异常";
					}
				} else {
					return "仪表编号在PAAS平台不存在";
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "系统异常";
			}
		} else {
			return "仪表已经存在于机构下";
		}

	}

	@Override
	public List<Meter> selectByEntity(Meter param) {
		return meterMapper.selectByEntity(param);
	}

	@Override
	public List<Meter> selectSaasMeter(Meter saasMeter) {
		return meterMapper.selectSaasMeter(saasMeter);
	}

	@Override
	public Meter selectByRoomIdAndMeterAddr(Meter param) {
//		/**
//		 * 查询房间下是否拥有仪表
//		 */
//		Meter saasMeter1 = new Meter();
//		saasMeter1.setRoomId(saasMeter.getRoomId());
//		List<Meter> meterList = meterMapper.selectByEntity(saasMeter1);
//		if (meterList != null && meterList.size() > 0) {
//			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该房间已存在仪表");
//		}
		/**
		 * 查询房间下是否拥有仪表
		 */
		Meter meterTemp = new Meter();
		meterTemp.setRoomId(param.getRoomId());
		List<Meter> meterList = meterMapper.selectByEntity(meterTemp);
		if (meterList != null && meterList.size() > 0) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该表已经与其他房间绑定");
		}

		/**
		 * 查询仪表
		 */
		Meter meter = meterMapper.selectByMeterAddr(param.getMeterAddr());
		/**
		 * 检验仪表是否存在
		 */
		if (meter==null) {
			throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL, "表号不存在");
		}
		ReadLast readLast = new ReadLast();
		readLast.setMeterAddr(meter.getMeterAddr());
		if (EnergyCategEum.ELEC.getValue().equals(meter.getEnergyType())) {
			readLast.setItemCode("totalActiveE");
		} else if (EnergyCategEum.WATER.getValue().equals(meter.getEnergyType())) {
			readLast.setItemCode("waterFlow");
		}
		ReadLast last = readLastMapper.selectByAddrAndItem(readLast);
		if (!StringUtils.isEmpty(last)) {
			meter.setReadValue(last.getReadValue());
		}
		return meter;
	}

	@Override
	public Meter selectOldMeter(String newMeter, String elecMeter,String waterMeter) {
		/**
		 * 根据 跟换的新表判断跟换的是 电表 还是水表
		 */
		Meter meter = new Meter();
		meter.setMeterAddr(newMeter);
		List<Meter> meters = meterMapper.selectByEntity(meter);
		/**
		 * 判断旧表类型
		 */
		if("110000".equals(meters.get(0).getEnergyType())){
			meter = meterMapper.selectByMeterAddr(elecMeter);
		}
		if("120000".equals(meters.get(0).getEnergyType())){
			meter = meterMapper.selectByMeterAddr(waterMeter);
		}
		
		return meter;
	}

	@Override
	public Meter selectByMeterAddr(String meterAddr) {
		return  meterMapper.selectByMeterAddr(meterAddr);
	}

	@Override
	public List<Meter> selectCommunicationTest(String meterAddr, String page) {
		return meterMapper.selectCommunicationTest(meterAddr, Integer.valueOf(page));
	}

	@Override
	public List<Meter> selectTerminalNumber(String userId) {
		
		return meterMapper.selectTerminalNumber(userId);
	}

	@Override
	public List<Meter> selectTerminalNetType(String userId) {
		return meterMapper.selectTerminalNetType(userId);
	}

	@Override
	public List<Meter> selectPriceByRoomId(String roomId) {
		return meterMapper.selectPriceByRoomId(roomId);
	}

	@Override
	public Meter selectRoomMeterByMeterAddr(Meter saasMeter) {
		return meterMapper.selectRoomMeterByMeterAddr(saasMeter);
	}

	@Override
	public List<Meter> selectAllInfoByRoomId(String roomId) {
		return meterMapper.selectAllInfoByRoomId(roomId);
	}

	@Override
	public Meter selectInfoByMeterAddr(String meterAddr) {
		
		return meterMapper.selectInfoByMeterAddr(meterAddr);
	}


//	@Override
//	public List<Meter> selectByLikeEntity(Meter saasMeter) {
//
//		return meterMapper.selectByLikeEntity(saasMeter);
//	}
//
//	@Override
//	public List<Meter> selectCommunicationTest(String meterAddr, String page) {
//
//		return meterMapper.selectCommunicationTest(meterAddr, Integer.valueOf(page));
//	}
//
//	@Override
//	public List<Meter> selectLoadMonitorByPage(Page page) {
//		return meterMapper.selectLoadMonitorByPage(page);
//	}
//
//	@Override
//	public List<Meter> selectHbByPage(Page page) {
//		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		List<Meter> saasMeters = meterMapper.selectHbByPage(page);
//		List<byte[]> rowList = new ArrayList<byte[]>();
//		List<String> cfNames = new ArrayList<String>();
//		for (Meter saasMeter : saasMeters) {
//			if (saasMeter.getMeterAddr() != null) {
//				String meterAddr = MD5Utils.digest(saasMeter.getMeterAddr());
//				rowList.add(Bytes.toBytes(meterAddr));
//				// cfNames.add("item:isOff");
//				// cfNames.add("item:totalActiveE");
//			}
//		}
//
//		try {
//			Result[] result = terminalLastDataDao.getListByKeys(table, rowList, cfNames);
//			// 获取查询参数
//			for (Result res : result) {
//				Map<String, Object> resultMap = new HashMap<String, Object>();
//
//				if (res.size() == 0) {
//					continue;
//				}
//
//				for (Cell cell : res.listCells()) {
//
//					String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
//
//					if ("createTime".equals(coluName) || "readTime".equals(coluName)) {
//						resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
//					} else {
//						resultMap.put(coluName, Bytes.toString(CellUtil.cloneValue(cell)));
//					}
//				}
//
//				String meterAddr = (String) resultMap.get("termNo");
//				String isOffStr = (String) resultMap.get("isOff");
//				String updateTime = (String) resultMap.get("updateTime");
//				Date date = new Date();
//				for (Meter saasMeter : saasMeters) {
//					if (saasMeter.getMeterAddr().equals(meterAddr)) {
//						saasMeter.setReadValue(new BigDecimal(isOffStr));
//						if ((date.getTime() - Long.parseLong(updateTime)) > 60 * 60 * 1000) {
//							saasMeter.setCommunicationType("0");
//						} else {
//							saasMeter.setCommunicationType("1");
//						}
//					}
//				}
//
//			}
//		} catch (IOException e) {
//			logger.error(e.getMessage());
//			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
//		}
//
//		return saasMeters;
//	}

}
