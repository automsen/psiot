package cn.com.tw.engine.core.entity;

import java.util.Date;
import java.util.Map;

import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.tools.json.JsonUtils;

/**
 * 
 * @author admin
 *
 */
public class CmdResult {
	
	private CmdResponse cmdResponse;
	
	private Date lastTime;
	
	private int num = 0;
	
	public CmdResult(){
		this.lastTime = new Date();
	}
	
	public CmdResult(CmdResponse cmdResponse){
		
		this.cmdResponse = cmdResponse;
		
		this.lastTime = new Date();
	}

	public CmdResponse getCmdResponse() {
		return cmdResponse;
	}

	public void setCmdResponse(CmdResponse cmdResponse) {
		this.cmdResponse = cmdResponse;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "CmdResult [cmdResponse=" + cmdResponse + ", lastTime="
				+ lastTime + ", num=" + num + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CmdResult){
			
			CmdResult cmdRes = (CmdResult) obj;
			CmdResponse cmdResp = cmdRes.getCmdResponse();
			
			if (cmdResp == null || this.getCmdResponse() == null) {
				return false;
			}
			
			return compareIsEqual(cmdResp);
			
		}
		
		return super.equals(obj);
	}
	
	private boolean compareIsEqual(CmdResponse cmdRes){
		Object respObj = cmdRes.getData();
		
		Map<String, Object> newResult = parseResult(respObj);
		
		String newDataMarker = (String)newResult.get("dataMarker");
		
		newDataMarker = StringUtils.isEmpty(newDataMarker) ? "" : newDataMarker;
		
		String newAddr = (String) newResult.get("addr");
		
		Map<String, Object> oldResult = parseResult(this.getCmdResponse().getData());
		
		String oldDataMarker = (String)oldResult.get("dataMarker");
		
		oldDataMarker = StringUtils.isEmpty(oldDataMarker) ? "" : oldDataMarker;
		
		String oldAddr = (String) oldResult.get("addr");
		
	    //如果数据标识为空，只有写指令下发返回会产生,这种情况也判重
		if (oldDataMarker.equals(newDataMarker) && oldAddr.equals(newAddr)){
			return true;
		}
		
		return false;
	}
	
	private Map<String, Object> parseResult(Object obj){
		
		if (obj == null){
			return null;
		}
		
		String JsonStr = JsonUtils.objectToJson(obj);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> result = JsonUtils.jsonToPojo(JsonStr, Map.class);
		
		return result;
		
	}
	

}
