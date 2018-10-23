package cn.com.tw.engine.core.bridge.compa;

import java.util.Comparator;

import cn.com.tw.engine.core.entity.CmdResult;

/**
 * 
 * @author admin
 *
 */
public class ResultComparator implements Comparator<CmdResult>{

	@Override
	public int compare(CmdResult o1, CmdResult o2) {
		
		return (int) (o1.getLastTime().getTime() - o2.getLastTime().getTime());
		
	}
	
}