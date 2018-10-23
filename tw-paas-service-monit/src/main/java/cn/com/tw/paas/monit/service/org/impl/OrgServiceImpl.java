package cn.com.tw.paas.monit.service.org.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.common.web.utils.gener.NumberGenerator;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.entity.business.org.OrgExpand;
import cn.com.tw.paas.monit.entity.db.org.Org;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.org.OrgUser;
import cn.com.tw.paas.monit.mapper.org.OrgApplicationMapper;
import cn.com.tw.paas.monit.mapper.org.OrgMapper;
import cn.com.tw.paas.monit.mapper.org.OrgUserMapper;
import cn.com.tw.paas.monit.service.org.OrgService;
import cn.com.tw.paas.monit.service.seria.OrgSeriaService;

@Service
public class OrgServiceImpl implements OrgService{
	
	@Autowired
	private OrgMapper orgMapper;
	@Autowired
	private OrgUserMapper orgUserMapper;
	@Autowired
	private NumberGenerator numberGenerator;
	@Autowired
	private OrgApplicationMapper orgApplicationMapper;
	@Autowired
	private OrgSeriaService orgSeriaService;
	

	@Transactional
	public int deleteById(String orgId) {
		OrgApplication orgApplication = new OrgApplication();
		orgApplication.setOrgId(orgId);
		List<OrgApplication> orgApplications = orgApplicationMapper.selectOrgApplicationAll(orgApplication);
		if(orgApplications != null && orgApplications.size() > 0){
			throw new BusinessException(MonitResultCode.CAN_NOT_DELETE_ERROR, "");
		}
		orgMapper.deleteByPrimaryKey(orgId);
		orgUserMapper.deleteByOrgId(orgId);
		return 0;
	}

	public int insert(Org arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional
	public void insertOrgAndOrgUser(OrgExpand orgExpand) {
		Org org = new Org();//机构
		OrgUser orgUser = new OrgUser();//用户机构
		//String orgId = CommUtils.getUuid();//机构ID
		long incNo = orgSeriaService.inc();
		String orgId = String.valueOf(incNo);
		String orgUserId = CommUtils.getUuid();//机构用户ID
		Date createTime = new Date(System.currentTimeMillis());//创建时间
		
		/**
		 * 校验机构名称重复
		 */
		List<OrgExpand> orgExpands = orgMapper.selectByOrgName(orgExpand.getOrgName());
		if(orgExpands != null && orgExpands.size() >0){
			throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "");
		}
		
		/**
		 * 编号
		 */
		String orgCode = numberGenerator.getId();
		
		//机构塞值添加
		org.setOrgId(orgId);
		org.setOrgCode(orgCode);
		org.setCreateTime(createTime);
		org.setOrgName(orgExpand.getOrgName());
		org.setOrgStatus(orgExpand.getOrgStatus());
		org.setOrgAddr(orgExpand.getOrgAddr());
		org.setParentOrgId(orgExpand.getParentOrgId());
		org.setOrgWebsite(orgExpand.getOrgWebsite());
		org.setOrgLogo(orgExpand.getOrgLogo());
		orgMapper.insertSelective(org);
		
		//用户机构塞值添加
		orgUser.setOrgId(orgId);
		orgUser.setUserId(orgUserId);
		orgUser.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
		orgUser.setCreateTime(createTime);
		orgUser.setRealName(orgExpand.getRealName());
		orgUser.setUserName(orgExpand.getPhone());
		orgUser.setPhone(orgExpand.getPhone());
		orgUser.setEmail(orgExpand.getEmail());
		orgUserMapper.insertSelective(orgUser);
	}

	public Org selectById(String arg0) {
		return orgMapper.selectByPrimaryKey(arg0);
	}

	public List<Org> selectByPage(Page page) {
		List<Org> orgs = orgMapper.selectByPage(page);
		return orgs;
	}

	public int update(Org arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int updateSelect(Org arg0) {
		orgMapper.updateByPrimaryKeySelective(arg0);
		return 0;
	}

	@Override
	public List<OrgExpand> selectOrgAll(OrgExpand orgExpand) {
		List<OrgExpand> orgExpands = orgMapper.selectOrgAll(orgExpand);
		return orgExpands;
	}

	@Override
	public int insertSelect(Org paramT) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public void updateOrgAndOrgUser(OrgExpand orgExpand) {
		Org org = new Org();//机构
		OrgUser orgUser = new OrgUser();//用户机构
		Date updateTime = new Date(System.currentTimeMillis());//创建时间
		
		/**
		 * 校验机构名称重复  通过ID查处机构名称如果与传过来的名称不相同 在判断机构名称是否重复 
		 */
		OrgExpand expand = orgMapper.selectByOrgId(orgExpand.getOrgId());
		if(!expand.getOrgName().equals(orgExpand.getOrgName())){
			List<OrgExpand> orgExpands = orgMapper.selectByOrgName(orgExpand.getOrgName());
			if(orgExpands != null && orgExpands.size() >0){
				throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "");
			}
		}
		
		//机构修改
		org.setOrgId(orgExpand.getOrgId());
		org.setCreateTime(updateTime);
		org.setOrgName(orgExpand.getOrgName());
		org.setOrgStatus(orgExpand.getOrgStatus());
		org.setOrgAddr(orgExpand.getOrgAddr());
		org.setIsUsable(orgExpand.getIsUsable());
		org.setOrgWebsite(orgExpand.getOrgWebsite());
		orgMapper.updateByPrimaryKeySelective(org);
		
		//用户机构修改
		orgUser.setOrgId(orgExpand.getOrgId());
		orgUser.setCreateTime(updateTime);
		orgUser.setRealName(orgExpand.getRealName());
		orgUser.setUserName(orgExpand.getPhone());
		orgUser.setPhone(orgExpand.getPhone());
		orgUser.setEmail(orgExpand.getEmail());
		orgUserMapper.updateByOrgId(orgUser);
	}

	@Override
	public List<OrgExpand> selectOrgExpandByPage(Page page) {
		List<OrgExpand> orgExpands = orgMapper.selectOrgExpandByPage(page);
		return orgExpands;
	}

	@Override
	public OrgExpand selectByOrgId(String orgId) {
		// TODO Auto-generated method stub
		return orgMapper.selectByOrgId(orgId);
	}
	
	/**
	 * 机构编号生成
	 * @return
	 */
	public String getCode(){
		
		OrgExpand orgExpand = new OrgExpand();
		String orgCode = "";
		try {
			OrgExpand orgExpand1 = orgMapper.selectOrgCode(orgExpand);
			/**
			 * 没有编号则从 000001开始 有则累加
			 */
			orgCode = orgExpand1.getOrgCode();
			int code = Integer.parseInt(orgCode);
			String str = String.valueOf(code);
			if(StringUtils.isEmpty(orgCode)){
				orgCode = "00000" + 1;
			}else{
				code = code + 1;
				if(str.length() == 1){
					orgCode = "00000" + code;
				}else if(str.length() == 2){
					orgCode = "0000" + code;
				}else if(str.length() == 3){
					orgCode = "000" + code;
				}else if(str.length() == 4){
					orgCode = "00" + code;
				}else if(str.length() == 5){
					orgCode = "0" + code;
				}else if(str.length() == 6){
					orgCode = "" + code;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgCode;
	}

}
