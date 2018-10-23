package cn.com.tw.paas.monit.common.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcleUtils {
	
	public final static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 读取EXCLE2007的方法
	 * @param filePath 文件名
	 * @param obj 对象名
	 * @param rowStart 起点行（从0开始）
	 * @param cellStrart 起点列（从0开始）
	 * @return 对象集合
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static List<Object> readExcel(final String filePath, final Object obj, final int rowStart, final int cellStrart) throws Exception {
		List<Object> aList = null;
		final String extension = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());//截取文件扩展名
		final Class c = obj.getClass();//获得类对象
		final Field[] filds = c.getDeclaredFields();//获得该类中所有的属性
		final File excelFile = new File(filePath); 
		final InputStream is = new FileInputStream(excelFile);// 获取文件输入流
		if ("xlsx".equals(extension)) {//解析2007
			aList = ReadExcleUtils.analyExcle2007(c, is, filds, rowStart, cellStrart);
		} else if ("xls".equals(extension)) {
			aList = ReadExcleUtils.analyExcle2003(c, is, filds, rowStart, cellStrart);
		} else {
			System.out.println("上传文件不正确");
		}
		return aList;
	}
	
	/**
	 * 读取EXCLE2007的方法
	 * @param filePath 文件名
	 * @param obj 对象名
	 * @param rowStart 起点行（从0开始）
	 * @param cellStrart 起点列（从0开始）
	 * @return 对象集合
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static List<Object> readExcelByColumns(final String filePath, final Object obj,final String[] columns, final int rowStart, final int cellStrart) throws Exception {
		List<Object> aList = null;
		final String extension = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());//截取文件扩展名
		final Class c = obj.getClass();//获得类对象
		final File excelFile = new File(filePath); 
		final InputStream is = new FileInputStream(excelFile);// 获取文件输入流
		if ("xlsx".equals(extension)) {//解析2007
			aList = ReadExcleUtils.analyExcle2007ByColumns(c, is, columns, rowStart, cellStrart);
		} else if ("xls".equals(extension)) {
			aList = ReadExcleUtils.analyExcle2003ByColumns(c, is, columns, rowStart, cellStrart);
		} else {
			System.out.println("上传文件不正确");
		}
		return aList;
	}
	
	
	@SuppressWarnings("rawtypes")
	private static List<Object> analyExcle2003ByColumns(final Class c, final InputStream is, final String[] columns, final int rowStart, final int cellStrart) throws Exception {
		Object objs = null;
		Map<String,Object> mapAtt = null;
		final List<Object> aList = new ArrayList<Object>();
		final HSSFWorkbook workbook2003 = new HSSFWorkbook(is);// 创建Excel2003文件对象 
		final HSSFSheet sheet = workbook2003.getSheetAt(0);// 取出第一个工作表，索引是0
		final HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook2003);
		for (int i = rowStart; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);// 获取行对象
			if (row == null) {// 如果为空，不处理  
                continue;  
            }else {
            	mapAtt = new HashMap<String,Object>();
            	for (int j = 0; j < columns.length; j++) {
            		String cellStr = null;// 单元格，最终按字符串处理
    				final HSSFCell cell = row.getCell(j+cellStrart);
    				//判断单元格的数据类型   
    				if (cell != null) {
    					//对时间的特殊处理
    					int dataFormat = cell.getCellStyle().getDataFormat();
//    					if (dataFormat == 14 || dataFormat == 178 || dataFormat == 180 || dataFormat == 181 || dataFormat == 182) {
    					if (dataFormat == 14 || dataFormat == 31 || dataFormat == 57 || dataFormat == 58) {
    						cellStr = ReadExcleUtils.getDateValue2003(cell);
    					} else{
    						switch (cell.getCellType()) {
        					case HSSFCell.CELL_TYPE_NUMERIC://数值
        						BigDecimal db = new BigDecimal(cell.getNumericCellValue());
        						if (db.toString().indexOf(".") != -1) {
        							java.text.DecimalFormat dfomat = new java.text.DecimalFormat("0.000000");
        							cellStr = dfomat.format(db);
        						}else {
        							cellStr = db.toPlainString();
        						}
        						break;
        					case HSSFCell.CELL_TYPE_STRING://字符串
        						cellStr = cell.getStringCellValue();
        						break;
        					case HSSFCell.CELL_TYPE_BOOLEAN://布尔
        						cellStr = String.valueOf(cell.getBooleanCellValue());
        						break;
        					case HSSFCell.CELL_TYPE_FORMULA://公式
        						cellStr = String.valueOf(evaluator.evaluate(cell).getNumberValue());
        						break;
        					case HSSFCell.CELL_TYPE_BLANK://空值
        						cellStr =  "";
        						break;
        					default:
        						cellStr = cell.getStringCellValue();
        						break;
        					}
    					}
    				} else {
                        cellStr = null;
                    }
    				//讲单元格中的数据放入集合中
    				mapAtt.put(columns[j].toLowerCase(), cellStr);
    			}
            }
			
			try {
				objs = c.newInstance();
				ReadExcleUtils.invokeMethod(c, objs, mapAtt);
				aList.add(objs);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return aList;
	}

	@SuppressWarnings("rawtypes")
	private static List<Object> analyExcle2007ByColumns(final Class c, final InputStream is, final String[] columns, final int rowStart, final int cellStrart) throws Exception {
		Object objs = null;
		Map<String,Object> mapAtt = null;
		final List<Object> aList = new ArrayList<Object>();
		final XSSFWorkbook workbook2007 = new XSSFWorkbook(is);// 创建Excel2007文件对象  
		final XSSFSheet sheet = workbook2007.getSheetAt(0);// 取出第一个工作表，索引是0
		final XSSFFormulaEvaluator evaluator=new XSSFFormulaEvaluator(workbook2007);
		for (int i = rowStart; i <= sheet.getLastRowNum(); i++) {
			String cellStr = null;// 单元格，最终按字符串处理
			final XSSFRow row = sheet.getRow(i);// 获取行对象  
			if (row == null) {// 如果为空，不处理  
                continue;  
            }else {
            	mapAtt = new HashMap<String,Object>();
            	for (int j = 0; j < columns.length; j++) {
    				final XSSFCell cell = row.getCell(j+cellStrart);
    				//判断单元格的数据类型   
    				if (cell != null) {
    					//对时间的特殊处理
    					int dataFormat = cell.getCellStyle().getDataFormat();
    					if (dataFormat == 14 || dataFormat == 176 || dataFormat == 178 || dataFormat == 180 || dataFormat == 181 || dataFormat == 182) {
    						cellStr = ReadExcleUtils.getDateValue2007(cell);
    					} else{
    						switch (cell.getCellType()) {
        					case HSSFCell.CELL_TYPE_NUMERIC://数值
        						BigDecimal db = new BigDecimal(cell.getNumericCellValue());
        						if (db.toString().indexOf(".") != -1) {
        							java.text.DecimalFormat dfomat = new java.text.DecimalFormat("0.000000");
        							cellStr = dfomat.format(db);
        						}else {
        							cellStr = db.toPlainString();
        						}
//        						cellStr = db.toPlainString();
//        						cellStr = String.valueOf(cell.getNumericCellValue());
        						break;
        					case HSSFCell.CELL_TYPE_STRING://字符串
        						cellStr = cell.getStringCellValue();
        						break;
        					case HSSFCell.CELL_TYPE_BOOLEAN://布尔
        						cellStr = String.valueOf(cell.getBooleanCellValue());
        						break;
        					case HSSFCell.CELL_TYPE_FORMULA://公式
        						cellStr = String.valueOf(evaluator.evaluate(cell).getNumberValue());
        						break;
        					case HSSFCell.CELL_TYPE_BLANK://空值
        						cellStr =  "";
        						break;
        					default:
        						cellStr = cell.getStringCellValue();
        						break;
        					}
    					}
    				} else {
    				    cellStr = null;
    				}
    				if(cellStr != null && !cellStr.trim().equals("")){
    					//讲单元格中的数据放入集合中
    					mapAtt.put(columns[j].toLowerCase(), cellStr);
    				}
    				if(cellStr == null || cellStr.trim().equals("")){
    					continue;
    				}
    			}
            }
			
			try {
				objs = c.newInstance();
				ReadExcleUtils.invokeMethod(c, objs, mapAtt);
				aList.add(objs);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return aList;
	}

	@SuppressWarnings("rawtypes")
	private static List<Object> analyExcle2007(final Class c, final InputStream is, final Field[] filds, final int rowStart, final int cellStrart) throws Exception {
		Object objs = null;
		Map<String,Object> mapAtt = null;
		final List<Object> aList = new ArrayList<Object>();
		final XSSFWorkbook workbook2007 = new XSSFWorkbook(is);// 创建Excel2007文件对象  
		final XSSFSheet sheet = workbook2007.getSheetAt(0);// 取出第一个工作表，索引是0
		final XSSFFormulaEvaluator evaluator=new XSSFFormulaEvaluator(workbook2007);
		for (int i = rowStart; i <= sheet.getLastRowNum(); i++) {
			String cellStr = null;// 单元格，最终按字符串处理
			final XSSFRow row = sheet.getRow(i);// 获取行对象  
			if (row == null) {// 如果为空，不处理  
                continue;  
            }else {
            	mapAtt = new HashMap<String,Object>();
            	for (int j = 0; j < filds.length; j++) {
    				final XSSFCell cell = row.getCell(j+cellStrart);
    				//判断单元格的数据类型   
    				if (cell != null) {
    					//对时间的特殊处理
    					int dataFormat = cell.getCellStyle().getDataFormat();
    					if (dataFormat == 14 || dataFormat == 176 || dataFormat == 178 || dataFormat == 180 || dataFormat == 181 || dataFormat == 182) {
    						cellStr = ReadExcleUtils.getDateValue2007(cell);
    					} else{
    						switch (cell.getCellType()) {
        					case HSSFCell.CELL_TYPE_NUMERIC://数值
        						BigDecimal db = new BigDecimal(cell.getNumericCellValue());
        						if (db.toString().indexOf(".") != -1) {
        							java.text.DecimalFormat dfomat = new java.text.DecimalFormat("0.000000");
        							cellStr = dfomat.format(db);
        						}else {
        							cellStr = db.toPlainString();
        						}
//        						cellStr = db.toPlainString();
//        						cellStr = String.valueOf(cell.getNumericCellValue());
        						break;
        					case HSSFCell.CELL_TYPE_STRING://字符串
        						cellStr = cell.getStringCellValue();
        						break;
        					case HSSFCell.CELL_TYPE_BOOLEAN://布尔
        						cellStr = String.valueOf(cell.getBooleanCellValue());
        						break;
        					case HSSFCell.CELL_TYPE_FORMULA://公式
        						cellStr = String.valueOf(evaluator.evaluate(cell).getNumberValue());
        						break;
        					case HSSFCell.CELL_TYPE_BLANK://空值
        						cellStr =  "";
        						break;
        					default:
        						cellStr = cell.getStringCellValue();
        						break;
        					}
    					}
    				} else {
    				    cellStr = null;
    				}
    				if(cellStr != null && !cellStr.trim().equals("")){
    					//讲单元格中的数据放入集合中
    					mapAtt.put(filds[j].getName().toLowerCase(), cellStr);
    				}
    			}
            }
			/*if(cellStr == null || cellStr.trim().equals("")){
				continue;
			}*/
			try {
				objs = c.newInstance();
				ReadExcleUtils.invokeMethod(c, objs, mapAtt);
				aList.add(objs);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return aList;
	}
	
	@SuppressWarnings("rawtypes")
	private static List<Object> analyExcle2003(final Class c, final InputStream is, final Field[] filds, final int rowStart, final int cellStrart) throws IOException {
		Object objs = null;
		Map<String,Object> mapAtt = null;
		final List<Object> aList = new ArrayList<Object>();
		final HSSFWorkbook workbook2003 = new HSSFWorkbook(is);// 创建Excel2003文件对象 
		final HSSFSheet sheet = workbook2003.getSheetAt(0);// 取出第一个工作表，索引是0
		final HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook2003);
		for (int i = rowStart; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);// 获取行对象
			if (row == null) {// 如果为空，不处理  
                continue;  
            }else {
            	mapAtt = new HashMap<String,Object>();
            	for (int j = 0; j < filds.length; j++) {
            		String cellStr = null;// 单元格，最终按字符串处理
    				final HSSFCell cell = row.getCell(j+cellStrart);
    				//判断单元格的数据类型   
    				if (cell != null) {
    					//对时间的特殊处理
    					int dataFormat = cell.getCellStyle().getDataFormat();
//    					if (dataFormat == 14 || dataFormat == 178 || dataFormat == 180 || dataFormat == 181 || dataFormat == 182) {
    					if (dataFormat == 14 || dataFormat == 31 || dataFormat == 57 || dataFormat == 58) {
    						cellStr = ReadExcleUtils.getDateValue2003(cell);
    					} else{
    						switch (cell.getCellType()) {
        					case HSSFCell.CELL_TYPE_NUMERIC://数值
        						BigDecimal db = new BigDecimal(cell.getNumericCellValue());
        						if (db.toString().indexOf(".") != -1) {
        							java.text.DecimalFormat dfomat = new java.text.DecimalFormat("0.000000");
        							cellStr = dfomat.format(db);
        						}else {
        							cellStr = db.toPlainString();
        						}
        						break;
        					case HSSFCell.CELL_TYPE_STRING://字符串
        						cellStr = cell.getStringCellValue();
        						break;
        					case HSSFCell.CELL_TYPE_BOOLEAN://布尔
        						cellStr = String.valueOf(cell.getBooleanCellValue());
        						break;
        					case HSSFCell.CELL_TYPE_FORMULA://公式
        						cellStr = String.valueOf(evaluator.evaluate(cell).getNumberValue());
        						break;
        					case HSSFCell.CELL_TYPE_BLANK://空值
        						cellStr =  "";
        						break;
        					default:
        						cellStr = cell.getStringCellValue();
        						break;
        					}
    					}
    				} else {
                        cellStr = null;
                    }
    				//讲单元格中的数据放入集合中
    				mapAtt.put(filds[j].getName().toLowerCase(), cellStr);
    			}
            }
			
			try {
				objs = c.newInstance();
				ReadExcleUtils.invokeMethod(c, objs, mapAtt);
				aList.add(objs);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return aList;
	}
	
	 /**
	  * 返回时间内的特殊时间格式 OFFICE2003
	  * @param cell
	  * @return
	  */
	 private static String getDateValue2003(HSSFCell cell){
		 if(cell.getDateCellValue() != null){
			 return DEFAULT_DATE_FORMAT.format(cell.getDateCellValue());
		 }
		return null;
	 }
	 
	 /**
	  * 返回时间内的特殊时间格式 OFFICE2007
	  * @param cell
	  * @return
	  */
	 private static String getDateValue2007(XSSFCell cell){
	     //System.out.println( cell.getStringCellValue()+"-----");
		 if(!"".equals(cell.getRawValue())&& cell.getDateCellValue() != null){
			 return DEFAULT_DATE_FORMAT.format(cell.getDateCellValue());
		 }
		 return null;
	 }
	
	/**
	 * java反射类并执行类方法
	 * @param c 类对象
	 * @param obj 对象名
	 * @param mapAtt EXCLE数据集合
	 */
	@SuppressWarnings("rawtypes")
	public static void invokeMethod(final Class c, final Object obj, final Map<String,Object> mapAtt) {
		final Method[] methods = c.getMethods();//获取该类中所有公共方法
		for (int i = 0; i < methods.length; i++) {
			if(methods[i].getName().length()<=3 || !methods[i].getName().toLowerCase().startsWith("set")){
				continue;
			}
			String methodName = methods[i].getName().toLowerCase().substring(3);
			if(!mapAtt.containsKey(methodName)){
				continue;
			}else{
				try {
					methods[i].invoke(obj, mapAtt.get(methodName));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
