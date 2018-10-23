package cn.com.tw.common.utils.tools.quartz.utils;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;

/**
 * cron 表达式解析
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  admin
 * @version  [版本号, 2015年10月8日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CronExUtil
{
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param cronEx
     * @param next
     * @return
     * @throws ParseException
     * @see [类、类#方法、类#成员]
     */
    public static String getCronNextValidTime(String cronEx, int next)
        throws ParseException
    {
        if (next < 1)
        {
            return "";
        }
        
        Date dd = getNextValidTime(cronEx, next);
        
        return DateFormatUtil.format("yyyy-MM-dd HH:mm:ss", dd);
    }
    
    public static Date getNextValidTime(String cronEx, int next) throws ParseException{
    	  CronExpression exp = new CronExpression(cronEx);
          Date dd = new Date();
          for (int i = 0; i < next; i++)
          {
              dd = exp.getNextValidTimeAfter(dd);
              
              if (i == (next - 1))
              {
                  break;
              }
              dd = new Date(dd.getTime() + 1000L);
          }
          return dd;
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param cronEx
     * @return
     * @throws ParseException
     * @see [类、类#方法、类#成员]
     */
    public static String getCronNextValidTimeAfter(String cronEx)
        throws ParseException
    {
        return getCronNextValidTime(cronEx, 1);
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param cronEx
     * @param next
     * @return
     * @throws ParseException
     * @see [类、类#方法、类#成员]
     */
    public static String[] getCronNextValidTimeArray(String cronEx, int next)
        throws ParseException
    {
        
        if (next < 1)
        {
            return new String[1];
        }
        
        String[] dateArray = new String[next];
        
        CronExpression exp = new CronExpression(cronEx);
        Date dd = new Date();
        for (int i = 0; i < next; i++)
        {
            dd = exp.getNextValidTimeAfter(dd);
            dateArray[i] = DateFormatUtil.format("yyyy-MM-dd HH:mm:ss", dd);
            dd = new Date(dd.getTime() + 1000L);
        }
        
        return dateArray;
    }
    
    public static long getIntervalTime(String cronEx) throws ParseException{
    	Date curdate = getNextValidTime(cronEx, 1);
    	Date nextdate = getNextValidTime(cronEx, 2);
    	long intervTimeM = (nextdate.getTime() - curdate.getTime());
    	return intervTimeM;
    }
}
