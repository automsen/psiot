package cn.com.tw.saas.serv.service.terminal.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.http.HttpGetReq;
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
import cn.com.tw.saas.serv.entity.db.read.ReadLast;
import cn.com.tw.saas.serv.entity.excel.MeterExcel;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.SaasMeter;
import cn.com.tw.saas.serv.mapper.read.ReadLastMapper;
import cn.com.tw.saas.serv.mapper.room.SaasRegionMapper;
import cn.com.tw.saas.serv.mapper.room.SaasRoomMeterMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.mapper.terminal.SaasMeterMapper;
import cn.com.tw.saas.serv.service.terminal.FeignMeterService;
import cn.com.tw.saas.serv.service.terminal.SaasMeterService;

@Service
public class SaasMeterServiceImpl implements SaasMeterService {
	
	private String URL = Env.getVal("paas.url");

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String table = "h_ss_term_last";

	private static String APPKEY = Env.getVal("paas.appKey");

	private static String SECRET = Env.getVal("paas.secret");

	@Autowired
	private SaasMeterMapper saasMeterMapper;

	@Autowired
	private FeignMeterService feignMeterService;

	@Autowired
	private ReadLastMapper readLastMapper;

	@Autowired
	private TerminalLastDataDao terminalLastDataDao;
	
	@Autowired
	private MeterMapper meterMapper;

	@Override
	public int deleteById(String arg0) {
		/**
		 * 判断仪表是否已经与房间绑定
		 */
		SaasMeter saasMeters = saasMeterMapper.selectByPrimaryKey(arg0);
		if (!StringUtils.isEmpty(saasMeters.getRoomId())) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该表已经与房间绑定");
		}

		return saasMeterMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(SaasMeter arg0) {
		// TODO Auto-generated method stub
		return saasMeterMapper.insert(arg0);
	}

	@Override
	public int insertSelect(SaasMeter arg0) {
		return saasMeterMapper.insert(arg0);
	}

	@Override
	public SaasMeter selectById(String arg0) {
		// TODO Auto-generated method stub
		return saasMeterMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<SaasMeter> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return saasMeterMapper.selectByPage(arg0);
	}

	@Override
	public int update(SaasMeter arg0) {
		// TODO Auto-generated method stub
		return saasMeterMapper.updateByPrimaryKey(arg0);
	}

	@Override
	public int updateSelect(SaasMeter arg0) {
		// TODO Auto-generated method stub
		return saasMeterMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public List<SaasMeter> selectByRoomId(String roomId) {
		// TODO Auto-generated method stub
		return saasMeterMapper.selectByRoomId(roomId);
	}

	@Override
	public String importInsert(MeterExcel meterExcel) {
		SaasMeter meter = new SaasMeter();
		meter.setMeterAddr(meterExcel.getMeterAddr());
		meter.setMeterInstallAddr(meterExcel.getMeterInstallAddr());
		meter.setElecPt(meterExcel.getElecPt());
		meter.setElecCt(meterExcel.getElecCt());
		// 仪表未录入
		if (saasMeterMapper.selectByMeterAddr(meter.getMeterAddr()) == null) {
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
	        HttpGetReq req = new HttpGetReq(URL+"/"+uri+"?"+MapUtil.mapJoin(requestMap, false, false));
	        String result1;
			try {
				result1 = req.excuteReturnStr();
				Response response = JsonUtils.jsonToPojo(result1, Response.class);
				// paas查到结果
				if (null != response.getData()) {
					Map<String, Object> map = (Map<String, Object>) response.getData();
					meter = SaasMeter.splicing(meter, map);
					meter.setOrgId("1");
					int result = saasMeterMapper.insert(meter);
					// 插入成功
					if (result == 1) {
						return "ok";
					}
					// 插入失败
					else {
						return "未知异常";
					}
				} else {
					return "仪表编号无匹配";
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "系统异常";
			}
		} else {
			return "仪表编号重复";
		}

	}

	@Override
	public SaasMeter selectByMeterAddr(String meterAddr) {
		// TODO Auto-generated method stub
		return saasMeterMapper.selectByMeterAddr(meterAddr);
	}

	@Override
	public List<SaasMeter> selectSaasMeter(SaasMeter saasMeter) {
		return saasMeterMapper.selectSaasMeter(saasMeter);
	}

	@Override
	public SaasMeter selectByRoomIdAndMeterAddr(SaasMeter saasMeter) {
		/**
		 * 查询房间下是否拥有仪表
		 */
		SaasMeter saasMeter1 = new SaasMeter();
		saasMeter1.setRoomId(saasMeter.getRoomId());
		List<SaasMeter> meterList = saasMeterMapper.selectByEntity(saasMeter1);
		if (meterList != null && meterList.size() > 0) {
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该表已经与其他房间绑定");
		}
		/**
		 * 校验仪表是否已经绑定房间 检验仪表是否存在 型号验证
		 */
		if (StringUtils.isEmpty(saasMeterMapper.selectByMeterAddr(saasMeter.getMeterAddr()))) {
			throw new BusinessException(MonitResultCode.DATA_EXISTS_NULL, "表号不存在");
		}

		/**
		 * 查最近读数
		 */
		SaasMeter saasMeter2 = saasMeterMapper.selectByMeterAddr(saasMeter.getMeterAddr());
		ReadLast readLast = new ReadLast();
		readLast.setMeterAddr(saasMeter2.getMeterAddr());
		if (EnergyCategEum.ELEC.getValue().equals(saasMeter2.getEnergyType())) {
			readLast.setItemCode("totalActiveE");
		} else if (EnergyCategEum.WATER.getValue().equals(saasMeter2.getEnergyType())) {
			readLast.setItemCode("waterFlow");
		}
		ReadLast last = readLastMapper.selectByAddrAndItem(readLast);
		if (!StringUtils.isEmpty(last)) {
			saasMeter2.setReadValue(last.getReadValue());
		}

		return saasMeter2;
	}

	@Override
	public List<SaasMeter> selectByLikeEntity(SaasMeter saasMeter) {

		return saasMeterMapper.selectByLikeEntity(saasMeter);
	}

	@Override
	public List<SaasMeter> selectCommunicationTest(String meterAddr, String page) {

		return saasMeterMapper.selectCommunicationTest(meterAddr, Integer.valueOf(page));
	}

	@Override
	public List<SaasMeter> selectLoadMonitorByPage(Page page) {
		return saasMeterMapper.selectLoadMonitorByPage(page);
	}

	@Override
	public List<Meter> selectHbByPage(Page page) {
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Meter> saasMeters = meterMapper.selectHbByPage(page);
		List<byte[]> rowList = new ArrayList<byte[]>();
		List<String> cfNames = new ArrayList<String>();
		for (Meter saasMeter : saasMeters) {
			if (saasMeter.getMeterAddr() != null) {
				String meterAddr = MD5Utils.digest(saasMeter.getMeterAddr());
				rowList.add(Bytes.toBytes(meterAddr));
				// cfNames.add("item:isOff");
				// cfNames.add("item:totalActiveE");
			}
		}

		try {
			Result[] result = terminalLastDataDao.getListByKeys(table, rowList, cfNames);
			// 获取查询参数
			for (Result res : result) {
				Map<String, Object> resultMap = new HashMap<String, Object>();

				if (res.size() == 0) {
					continue;
				}

				for (Cell cell : res.listCells()) {

					String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));

					if ("createTime".equals(coluName) || "readTime".equals(coluName)) {
						resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
					} else {
						resultMap.put(coluName, Bytes.toString(CellUtil.cloneValue(cell)));
					}
				}

				String meterAddr = (String) resultMap.get("termNo");
				String isOffStr = (String) resultMap.get("isOff");//通断
				String updateTimeStr = (String) resultMap.get("updateTime");//最后读取时间
				String totalActiveE = (String) resultMap.get("totalActiveE");//最后止码
				Long updateTime = Long.parseLong(updateTimeStr);
				Date date1 = new Date(updateTime);
				Date date = new Date();
				for (Meter saasMeter : saasMeters) {
					if (saasMeter.getMeterAddr().equals(meterAddr)) {
						if(isOffStr != null && isOffStr != ""){
							saasMeter.setReadValue(new BigDecimal(isOffStr));
						}
						if(totalActiveE != null && totalActiveE != ""){
							saasMeter.setTotalActiveE(new BigDecimal(totalActiveE));
						}
						if(updateTimeStr != null && updateTimeStr != ""){
							saasMeter.setLastReadTime(date1);
						}
						/**
						 * 判断仪表仪表是否 配置采集频率
						 */
						if(saasMeter.getGatherHz() != null){
							/**
							 * 大于 两倍的采集频率时间不正常
							 */
							if ((date.getTime() - Long.parseLong(updateTimeStr)) > 2*saasMeter.getGatherHz() * 60 * 1000) {
								saasMeter.setCommunicationType("0");//通讯状态   1.正常 0.不正常
							} else {
								saasMeter.setCommunicationType("1");
							}
						}else{
							saasMeter.setCommunicationType(null);
						}
					}
				}

			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}

		return saasMeters;
	}

}
