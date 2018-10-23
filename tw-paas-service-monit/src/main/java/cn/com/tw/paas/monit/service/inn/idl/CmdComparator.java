package cn.com.tw.paas.monit.service.inn.idl;

import java.util.Comparator;


/**
 * 
 * @author admin
 *
 */
@SuppressWarnings("rawtypes")
public class CmdComparator implements Comparator<TaskThread>{

	@Override
	public int compare(TaskThread o1, TaskThread o2) {
		if(o2.getPriority() != o1.getPriority()){
			return o2.getPriority() - o1.getPriority();
		}
		Long time1 = Long.valueOf(o1.getExecuteTime());
		Long time2 = Long.valueOf(o2.getExecuteTime());
		return time2.intValue() - time1.intValue();
	}

}
