package cn.com.tw.paas.service.queue.eum;
/**
 * oId orgId
 * aId appId
 * aKy appKey
 * oNm orgName
 * aNm appName
 * tTy 终端类型
 * tETy 终端电表类型
 * tNO 终端号 
 * bNO 业务号
 * tNNo 终端网络号
 * pUl 推动URL
 * dTy 数据类型
 * pUs 推送状态
 * pTs 推送次数
 * pTm 推送时间
 * pC 推送内容
 * rTm 读取时间采集时间
 * cTm 数据创建时间
 * at 是否自动 1表示手动 0表示自动
 * lTy 回路类型 0总回路/单回路， 1表示子回路1，2表示子回路2，3表示子回路3
 * dm 数据标识
 * @author admin
 *
 */
public enum HisColEnum {
	oId, aId, aKy, oNm, 
	aNm, tTy, tETy, tNo, bNo,
	tNNo, dTy, pUl, pUs, pTs, pTm, rTm, cTm, at, lTy, dm
}
