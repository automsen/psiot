package cn.com.tw.common.utils.tools.excel;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.document.AbstractExcelView;


/**
 * 生成excel视图，可用excel工具打开或者保存
 * 由ExportController的return new ModelAndView(viewExcel, model)生成
 * <功能详细描述>
 * 
 * @author  admin
 * @version  [版本号, 2015年7月6日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ViewExcel extends AbstractExcelView
{
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        if (model == null)
        {
            model = new HashMap<String, Object>();
        }
        
        //获取excel名称 默认default
        String excelName =
            StringUtils.isEmpty((String)model.get(ExportUtil.EXCEL_NAME)) ? "default"
                : (String)model.get(ExportUtil.EXCEL_NAME);
        
        // 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开  
        response.setContentType("APPLICATION/OCTET-STREAM");
//        response.setHeader("Content-Disposition",
//            "attachment; filename=" + URLEncoder.encode(excelName + ".xls", "UTF-8"));
        //兼容各种浏览器下载乱码
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((excelName + ".xls").getBytes("GBK"), "iso-8859-1"));
        
        //产生Excel
        String sheetName = (String)model.get(ExportUtil.SHEET_NAME);
        HSSFSheet sheet = workbook.createSheet(StringUtils.isEmpty(sheetName)?String.valueOf(new Date().getTime()):sheetName);
        sheet.setDefaultColumnWidth(15);
        //获取头信息
        DocuModel docu = (DocuModel)model.get(ExportUtil.EXCEL_HEADER_DATA);
        
        List<Header> headers = docu.getHeaders();
        
        //设置样式
        HSSFCellStyle cellStyle = cellStyle(workbook);
        
        /** ========== 添加头信息 ============= start **/
        createHeader(headers, sheet, 0, docu.getDataStartNum());
        /** ========== 添加头信息 ============= end **/
        
        /**========== 添加数据 ============= start **/
        //获取属性字段
        String[] fields = docu.getFields();
        
        //获取数据List
        List<Object> dataList = (List<Object>)docu.getExportData();
        
        createData(dataList, fields, sheet, docu.getDataStartNum(),docu.getDataColNumMin());
        /**========== 添加数据 ============= end **/
        
        // 列总和计算  
        /**HSSFRow row = sheet.createRow(rowNum);
        row.createCell((short)0).setCellValue("TOTAL:");
        String formual = "SUM(D2:D" + rowNum + ")"; // D2到D[rowNum]单元格起(count数据)  
        row.createCell((short)3).setCellFormula(formual);*/
    }
    
    /**
     * 创建表头
     * <功能详细描述>
     * @param headers
     * @param sheet
     * @param startNum
     * @param endNum
     * @see [类、类#方法、类#成员]
     */
    private void createHeader(List<Header> headers, HSSFSheet sheet, int startNum, int headerNum)
    {
        for (int rowNum = 0; rowNum < headerNum; rowNum++)
        {
            HSSFRow row = sheet.createRow(startNum + rowNum);
            for (Header header : headers)
            {
                if (rowNum != header.getRow())
                {
                    continue;
                }
                //通过行数 去 创建行
                row.createCell(header.getColumnStart()).setCellValue(header.getHeaderName());
                
                sheet.addMergedRegion(new CellRangeAddress(startNum + header.getRow(), startNum + header.getRow() + header.getRowStep() - 1,
                    header.getColumnStart(), header.getColumnStart() + header.getColumnStep() - 1));
                //每行设置样式
                //row.setRowStyle(cellStyle);
            }
        }
    }
    
    /**
     * 创建数据
     * <功能详细描述>
     * @param dataList
     * @param fields
     * @param sheet
     * @param startNum
     * @see [类、类#方法、类#成员]
     */
    private int createData(List<Object> dataList, String[] fields, HSSFSheet sheet, int startNum, int startColNum)
    {
    	if(dataList == null){
    		return startNum;
    	}
        for (Object data : dataList)
        {
            //创建行（数据）
            HSSFRow dataRow = sheet.createRow(startNum++);
            
            for (int num = 0; num < fields.length; num++)
            {
                String field = fields[num];
                
                HSSFCell dataCell = dataRow.createCell(num + startColNum);
                
                //判断字段是否是复合字段
                if (field.indexOf(".") > -1)
                {
                    String[] fieldArray = field.split("\\.");
                    
                    Object findObject = data;
                    
                    //通过字段名找到对应的value
                    for (String key : fieldArray)
                    {
                        findObject = findFieldValue(key, findObject);
                    }
                    
                    //判断数据类型 并且复制
                    setObjValue(dataCell, findObject);
                    
                    continue;
                }
                
                Object value = findFieldValue(field, data);
                
                //判断数据类型 并且复制
                setObjValue(dataCell, value);
                
            }
            
            startNum = setChildData(data, startNum, sheet);
        }
        
        return startNum;
    }
    
    /**
     * 设置数据
     * <功能详细描述>
     * @param obj
     * @param startNum
     * @param sheet
     * @see [类、类#方法、类#成员]
     */
    private int setChildData(Object obj, int startNum, HSSFSheet sheet){
        if(obj == null){
            return startNum;
        }
        
        Field[] fileds = obj.getClass().getDeclaredFields();
        
        DocuModel docuModelObject = null;
        
        for (Field field : fileds)
        {
            
            field.setAccessible(true);
            try
            {
                if (field.getType() == DocuModel.class)
                {
                    docuModelObject = (DocuModel)field.get(obj);
                }
                
            }
            catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        if(docuModelObject == null){
            return startNum;
        }
        
        //startNum = startNum + docuModelObject.getDataStartNum();
        //头
        createHeader(docuModelObject.getHeaders(), sheet, startNum, docuModelObject.getDataStartNum());
        
        int dataStartNum = startNum + docuModelObject.getDataStartNum();
        //
        startNum = createData((List<Object>)docuModelObject.getExportData(), docuModelObject.getFields(), sheet, dataStartNum, docuModelObject.getDataColNumMin());
        
        return startNum;
    }
    
    /**
    * EXCLE样式
    * <功能详细描述>
    * @param workbook workbook
    * @return HSSFCellStyle
    * @see [类、类#方法、类#成员]
    */
    private static HSSFCellStyle cellStyle(HSSFWorkbook workbook)
    {
        // *生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        
        // 设置这些样式
        // 前景色
        style.setFillForegroundColor(HSSFColor.BLACK.index);
        
        // 背景色
        style.setFillBackgroundColor(HSSFColor.WHITE.index);
        
        // 填充样式
        //style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        // 设置底边框
        // style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        
        // 设置底边框颜色
        // style.setBottomBorderColor(HSSFColor.BLACK.index);
        
        // 设置左边框
        // style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        
        // 设置左边框颜色
        // style.setLeftBorderColor(HSSFColor.BLACK.index);
        
        // 设置右边框
        // style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
        // 设置右边框颜色
        // style.setRightBorderColor(HSSFColor.BLACK.index);
        
        // 设置顶边框
        // style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        
        // 设置顶边框颜色 
        //      style.setTopBorderColor(HSSFColor.BLACK.index);
        // 设置自动换行
        style.setWrapText(true);
        
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        
        //设置字体
        HSSFFont font = createCellFont(workbook);
        
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param workbook
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static HSSFFont createCellFont(HSSFWorkbook workbook)
    {
        //生成一个字体
        HSSFFont font = workbook.createFont();
        
        //字体颜色
        font.setColor(HSSFColor.BLACK.index);
        
        //字体大小
        font.setFontHeightInPoints((short)10);
        
        //字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        //是否使用斜体
        font.setItalic(false);
        
        //是否使用划线
        font.setStrikeout(false);
        
        //字体名字
        font.setFontName("宋体");
        return font;
    }
    
    /**
     * 找到该字段对应的属性值
     * <功能详细描述>
     * @param strKey
     * @param obj
     * @return
     * @see [类、类#方法、类#成员]
     */
    private Object findFieldValue(String key, Object obj)
    {
    	
    	if (StringUtils.isEmpty(key)) {
    		return "";
    	}
    	
    	String keyFormat = "";
    	
    	if (key.indexOf(":") > -1) {
    		String[] keyArr = key.split(":");
    		
    		if (keyArr.length == 2) {
    			key = keyArr[0];
    			keyFormat = keyArr[1];
    		}
    	}
    	
        if (obj instanceof Map)
        {
        	
        	Object rObj = ((Map)obj).get(key);
        	
        	if (keyFormat.equals("time")) {
        		try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					rObj = sdf.format(new Date((long) rObj));
				} catch (Exception e) {
					rObj = "";
				}
        	}
        	
            return rObj;
        }
        else
        {
            Field[] fileds = obj.getClass().getDeclaredFields();
            for (Field field : fileds)
            {
                
                field.setAccessible(true);
                try
                {
                    if (key.equals(field.getName()))
                    {
                        return field.get(obj);
                    }
                }
                catch (IllegalArgumentException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        return null;
    }
    
    /**
     * 判断基本数据类型 并且赋值
     * <功能详细描述>
     * @param dataCell
     * @param param
     * @see [类、类#方法、类#成员]
     */
    private void setObjValue(HSSFCell dataCell, Object param)
    {
        if (param == null)
        {
            dataCell.setCellValue("");
            return;
        }
        if (param instanceof Integer)
        {
            dataCell.setCellValue(((Integer)param).intValue());
        }
        else if (param instanceof String)
        {
            dataCell.setCellValue((String)param);
        }
        else if (param instanceof Double)
        {
            dataCell.setCellValue(((Double)param).doubleValue());
        }
        else if (param instanceof Float)
        {
            dataCell.setCellValue(((Float)param).floatValue());
        }
        else if (param instanceof Long)
        {
            dataCell.setCellValue(((Long)param).longValue());
        }
        else if (param instanceof Boolean)
        {
            dataCell.setCellValue(((Boolean)param).booleanValue());
        }
        else if (param instanceof Date)
        {
            dataCell.setCellValue((Date)param);
        }
        else
        {
            dataCell.setCellValue(String.valueOf(param));
        }
    }
    
    /**
     * 查找元素的位置
     * <功能详细描述>
     * @param fields fields
     * @param field field
     * @return index
     * @see [类、类#方法、类#成员]
     */
    private int getIndex(String[] fields, String field)
    {
        for (int i = 0; i < fields.length; i++)
        {
            if (fields[i].equals(field))
            {
                return i;
            }
        }
        return 999;
    }
}
