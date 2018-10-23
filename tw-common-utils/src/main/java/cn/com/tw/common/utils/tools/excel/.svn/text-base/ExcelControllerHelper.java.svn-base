package cn.com.tw.common.utils.tools.excel;
import java.util.List;



/**
 * 
 * <控制层导出时的工具类>
 * <功能详细描述>
 * 
 * @author  admin
 * @version  [版本号, 2015年7月29日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ExcelControllerHelper
{
    /**
     * <简单导出>
     * <单行列头,无特殊配置>
     * @param data
     * @param headers
     * @param fields
     * @param fileName
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static ExportUtil simpleExportData(List<?> data, String[] headers, String[] fields, String fileName,String sheetName)
    {
        DocuModel docuModel = new DocuModel();
        docuModel.addHeader(headers,0,1);
        docuModel.setFields(fields);
        docuModel.setExportData(data);
        ExportUtil exprot = new ExportUtil();
        //添加头信息
        exprot.setHeaderData(docuModel);
        exprot.setSheetName(sheetName);
        //添加文件名，不需要加文件后缀
        exprot.setFileName(fileName);
        
        //添加数据
        //exprot.setDbData(data);
        return exprot;
    }
    
/* 
 *    例子
 *    @RequestMapping(value = "/exportReport.do")
    public ModelAndView exportExecl(HttpServletRequest request, HttpServletResponse response, String sdate,String edate) throws ParseException {
        List<OaAttendanceSummary>  list = dwrAttendanceSummaryService.query(sdate, edate);
        logger.info("根据开始时间"+sdate+"结束时间"+edate+"导出统计考勤");
            
        //添加头信息
        String[] headers = {"员工姓名","全勤","休假（天）","事假（天）","病假（天）","早退（次）","迟到（次）"};
        //设置头字段名，对应实体类字段，必须一一对应, 顺序与头信息保持一致
        String[] fields = {"userName", "fullTime", "onLeave", "affairLeave", "sickLeave", "leaveEarly","lateNumber"};
        //添加文件名，不需要加文件后缀
        String fileName = dwrAttendanceSummaryService.getReporTtitle(sdate,edate);
        ExportUtil exprot  = ExcelControllerHelper.simpleExportData(list, headers, fields, fileName,"考勤统计");
        return new ModelAndView(new ViewExcel(), exprot.build());
    }*/
}
