package cn.com.tw.saas.serv.service.rule.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.rule.EnergyPriceMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.rule.EnergyPriceService;

@Service
public class EnergyPriceServiceImpl implements EnergyPriceService{
	
	@Autowired
	private EnergyPriceMapper ladderPriceMapper;
	@Autowired
	private MeterMapper meterMapper;

	@Override
	public int deleteById(String priceId) {
		Meter meter = new Meter();
		meter.setPriceId(priceId);
		List<Meter> meters = meterMapper.selectByEntity(meter);
		if(meters != null && meters.size() > 0){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该规则已被使用");
		}
		return ladderPriceMapper.deleteByPrimaryKey(priceId);
	}

	@Override
	@Transactional
	public int insert(EnergyPrice arg0) {
		// TODO Auto-generated method stub
		/**
		 * 规则名称重复校验
		 */
		EnergyPrice price = ladderPriceMapper.selectByPriceName(arg0);
		if(price != null){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该规则名已被使用");
		}
		
		/**
		 *判断是否为默认规则 
		 */
		if(arg0.getIsDefault() == 1){
			EnergyPrice energyPrice = new EnergyPrice();
			/**
			 * 查出机构下的默认规则
			 */
			energyPrice.setOrgId(arg0.getOrgId());
			energyPrice.setIsDefault(arg0.getIsDefault());
			energyPrice.setEnergyType(arg0.getEnergyType());
			List<EnergyPrice> energyPrices = ladderPriceMapper.selectByBean(energyPrice);
			/**
			 * 判断是否为第一条插进来的 默认规则
			 */
			if(energyPrices != null && energyPrices.size() > 0){
				energyPrice = energyPrices.get(0);
				energyPrice.setIsDefault((byte)0);
				ladderPriceMapper.updateByPrimaryKeySelective(energyPrice);
			}
		}
		// 目前暂不支持阶梯计价
		arg0.setStepNum((byte) 0);
		return ladderPriceMapper.insert(arg0);
	}

	@Override
	public EnergyPrice selectById(String arg0) {
		// TODO Auto-generated method stub
		return ladderPriceMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<EnergyPrice> selectByPage(Page page) {
		// TODO Auto-generated method stub
		return ladderPriceMapper.selectByPage(page);
	}

	@Override
	@Transactional
	public int updateSelect(EnergyPrice ladderPrice) {
		
		
		/**
		 * 规则名称重复校验 先判断规则名称是否发生改变
		 */
		EnergyPrice energyPrice1 = ladderPriceMapper.selectByPrimaryKey(ladderPrice.getPriceId());
		if(!energyPrice1.getPriceName().equals(ladderPrice.getPriceName())){
			EnergyPrice price = ladderPriceMapper.selectByPriceName(ladderPrice);
			if(price != null){
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "该规则名已被使用");
			}
		}
		
		/**
		 *判断是否为默认规则 
		 */
		if(ladderPrice.getIsDefault() == 1){
			EnergyPrice energyPrice = new EnergyPrice();
			/**
			 * 查出机构下的默认规则
			 */
			energyPrice.setOrgId(ladderPrice.getOrgId());
			energyPrice.setIsDefault(ladderPrice.getIsDefault());
			energyPrice.setEnergyType(ladderPrice.getEnergyType());
			List<EnergyPrice> energyPrices = ladderPriceMapper.selectByBean(energyPrice);
			/**
			 * 判断是否为第一条插进来的 默认规则
			 */
			if(energyPrices != null && energyPrices.size() > 0){
				energyPrice = energyPrices.get(0);
				energyPrice.setIsDefault((byte)0);
				ladderPriceMapper.updateByPrimaryKeySelective(energyPrice);
			}
		}
		return ladderPriceMapper.updateByPrimaryKeySelective(ladderPrice);
	}

	@Override
	public List<EnergyPrice> selectByBean(EnergyPrice ladderPrice) {
		return ladderPriceMapper.selectByBean(ladderPrice);
	}

}
