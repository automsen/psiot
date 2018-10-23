package cn.com.tw.saas.serv.common.utils;

import java.math.BigDecimal;

import cn.com.tw.saas.serv.entity.business.cust.DeductOrderParams;
import cn.com.tw.saas.serv.entity.db.cust.DeductOrder;
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;

/**
 * 能耗费用
 * 系统计费
 * 扣费订单计算器
 * 
 * @author admin
 *
 */
public class DeductOrderCalculator {
	
	public static DeductOrder generateOrder(DeductOrderParams param, EnergyPrice price) {
		// 无上次扣费时间 或上次扣费时间早于最近关联时间，即为首次扣费
		if (null == param.getLastDeductTime() || param.getStartTime().compareTo(param.getLastDeductTime()) >= 0) {
			param.setLastReadValue(param.getStartRead());
			param.setLastReadValue1(BigDecimal.ZERO);
			param.setLastReadValue2(BigDecimal.ZERO);
			param.setLastReadValue3(BigDecimal.ZERO);
			param.setLastReadValue4(BigDecimal.ZERO);
			param.setLastIsPriceOver((byte) 0);
			param.setDeductNo(1);
		}
		// 无上次扣费信息
		else if (null == param.getDeductNo()) {
			param.setDeductNo(1);
		}
		// 有上次扣费信息
		else if (null != param.getDeductNo()) {
			param.setDeductNo(param.getDeductNo() + 1);
		}
		// (当前示值-基准值)*倍率=用量
		BigDecimal use = param.getReadValue().subtract(param.getLastReadValue()).multiply(param.getMultiple());
		param.setUseValue(use);
		// 单一计价
		if (price.getPriceType().equals("1104")){
			param = uniformPriceCal(param, price);
		}
		// 分时计价
		else if(price.getPriceType().equals("1103")){
			param = timeareaPriceCal(param, price);
		}
		// 需要计算阶梯费用
		if (price.getStepNum()>0){
			if ((byte) 1 == param.getLastIsPriceOver()) {
				param = resetStep(param);
			}
			param = stepPriceCal(param, price);
		}
		
		// 应扣费用
		// 金额超过1才扣费
		if (null == param.getDeductMoney()
				|| param.getDeductMoney().compareTo(new BigDecimal("1")) < 0) {
			// 金额过小时不扣除费用下次再算
			return null;
		}
		if (param.getDeductMoney().compareTo(BigDecimal.ZERO)<=0){
			// 扣费金额为负数时不扣除费用下次再算
			return null;
		}
		// 扣费订单
		DeductOrder order = param;
		return order;
	}

	/**
	 * 重置已使用阶梯量
	 * 
	 * @param param
	 * @return
	 */
	private static DeductOrderParams resetStep(DeductOrderParams param) {
		param.setLastStepValue1(BigDecimal.ZERO);
		param.setLastStepValue2(BigDecimal.ZERO);
		param.setLastStepValue3(BigDecimal.ZERO);
		param.setLastStepValue4(BigDecimal.ZERO);
		return param;
	}

	/**
	 * 计算阶梯价格费用
	 * 
	 * @return
	 */
	private static DeductOrderParams stepPriceCal(DeductOrderParams param, EnergyPrice price) {
		// 未计算的用量
		BigDecimal tempUse = param.getUseValue();
		// 阶梯额外费用
		BigDecimal stepMoney = BigDecimal.ZERO;
		// 阶梯数
		Byte stepNum = price.getStepNum();
		// 上次各阶梯累计已使用量
		BigDecimal lastStepUse[] = new BigDecimal[4];
		lastStepUse[0] = param.getLastStepValue1();
		lastStepUse[1] = param.getLastStepValue2();
		lastStepUse[2] = param.getLastStepValue3();
		lastStepUse[3] = param.getLastStepValue4();
		// 本次各阶梯使用增量
		BigDecimal stepUse[] = { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
		// 各阶梯费用
		BigDecimal money[] = { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
		// 阶梯加价1<边界1<阶梯加价2<边界2<阶梯加价3<边界3<阶梯加价4
		BigDecimal priceValue[] = new BigDecimal[4];
		BigDecimal step[] = new BigDecimal[4];
		priceValue[0] = price.getStep1Price();
		step[0] = price.getStep1Value();
		priceValue[1] = price.getStep2Price();
		step[1] = price.getStep2Value();
		priceValue[2] = price.getStep3Price();
		step[2] = price.getStep3Value();
		priceValue[3] = price.getStep4Price();
		int i = 0;
		// 计算各阶梯(不包含最后一阶梯)
		for (; i < stepNum.intValue() - 2; i++) {
			// 上次用量未超过阶梯边界,且有未计算完的用量
			if (lastStepUse[i].compareTo(step[i]) < 0 && tempUse.compareTo(BigDecimal.ZERO) > 0) {
				// 本次使用量未超过此阶梯
				if (lastStepUse[i].add(tempUse).compareTo(step[i]) < 0) {
					stepUse[i] = tempUse;
					lastStepUse[i] = lastStepUse[i].add(stepUse[i]);
					money[i] = stepUse[i].multiply(priceValue[i]);
					tempUse = BigDecimal.ZERO;
				}
				// 本次使用量超过此阶梯，计算阶梯内的量
				else {
					stepUse[i] = step[i].subtract(lastStepUse[i]);
					lastStepUse[i] = step[i];
					money[i] = stepUse[i].multiply(priceValue[i]);
					tempUse = tempUse.subtract(stepUse[i]);
				}
			}
			stepMoney = stepMoney.add(money[i]);
		}
		// 有未计算完的用量计算最后一阶梯
		if (tempUse.compareTo(BigDecimal.ZERO) > 0) {
			stepUse[i] = tempUse;
			lastStepUse[i] = lastStepUse[i].add(stepUse[i]);
			money[i] = stepUse[i].multiply(priceValue[i]);
		}
		// 阶梯额外费用
		stepMoney = stepMoney.add(money[i]);
		param.setDeductMoney(param.getDeductMoney().add(stepMoney));
		// 阶梯累计用量
		param.setStepValue1(lastStepUse[0]);
		param.setStepValue2(lastStepUse[1]);
		param.setStepValue3(lastStepUse[2]);
		param.setStepValue4(lastStepUse[3]);
		return param;
	}

	/**
	 * 计算分时价格费用
	 * 
	 * @return
	 */
	private static DeductOrderParams timeareaPriceCal(DeductOrderParams param, EnergyPrice price) {
		// 尖
		BigDecimal use1 = param.getReadValue1().subtract(param.getLastReadValue1()).multiply(param.getMultiple());
		BigDecimal money1 = use1.multiply(price.getPrice1());
		param.setUseValue1(use1);
		// 峰
		BigDecimal use2 = param.getReadValue2().subtract(param.getLastReadValue2()).multiply(param.getMultiple());
		BigDecimal money2 = use2.multiply(price.getPrice2());
		param.setUseValue2(use2);
		// 平
		BigDecimal use3 = param.getReadValue3().subtract(param.getLastReadValue3()).multiply(param.getMultiple());
		BigDecimal money3 = use3.multiply(price.getPrice3());
		param.setUseValue3(use3);
		// 谷
		BigDecimal use4 = param.getReadValue4().subtract(param.getLastReadValue4()).multiply(param.getMultiple());
		BigDecimal money4 = use4.multiply(price.getPrice4());
		param.setUseValue4(use4);
		// 总
		BigDecimal totalMoney = money1.add(money2).add(money3).add(money4);
		param.setDeductMoney(totalMoney);
		return param;
	}

	/**
	 * 计算单一价格费用
	 * 
	 * @return
	 */
	private static DeductOrderParams uniformPriceCal(DeductOrderParams param, EnergyPrice price) {
		BigDecimal money = param.getUseValue().multiply(price.getPrice1());
		param.setDeductMoney(money);
		param.setPrice(price.getPrice1().toString());
		return param;
	}
}
