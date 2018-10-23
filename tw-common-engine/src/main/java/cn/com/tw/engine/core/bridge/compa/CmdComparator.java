package cn.com.tw.engine.core.bridge.compa;

import java.util.Comparator;

import cn.com.tw.engine.core.entity.CmdRequest;

/**
 * 
 * @author admin
 *
 */
public class CmdComparator implements Comparator<CmdRequest>{

	@Override
	public int compare(CmdRequest o1, CmdRequest o2) {
		
		if (o2.getCmdLvl() != o1.getCmdLvl()){
			return o2.getCmdLvl() - o1.getCmdLvl();
		}
		
		return (int) (o1.getDate().getTime() - o2.getDate().getTime());
		
	}

}
