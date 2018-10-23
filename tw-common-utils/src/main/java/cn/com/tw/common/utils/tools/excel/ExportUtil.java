package cn.com.tw.common.utils.tools.excel;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  admin
 * @version  [版本号, 2015年7月7日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ExportUtil
{
    /**
     * 导出excel名称
     */
    public static final String EXCEL_NAME = "file_name";
    /**
     * 导出SHEET名称
     */
    public static final String SHEET_NAME = "sheet_name";
    
    /**
     * 导出excel数据
     */
    public static final String EXCEL_DATA = "db_data";
    
    /**
     * 表头
     */
    public static final String EXCEL_HEADER_DATA = "header_data";
    
    
    /**
     * model
     */
    private Map<String, Object> model;    
    
    /**
     * <默认构造函数>
     */
    public ExportUtil()
    {
        model = new HashMap<String, Object>();
    }
    
    public void setFileName(String name){
        model.put(EXCEL_NAME, name);
    }
    
   /* public void setDbData(List<?> objList){
        model.put(EXCEL_DATA, objList);
    }*/
    public void setSheetName(String name){
        model.put(SHEET_NAME, name);
    }
    
    public void setHeaderData(DocuModel docuModel){
        model.put(EXCEL_HEADER_DATA, docuModel);
    }
    
    public String getFileName(){
        return (String)model.get(EXCEL_NAME);
    }
    
    /*public List<Object> getData(){
        return (List<Object>)model.get(EXCEL_DATA);
    }*/
    
    public DocuModel getDocuModel(){
        return (DocuModel)model.get(EXCEL_HEADER_DATA);
    }

	/**
     * 获取
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, Object> build(){
        return model;
    }
}
