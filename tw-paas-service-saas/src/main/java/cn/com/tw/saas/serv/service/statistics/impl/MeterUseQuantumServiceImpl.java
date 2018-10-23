package cn.com.tw.saas.serv.service.statistics.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.dao.TerminalLastDataDao;
import cn.com.tw.saas.serv.entity.statistics.MeterUseDay;
import cn.com.tw.saas.serv.entity.statistics.MeterUseMonth;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.entity.statistics.MeterUseYear;
import cn.com.tw.saas.serv.mapper.org.OrgUserMapper;
import cn.com.tw.saas.serv.mapper.statistics.MeterUseDayMapper;
import cn.com.tw.saas.serv.mapper.statistics.MeterUseHourMapper;
import cn.com.tw.saas.serv.mapper.statistics.MeterUseMonthMapper;
import cn.com.tw.saas.serv.mapper.statistics.MeterUseQuantumMapper;
import cn.com.tw.saas.serv.mapper.statistics.MeterUseYearMapper;
import cn.com.tw.saas.serv.mapper.terminal.SaasMeterMapper;
import cn.com.tw.saas.serv.service.statistics.MeterUseQuantumService;

@Service
public class MeterUseQuantumServiceImpl implements MeterUseQuantumService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String table = "h_ss_term_last";

	private String cfName = "item";

	@Autowired
	private MeterUseQuantumMapper meterUseQuantumMapper;
	@Autowired
	private MeterUseDayMapper meterUseDayMapper;
	@Autowired
	private MeterUseMonthMapper meterUseMonthMapper;
	@Autowired
	private MeterUseYearMapper meterUseYearMapper;
	@Autowired
	private TerminalLastDataDao terminalLastDataDao;
	@Autowired
	private OrgUserMapper orgUserMapper;
	@Autowired
	private SaasMeterMapper saasMeterMapper;

	@Autowired
	private MeterUseHourMapper meterUseHourMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(MeterUseQuantum arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(MeterUseQuantum arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MeterUseQuantum selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MeterUseQuantum> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(MeterUseQuantum arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(MeterUseQuantum arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public void meterUseQuantumJob(String format) {
		/*
		 * List<byte[]> rowList = new ArrayList<byte[]>(); List<String> cfNames
		 * = new ArrayList<String>(); try { List<OrgUser> orgUsers =
		 * orgUserMapper.selectAll(); for (OrgUser orgUser : orgUsers) {
		 * SaasMeter saasMeter = new SaasMeter();
		 * saasMeter.setOrgId(orgUser.getOrgId()); List<SaasMeter> saasMeters =
		 * saasMeterMapper.selectByEntity(saasMeter); for (SaasMeter saasMeter2
		 * : saasMeters) { String meterAddr =
		 * MD5Utils.digest(saasMeter2.getMeterAddr());
		 * rowList.add(Bytes.toBytes(meterAddr)); } }
		 * 
		 * Result[] result = terminalLastDataDao.getListByKeys(table, rowList,
		 * cfNames); //获取查询参数 for (Result res : result) { Map<String, Object>
		 * resultMap = new HashMap<String, Object>();
		 * 
		 * if(res.size() == 0 ) { continue; }
		 * 
		 * for (Cell cell : res.listCells()) {
		 * 
		 * String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
		 * 
		 * if ("createTime".equals(coluName) || "readTime".equals(coluName)) {
		 * resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
		 * }else { resultMap.put(coluName,
		 * Bytes.toString(CellUtil.cloneValue(cell))); } }
		 * 
		 * String totalActiveE = (String) resultMap.get("totalActiveE"); String
		 * positiveActiveE = (String) resultMap.get("positiveActiveE"); String
		 * reverseActiveE = (String) resultMap.get("reverseActiveE"); String
		 * totalReactive1E = (String) resultMap.get("totalReactive1E"); String
		 * totalReactive2E = (String) resultMap.get("totalReactive2E"); String
		 * waterFlow = (String) resultMap.get("waterFlow"); String meterAddr =
		 * (String) resultMap.get("termNo"); String meterCateg = (String)
		 * resultMap.get("meterCateg");//能源类型 String meterType = (String)
		 * resultMap.get("meterType");//仪表类型
		 * 
		 * } } catch (IOException e) { logger.error(e.getMessage()); throw new
		 * BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR,
		 * "req hbase service error!!"); }
		 */

		List<MeterUseQuantum> meterUseQuantums = meterUseQuantumMapper
				.meterUseQuantumJob(format);

		meterUseQuantumMapper.deleteByFreezeTd(format);
		for (MeterUseQuantum meterUseQuantum : meterUseQuantums) {
			meterUseQuantum.setId(CommUtils.getUuid());
			meterUseQuantum.setCreateTime(new Date(System.currentTimeMillis()));
			meterUseQuantumMapper.insertSelective(meterUseQuantum);
		}

	}

	@Override
	@Transactional
	public void meterUseDayAndMonthAndYearJob(String format) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = sdf.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		/**
		 * 日统计
		 */
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDayDate = calendar.getTime();
		String lastDayTime = sdf.format(lastDayDate);

		List<MeterUseDay> meterUseDays = meterUseDayMapper.meterUseDayJob(
				format, lastDayTime);
		meterUseDayMapper.deleteByFreezeTd(format);
		for (MeterUseDay meterUseDay : meterUseDays) {
			meterUseDay.setCreateTime(new Date(System.currentTimeMillis()));
			meterUseDay.setId(CommUtils.getUuid());
			meterUseDayMapper.insertSelective(meterUseDay);
		}

		/**
		 * 月统计
		 */
		/*
		 * String monthFormat = format.substring(0, 6);
		 * calendar.add(Calendar.MONTH, -1); Date lastMonthDate =
		 * calendar.getTime(); String lastMonthTime =
		 * sdf.format(lastMonthDate).substring(0, 6);
		 * 
		 * List<MeterUseMonth> meterUseMonths =
		 * meterUseMonthMapper.meterUseMonthJob(monthFormat,lastMonthTime);
		 * meterUseMonthMapper.deleteByFreezeTd(monthFormat); for (MeterUseMonth
		 * meterUseMonth : meterUseMonths) {
		 * meterUseMonth.setId(CommUtils.getUuid());
		 * meterUseMonth.setCreateTime(new Date(System.currentTimeMillis()));
		 * meterUseMonthMapper.insertSelective(meterUseMonth); }
		 *//**
		 * 年统计
		 */
		/*
		 * String yearFormat = format.substring(0, 4); List<MeterUseYear>
		 * meterUseYears = meterUseYearMapper.meterUseYearJob(yearFormat);
		 * meterUseYearMapper.deleteByFreezeTd(yearFormat); for (MeterUseYear
		 * meterUseYear : meterUseYears) {
		 * meterUseYear.setId(CommUtils.getUuid());
		 * meterUseYear.setCreateTime(new Date(System.currentTimeMillis()));
		 * meterUseYearMapper.insertSelective(meterUseYear); }
		 */
	}

	@Override
	public List<MeterUseQuantum> selectMeterUseLevel(MeterUseQuantum useQuantum) {

		Page page = new Page();
		page.setParamObj(useQuantum);

		// 查询小时
		if ("hour".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseHours = meterUseHourMapper
					.selectMeterUseHour(page);
			return meterUseHours;
		}
		/*
		 * //房间用电查询天 if("day1".equals(useQuantum.getDateType())){
		 * List<MeterUseQuantum> dayElc=meterUseHourMapper.selectDayElc(page);
		 * return dayElc; }
		 */

		// 查询日
		if ("day".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseDays = meterUseDayMapper
					.selectMeterUseDay(page);
			return meterUseDays;
		}
		// 查询月
		if ("month".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseMonths = meterUseMonthMapper
					.selectMeterUseMonth(page);
			return meterUseMonths;
		}
		// 查询年
		if ("year".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseYears = meterUseYearMapper
					.selectMeterUseYear(page);
			return meterUseYears;
		}

		return null;
	}

	@Override
	public List<MeterUseQuantum> selectMeterUseHour(Page page) {
		List<MeterUseQuantum> meterUseHours = meterUseHourMapper
				.selectByPage(page);
		for (MeterUseQuantum meterUseQuantum : meterUseHours) {
			if (meterUseQuantum.getFreezeTd() != null) {
				Long a = Long.valueOf(meterUseQuantum.getFreezeTd())
						.longValue();
				Long b = a / 100;
				String s = String.valueOf(b);
				meterUseQuantum.setFreezeTd(s);
			} else {
				meterUseQuantum.setFreezeTd(meterUseQuantum.getFreezeTd());
			}

			if (meterUseQuantum.getUseValue() != null) {
				if (meterUseQuantum.getPrice1() != null) {
					BigDecimal bignum1 = (meterUseQuantum.getUseValue())
							.multiply(meterUseQuantum.getPrice1());
					BigDecimal bignum3 = bignum1.setScale(2,
							BigDecimal.ROUND_HALF_DOWN);
					meterUseQuantum.setMoneyNow(bignum3);
				} else if (meterUseQuantum.getPrice1() == null) {
					BigDecimal c1 = new BigDecimal(0);
					BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
					meterUseQuantum.setMoneyNow(c);
				}
			} else if (meterUseQuantum.getUseValue() == null) {
				BigDecimal c1 = new BigDecimal(0);
				BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
				meterUseQuantum.setMoneyNow(c);
			}
		}
		return meterUseHours;
	}

	@Override
	public List<MeterUseQuantum> selectMeterUseday(Page page) {
		List<MeterUseQuantum> meterUseDays = meterUseDayMapper
				.selectByPage(page);
		for (MeterUseQuantum meterUseQuantum : meterUseDays) {
			if (meterUseQuantum.getUseValue() != null) {
				if (meterUseQuantum.getPrice1() != null) {
					BigDecimal bignum1 = (meterUseQuantum.getUseValue())
							.multiply(meterUseQuantum.getPrice1());
					BigDecimal bignum3 = bignum1.setScale(2,
							BigDecimal.ROUND_HALF_DOWN);
					meterUseQuantum.setMoneyNow(bignum3);
				} else if (meterUseQuantum.getPrice1() == null) {
					BigDecimal c1 = new BigDecimal(0);
					BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
					meterUseQuantum.setMoneyNow(c);
				}
			} else if (meterUseQuantum.getUseValue() == null) {
				BigDecimal c1 = new BigDecimal(0);
				BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
				meterUseQuantum.setMoneyNow(c);
			}
		}
		return meterUseDays;
	}

	@Override
	public List<MeterUseQuantum> selectMeterUseMonth(Page page) {
		List<MeterUseQuantum> meterUseMonths = meterUseMonthMapper
				.selectByPage(page);
		for (MeterUseQuantum meterUseQuantum : meterUseMonths) {
			if (meterUseQuantum.getUseValue() != null) {
				if (meterUseQuantum.getPrice1() != null) {
					BigDecimal bignum1 = (meterUseQuantum.getUseValue())
							.multiply(meterUseQuantum.getPrice1());
					BigDecimal bignum3 = bignum1.setScale(2,
							BigDecimal.ROUND_HALF_DOWN);
					meterUseQuantum.setMoneyNow(bignum3);
				} else if (meterUseQuantum.getPrice1() == null) {
					BigDecimal c1 = new BigDecimal(0);
					BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
					meterUseQuantum.setMoneyNow(c);
				}
			} else if (meterUseQuantum.getUseValue() == null) {
				BigDecimal c1 = new BigDecimal(0);
				BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
				meterUseQuantum.setMoneyNow(c);
			}
		}
		return meterUseMonths;
	}

	@Override
	public List<MeterUseQuantum> selectMeterUseYear(Page page) {
		List<MeterUseQuantum> meterUseYears = meterUseYearMapper
				.selectByPage(page);
		for (MeterUseQuantum meterUseQuantum : meterUseYears) {
			if (meterUseQuantum.getUseValue() != null) {
				if (meterUseQuantum.getPrice1() != null) {
					BigDecimal bignum1 = (meterUseQuantum.getUseValue())
							.multiply(meterUseQuantum.getPrice1());
					BigDecimal bignum3 = bignum1.setScale(2,
							BigDecimal.ROUND_HALF_DOWN);
					meterUseQuantum.setMoneyNow(bignum3);
				} else if (meterUseQuantum.getPrice1() == null) {
					BigDecimal c1 = new BigDecimal(0);
					BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
					meterUseQuantum.setMoneyNow(c);
				}
			} else if (meterUseQuantum.getUseValue() == null) {
				BigDecimal c1 = new BigDecimal(0);
				BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
				meterUseQuantum.setMoneyNow(c);
			}
		}
		return meterUseYears;
	}

	@Override
	public List<MeterUseQuantum> selectConditionByDay(MeterUseQuantum node) {
		return meterUseQuantumMapper.selectConditionByDay(node);
	}

	@Override
	public List<MeterUseQuantum> selectConditionByMonth(MeterUseQuantum node) {
		return meterUseQuantumMapper.selectConditionByMonth(node);
	}

	@Override
	public List<MeterUseQuantum> selectConditionByYear(MeterUseQuantum node) {
		return meterUseQuantumMapper.selectConditionByYear(node);
	}

	@Override
	public List<MeterUseQuantum> selectConditionByHour(MeterUseQuantum node) {
		return meterUseQuantumMapper.selectConditionByHour(node);
	}

	@Override
	public List<MeterUseQuantum> historyExpert(MeterUseQuantum useQuantum) {
		// 查询小时
		if ("hour".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseQuantums = meterUseHourMapper
					.selectHistoryExorptByhour(useQuantum);
			for (MeterUseQuantum meterUseQuantum : meterUseQuantums) {

				if (meterUseQuantum.getFreezeTd() != null) {
					Long a = Long.valueOf(meterUseQuantum.getFreezeTd())
							.longValue();
					Long b = a / 100;
					String s = String.valueOf(b);
					meterUseQuantum.setFreezeTd(s);
				} else {
					// meterUseQuantum.setFreezeTd(meterUseQuantum.getFreezeTd());

				}

				if (meterUseQuantum.getUseValue() != null) {
					if (meterUseQuantum.getPrice1() != null) {
						BigDecimal bignum1 = (meterUseQuantum.getUseValue())
								.multiply(meterUseQuantum.getPrice1());
						BigDecimal bignum3 = bignum1.setScale(2,
								BigDecimal.ROUND_HALF_DOWN);
						String str = bignum3.toString();
						if ("0".equals(str)) {
							meterUseQuantum.setMoneyNowStr("0");
						} else {
							meterUseQuantum.setMoneyNowStr(str);
						}
					} else if (meterUseQuantum.getPrice1() == null) {
						BigDecimal c1 = new BigDecimal(0);
						BigDecimal c = c1.setScale(0,
								BigDecimal.ROUND_HALF_DOWN);
						String str = c.toString();
						if ("0".equals(str)) {
							meterUseQuantum.setMoneyNowStr("0");
						} else {
							meterUseQuantum.setMoneyNowStr(str);
						}
					}
				} else if (meterUseQuantum.getUseValue() == null) {
					BigDecimal c1 = new BigDecimal(0);
					BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
					String str = c.toString();
					if ("0".equals(str)) {
						meterUseQuantum.setMoneyNowStr("0");
					} else {
						meterUseQuantum.setMoneyNowStr(str);
					}
				}

				if (meterUseQuantum.getLoopType() == 0) {
					meterUseQuantum.setLoopTypeStr("总回路");
				} else if (meterUseQuantum.getLoopType() == 1) {
					meterUseQuantum.setLoopTypeStr("主回路");
				} else if (meterUseQuantum.getLoopType() == 2) {
					meterUseQuantum.setLoopTypeStr("副回路");
				} else if (meterUseQuantum.getLoopType() == 3) {
					meterUseQuantum.setLoopTypeStr("第三回路");
				}

				if ("110000".equals(meterUseQuantum.getMeterCateg())) {
					meterUseQuantum.setMeterCateg("电表");
				} else if ("120000".equals(meterUseQuantum.getMeterCateg())) {
					meterUseQuantum.setMeterCateg("水表");
				}

				// if("1302".equals(meterUseQuantum.getControlType())){
				// meterUseQuantum.setControlType("系统预付费");
				// }else if("1304".equals(meterUseQuantum.getControlType())){
				// meterUseQuantum.setControlType("不计费");
				// }
			}

			return meterUseQuantums;
		}

		// 查询日
		if ("day".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseQuantums = meterUseDayMapper
					.selectHistoryExorptByday(useQuantum);
			for (MeterUseQuantum meterUseQuantum : meterUseQuantums) {
				// meterUseQuantum.setControlType(
				// meterUseQuantum.getControlType()=="1302"?"不计费":"系统预付费");
				// meterUseQuantum.setMeterCateg(meterUseQuantum.getMeterCateg()=="110000"?"电表":"水表");
				if (meterUseQuantum.getUseValue() != null) {
					if (meterUseQuantum.getPrice1() != null) {
						BigDecimal bignum1 = (meterUseQuantum.getUseValue())
								.multiply(meterUseQuantum.getPrice1());
						BigDecimal bignum3 = bignum1.setScale(2,
								BigDecimal.ROUND_HALF_DOWN);
						String str = bignum3.toString();
						if ("0".equals(str)) {
							meterUseQuantum.setMoneyNowStr("0");
						} else {
							meterUseQuantum.setMoneyNowStr(str);
						}
					} else if (meterUseQuantum.getPrice1() == null) {
						BigDecimal c1 = new BigDecimal(0);
						BigDecimal c = c1.setScale(0,
								BigDecimal.ROUND_HALF_DOWN);
						String str = c.toString();
						meterUseQuantum.setMoneyNowStr(str);
					}
				} else if (meterUseQuantum.getUseValue() == null) {
					BigDecimal c1 = new BigDecimal(0);
					BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
					String str = c.toString();
					meterUseQuantum.setMoneyNowStr(str);
				}

				if (meterUseQuantum.getLoopType() == 0) {
					meterUseQuantum.setLoopTypeStr("总回路");
				} else if (meterUseQuantum.getLoopType() == 1) {
					meterUseQuantum.setLoopTypeStr("主回路");
				} else if (meterUseQuantum.getLoopType() == 2) {
					meterUseQuantum.setLoopTypeStr("副回路");
				} else if (meterUseQuantum.getLoopType() == 3) {
					meterUseQuantum.setLoopTypeStr("第三回路");
				}

				if ("110000".equals(meterUseQuantum.getMeterCateg())) {
					meterUseQuantum.setMeterCateg("电表");
				} else if ("120000".equals(meterUseQuantum.getMeterCateg())) {
					meterUseQuantum.setMeterCateg("水表");
				}

				// if("1302".equals(meterUseQuantum.getControlType())){
				// meterUseQuantum.setControlType("系统预付费");
				// }else if("1304".equals(meterUseQuantum.getControlType())){
				// meterUseQuantum.setControlType("不计费");
				// }
			}
			return meterUseQuantums;
		}
		// 查询月
		if ("month".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseQuantums = meterUseMonthMapper
					.selectHistoryExorptByMonth(useQuantum);
			for (MeterUseQuantum meterUseQuantum : meterUseQuantums) {
				/*
				 * meterUseQuantum.setControlType(
				 * meterUseQuantum.getControlType()=="1302"?"不计费":"系统预付费");
				 * meterUseQuantum
				 * .setMeterCateg(meterUseQuantum.getMeterCateg()==
				 * "110000"?"电表":"水表");
				 */

				if (meterUseQuantum.getUseValue() != null) {
					if (meterUseQuantum.getPrice1() != null) {
						BigDecimal bignum1 = (meterUseQuantum.getUseValue())
								.multiply(meterUseQuantum.getPrice1());
						BigDecimal bignum3 = bignum1.setScale(2,
								BigDecimal.ROUND_HALF_DOWN);
						String str = bignum3.toString();
						if ("0".equals(str)) {
							meterUseQuantum.setMoneyNowStr("0");
						} else {
							meterUseQuantum.setMoneyNowStr(str);
						}
					} else if (meterUseQuantum.getPrice1() == null) {
						BigDecimal c1 = new BigDecimal(0);
						BigDecimal c = c1.setScale(0,
								BigDecimal.ROUND_HALF_DOWN);
						String str = c.toString();
						meterUseQuantum.setMoneyNowStr(str);
					}
				} else if (meterUseQuantum.getUseValue() == null) {
					BigDecimal c1 = new BigDecimal(0);
					BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
					String str = c.toString();
					meterUseQuantum.setMoneyNowStr(str);
				}

				if (meterUseQuantum.getLoopType() == 0) {
					meterUseQuantum.setLoopTypeStr("总回路");
				} else if (meterUseQuantum.getLoopType() == 1) {
					meterUseQuantum.setLoopTypeStr("主回路");
				} else if (meterUseQuantum.getLoopType() == 2) {
					meterUseQuantum.setLoopTypeStr("副回路");
				} else if (meterUseQuantum.getLoopType() == 3) {
					meterUseQuantum.setLoopTypeStr("第三回路");
				}
				if ("1302".equals(meterUseQuantum.getControlType())) {
					if ("110000".equals(meterUseQuantum.getMeterCateg())) {
						meterUseQuantum.setMeterCateg("电表");
					} else if ("120000".equals(meterUseQuantum.getMeterCateg())) {
						meterUseQuantum.setMeterCateg("水表");
					}
					// meterUseQuantum.setControlType("系统预付费");
				} else if ("1304".equals(meterUseQuantum.getControlType())) {
					if ("110000".equals(meterUseQuantum.getMeterCateg())) {
						meterUseQuantum.setMeterCateg("电表");
					} else if ("120000".equals(meterUseQuantum.getMeterCateg())) {
						meterUseQuantum.setMeterCateg("水表");
					}
					// meterUseQuantum.setControlType("不计费");
				}
			}
			return meterUseQuantums;
		}
		// 查询年
		if ("year".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseQuantums = meterUseYearMapper
					.selectHistoryExorptByYear(useQuantum);
			for (MeterUseQuantum meterUseQuantum : meterUseQuantums) {
				/*
				 * meterUseQuantum.setControlType(
				 * meterUseQuantum.getControlType()=="1302"?"不计费":"系统预付费");
				 * meterUseQuantum
				 * .setMeterCateg(meterUseQuantum.getMeterCateg()==
				 * "110000"?"电表":"水表");
				 */

				if (meterUseQuantum.getUseValue() != null) {
					if (meterUseQuantum.getPrice1() != null) {
						BigDecimal bignum1 = (meterUseQuantum.getUseValue())
								.multiply(meterUseQuantum.getPrice1());
						BigDecimal bignum3 = bignum1.setScale(2,
								BigDecimal.ROUND_HALF_DOWN);
						String str = bignum3.toString();
						if ("0".equals(str)) {
							meterUseQuantum.setMoneyNowStr("0");
						} else {
							meterUseQuantum.setMoneyNowStr(str);
						}
					} else if (meterUseQuantum.getPrice1() == null) {
						BigDecimal c1 = new BigDecimal(0);
						BigDecimal c = c1.setScale(0,
								BigDecimal.ROUND_HALF_DOWN);
						String str = c.toString();
						meterUseQuantum.setMoneyNowStr(str);
					}
				} else if (meterUseQuantum.getUseValue() == null) {
					BigDecimal c1 = new BigDecimal(0);
					BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
					String str = c.toString();
					meterUseQuantum.setMoneyNowStr(str);
				}

				if ((meterUseQuantum.getLoopType()) == 0) {
					meterUseQuantum.setLoopTypeStr("总回路");
				} else if ((meterUseQuantum.getLoopType()) == 1) {
					meterUseQuantum.setLoopTypeStr("主回路");
				} else if ((meterUseQuantum.getLoopType()) == 2) {
					meterUseQuantum.setLoopTypeStr("副回路");
				} else if ((meterUseQuantum.getLoopType()) == 3) {
					meterUseQuantum.setLoopTypeStr("第三回路");
				}

				if ("1302".equals(meterUseQuantum.getControlType())) {
					if ("110000".equals(meterUseQuantum.getMeterCateg())) {
						meterUseQuantum.setMeterCateg("电表");
					} else if ("120000".equals(meterUseQuantum.getMeterCateg())) {
						meterUseQuantum.setMeterCateg("水表");
					}
					// meterUseQuantum.setControlType("系统预付费");
				} else if ("1304".equals(meterUseQuantum.getControlType())) {
					if ("110000".equals(meterUseQuantum.getMeterCateg())) {
						meterUseQuantum.setMeterCateg("电表");
					} else if ("120000".equals(meterUseQuantum.getMeterCateg())) {
						meterUseQuantum.setMeterCateg("水表");
					}
					// meterUseQuantum.setControlType("不计费");
				}
			}
			return meterUseQuantums;
		}

		return null;
	}

	/**
	 * 时统计总计量的查询
	 */
	@Override
	public List<MeterUseQuantum> selectHoursStatics(Page page) {
		// TODO Auto-generated method stub
		List<MeterUseQuantum> hoursStatics = meterUseQuantumMapper
				.selectHoursStatics(page);
		for (MeterUseQuantum meterUseQuantum : hoursStatics) {
			if (meterUseQuantum.getFreezeTd() != null) {
				Long a = Long.valueOf(meterUseQuantum.getFreezeTd())
						.longValue();
				Long b = a / 100;
				String s = String.valueOf(b);
				meterUseQuantum.setFreezeTd(s);
			} else {
				meterUseQuantum.setFreezeTd(meterUseQuantum.getFreezeTd());
			}

			if (meterUseQuantum.getUseValue() != null) {
				if (meterUseQuantum.getPrice1() != null) {
					BigDecimal bignum1 = (meterUseQuantum.getUseValue())
							.multiply(meterUseQuantum.getPrice1());
					BigDecimal bignum3 = bignum1.setScale(2,
							BigDecimal.ROUND_HALF_DOWN);
					String str = bignum3.toString();
					if ("0".equals(str)) {
						meterUseQuantum.setMoneyNowStr("0");
					} else {
						meterUseQuantum.setMoneyNowStr(str);
					}
				} else if (meterUseQuantum.getPrice1() == null) {
					BigDecimal c1 = new BigDecimal(0);
					BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
					String str = c.toString();
					meterUseQuantum.setMoneyNowStr(str);
				}
			} else if (meterUseQuantum.getUseValue() == null) {
				BigDecimal c1 = new BigDecimal(0);
				BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
				String str = c.toString();
				meterUseQuantum.setMoneyNowStr(str);
			}

			if (meterUseQuantum.getLoopType() == 0) {
				meterUseQuantum.setLoopTypeStr("总回路");
			} else if (meterUseQuantum.getLoopType() == 1) {
				meterUseQuantum.setLoopTypeStr("主回路");
			} else if (meterUseQuantum.getLoopType() == 2) {
				meterUseQuantum.setLoopTypeStr("副回路");
			} else if (meterUseQuantum.getLoopType() == 3) {
				meterUseQuantum.setLoopTypeStr("第三回路");
			}

			if ("110000".equals(meterUseQuantum.getMeterCateg())) {
				meterUseQuantum.setMeterCateg("电表");
			} else if ("120000".equals(meterUseQuantum.getMeterCateg())) {
				meterUseQuantum.setMeterCateg("水表");
			}

			if ("1302".equals(meterUseQuantum.getControlType())) {
				meterUseQuantum.setControlType("系统预付费");
			} else if ("1304".equals(meterUseQuantum.getControlType())) {
				meterUseQuantum.setControlType("不计费");
			}
		}

		return hoursStatics;
	}

	/**
	 * 时统计总计量的导出
	 */
	@Override
	public List<MeterUseQuantum> hoursStaticsExport(MeterUseQuantum useQuantum) {
		List<MeterUseQuantum> hoursStatics1 = meterUseQuantumMapper
				.hoursStaticsExport(useQuantum);
		for (MeterUseQuantum meterUseQuantum : hoursStatics1) {
			if (meterUseQuantum.getFreezeTd() != null) {
				Long a = Long.valueOf(meterUseQuantum.getFreezeTd())
						.longValue();
				Long b = a / 100;
				String s = String.valueOf(b);
				meterUseQuantum.setFreezeTd(s);
			} else {
				meterUseQuantum.setFreezeTd(meterUseQuantum.getFreezeTd());
			}

			if (meterUseQuantum.getUseValue() != null) {
				if (meterUseQuantum.getPrice1() != null) {
					BigDecimal bignum1 = (meterUseQuantum.getUseValue())
							.multiply(meterUseQuantum.getPrice1());
					BigDecimal bignum3 = bignum1.setScale(2,
							BigDecimal.ROUND_HALF_DOWN);
					String str = bignum3.toString();
					if ("0".equals(str)) {
						meterUseQuantum.setMoneyNowStr("0");
					} else {
						meterUseQuantum.setMoneyNowStr(str);
					}
				} else if (meterUseQuantum.getPrice1() == null) {
					BigDecimal c1 = new BigDecimal(0);
					BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
					String str = c.toString();
					meterUseQuantum.setMoneyNowStr(str);
				}
			} else if (meterUseQuantum.getUseValue() == null) {
				BigDecimal c1 = new BigDecimal(0);
				BigDecimal c = c1.setScale(0, BigDecimal.ROUND_HALF_DOWN);
				String str = c.toString();
				meterUseQuantum.setMoneyNowStr(str);
			}

			if (meterUseQuantum.getLoopType() == 0) {
				meterUseQuantum.setLoopTypeStr("总回路");
			} else if (meterUseQuantum.getLoopType() == 1) {
				meterUseQuantum.setLoopTypeStr("主回路");
			} else if (meterUseQuantum.getLoopType() == 2) {
				meterUseQuantum.setLoopTypeStr("副回路");
			} else if (meterUseQuantum.getLoopType() == 3) {
				meterUseQuantum.setLoopTypeStr("第三回路");
			}

			if ("110000".equals(meterUseQuantum.getMeterCateg())) {
				meterUseQuantum.setMeterCateg("电表");
			} else if ("120000".equals(meterUseQuantum.getMeterCateg())) {
				meterUseQuantum.setMeterCateg("水表");
			}

			if ("1302".equals(meterUseQuantum.getControlType())) {
				meterUseQuantum.setControlType("系统预付费");
			} else if ("1304".equals(meterUseQuantum.getControlType())) {
				meterUseQuantum.setControlType("不计费");
			}
		}

		return hoursStatics1;
	}

	@Override
	public List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum) {
		return meterUseQuantumMapper.selectByDate1(useQuantum);
	}

	/**
	 * 房间用电量的日数据
	 */
	@Override
	public List<MeterUseQuantum> selectDayElc(Page page) {

		List<MeterUseQuantum> dayElc = meterUseDayMapper.selectDayElc(page);
		for (MeterUseQuantum meterUseQuantum : dayElc) {
			if ("1710".equals(meterUseQuantum.getRoomUse())) {
				meterUseQuantum.setRoomUse("商铺");
			} else if ("1720".equals(meterUseQuantum.getRoomUse())) {
				meterUseQuantum.setRoomUse("宿舍");
			}
		}
		return dayElc;
	}

	/**
	 * 房间用电量的月数据
	 */
	@Override
	public List<MeterUseQuantum> selectMonthElc(Page page) {

		List<MeterUseQuantum> monthElec = meterUseMonthMapper
				.selectMonthElc(page);
		for (MeterUseQuantum meterUseQuantum : monthElec) {
			if ("1710".equals(meterUseQuantum.getRoomUse())) {
				meterUseQuantum.setRoomUse("商铺");
			} else if ("1720".equals(meterUseQuantum.getRoomUse())) {
				meterUseQuantum.setRoomUse("宿舍");
			}
		}
		return monthElec;
	}

	/**
	 * 房间用电量的年数据
	 */
	@Override
	public List<MeterUseQuantum> selectYearElc(Page page) {
		List<MeterUseQuantum> yearElec = meterUseYearMapper.selectYearElc(page);
		for (MeterUseQuantum meterUseQuantum : yearElec) {
			if ("1710".equals(meterUseQuantum.getRoomUse())) {
				meterUseQuantum.setRoomUse("商铺");
			} else if ("1720".equals(meterUseQuantum.getRoomUse())) {
				meterUseQuantum.setRoomUse("宿舍");
			}
		}
		return yearElec;
	}

	/**
	 * 根据时标和表号查询出对应天的总用电量
	 * 
	 * @param freezed
	 * @param meterAddr
	 * @return
	 */
	@Override
	public List<MeterUseQuantum> selectDayLineDate(String freezeTd,
			String meterAddr) {
		List<MeterUseQuantum> dayLineDate = meterUseQuantumMapper
				.selectDayLineDate(freezeTd, meterAddr);
		return dayLineDate;
	}

	/**
	 * 根据时标和表号查询出对应月的总用电量
	 */
	@Override
	public List<MeterUseQuantum> selectMonthLineDate(String freezeTd,
			String meterAddr) {
		List<MeterUseQuantum> monthLineDate = meterUseDayMapper
				.selectMonthLineDate(freezeTd, meterAddr);
		return monthLineDate;
	}

	/**
	 * 根据时标和表号来查询出对应年的总用电量
	 */
	@Override
	public List<MeterUseQuantum> selectYearLineDate(String freezeTd,
			String meterAddr) {
		List<MeterUseQuantum> yearLineDate = meterUseMonthMapper
				.selectYearLineDate(freezeTd, meterAddr);
		return yearLineDate;
	}

	/**
	 * 根据表号和时标查询出天折线下面的小时用电量（总回路，主回路，副回路）
	 */
	@Override
	public List<MeterUseQuantum> selectDayLineDate1(String freezeTd,
			String meterAddr) {
		List<MeterUseQuantum> dayLineDate1 = meterUseQuantumMapper
				.selectDayLineDate1(freezeTd, meterAddr);
		return dayLineDate1;
	}

	/**
	 * 根据表号和时标查询出月折线下面的天用电量（总回路，主回路，副回路）
	 */
	@Override
	public List<MeterUseQuantum> selectMonthLineDate1(String freezeTd,
			String meterAddr) {
		List<MeterUseQuantum> monthLineDate1 = meterUseDayMapper
				.selectMonthLineDate1(freezeTd, meterAddr);
		return monthLineDate1;
	}

	/**
	 * 根据表号和时标查询出年折线下面的月用电量（总回路，主回路，副回路）
	 */
	@Override
	public List<MeterUseQuantum> selectYearLineDate1(String freezeTd,
			String meterAddr) {
		List<MeterUseQuantum> yearLineDate1 = meterUseMonthMapper
				.selectYearLineDate1(freezeTd, meterAddr);
		return yearLineDate1;
	}

	@Override
	public List<MeterUseQuantum> selectHouseUseExpert(MeterUseQuantum useQuantum) {

		// 查询日
		if ("day".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseQuantums = meterUseDayMapper
					.selectHouseUseExpertByday(useQuantum);
			for (MeterUseQuantum meterUseQuantum : meterUseQuantums) {
				if ("1710".equals(meterUseQuantum.getRoomUse())) {
					meterUseQuantum.setRoomUse("商铺");
				}
				if ("1720".equals(meterUseQuantum.getRoomUse())) {
					meterUseQuantum.setRoomUse("宿舍");
				}
			}
			return meterUseQuantums;
		}
		// 查询月
		if ("month".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseQuantums = meterUseMonthMapper
					.selectHouseUseExpertByMonth(useQuantum);
			for (MeterUseQuantum meterUseQuantum : meterUseQuantums) {
				if ("1710".equals(meterUseQuantum.getRoomUse())) {
					meterUseQuantum.setRoomUse("商铺");
				}
				if ("1720".equals(meterUseQuantum.getRoomUse())) {
					meterUseQuantum.setRoomUse("宿舍");
				}
			}
			return meterUseQuantums;
		}
		// 查询年
		if ("year".equals(useQuantum.getDateType())) {
			List<MeterUseQuantum> meterUseQuantums = meterUseYearMapper
					.selectHouseUseExpertByYear(useQuantum);
			for (MeterUseQuantum meterUseQuantum : meterUseQuantums) {
				if ("1710".equals(meterUseQuantum.getRoomUse())) {
					meterUseQuantum.setRoomUse("商铺");
				}
				if ("1720".equals(meterUseQuantum.getRoomUse())) {
					meterUseQuantum.setRoomUse("宿舍");
				}
			}
			return meterUseQuantums;
		}

		return null;
	}

	@Override
	@Transactional
	public void meterUseMonthAndYearJob(String format) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Date date = null;
		try {
			date = sdf.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		/**
		 * 月统计
		 */
		calendar.add(Calendar.MONTH, -1);
		Date lastMonthDate = calendar.getTime();
		String lastMonthTime = sdf.format(lastMonthDate).substring(0, 6);

		List<MeterUseMonth> meterUseMonths = meterUseMonthMapper
				.meterUseMonthJob(format, lastMonthTime);
		meterUseMonthMapper.deleteByFreezeTd(format);
		for (MeterUseMonth meterUseMonth : meterUseMonths) {
			meterUseMonth.setId(CommUtils.getUuid());
			meterUseMonth.setCreateTime(new Date(System.currentTimeMillis()));
			meterUseMonthMapper.insertSelective(meterUseMonth);
		}

		/**
		 * 年统计
		 */
		String yearFormat = format.substring(0, 4);
		List<MeterUseYear> meterUseYears = meterUseYearMapper
				.meterUseYearJob(yearFormat);
		meterUseYearMapper.deleteByFreezeTd(yearFormat);
		for (MeterUseYear meterUseYear : meterUseYears) {
			meterUseYear.setId(CommUtils.getUuid());
			meterUseYear.setCreateTime(new Date(System.currentTimeMillis()));
			meterUseYearMapper.insertSelective(meterUseYear);
		}

	}

}