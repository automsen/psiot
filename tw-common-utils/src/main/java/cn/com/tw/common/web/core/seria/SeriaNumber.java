package cn.com.tw.common.web.core.seria;

/**
 * 维护自己的序列号
 * @author admin
 */
public abstract class SeriaNumber {
	
	/**
	 * 增长间隔
	 */
	private int interval = 1;
	
	private long maxNum = Long.MAX_VALUE;
      
    public SeriaNumber() {}  
      
    public SeriaNumber(int interval, long maxNum) {  
        this.interval = interval;  
        this.maxNum = maxNum;  
    }  
    
    public synchronized long inc() {
    	
    	long readNum = readNum();
    	
    	long updateNum = readNum + interval;
    	
    	updateNum(updateNum);
    	
    	return updateNum;
    }
    
    protected abstract long readNum();
    
    protected abstract int updateNum(long updateNum);
    
}  