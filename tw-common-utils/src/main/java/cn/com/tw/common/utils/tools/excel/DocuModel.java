package cn.com.tw.common.utils.tools.excel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class DocuModel
{
    /**
     * 头部信息
     */
    private List<Header> headers;
    
    /**
     * 字段名 与 实体类对应
     */
    private String[] fields;
    
    /**
     * 记录数据位置
     */
    private int dataStartNum;
    
    /**
     * 记录数据行开始位置
     */
    private int dataColNumMin = 9999;
    
    /**
     * 导出的数据
     */
    private List<?> exportData;
    
    private List<?> countData;
    
    /**
     * <默认构造函数>
     */
    public DocuModel()
    {
        this.headers = new ArrayList<Header>();
    }
    
    /**
     * <默认构造函数>
     */
    public DocuModel(List<Header> headers, String[] fields)
    {
        this.headers = new ArrayList<Header>();
        if(headers != null){
            this.headers = headers;
        }
        this.fields = fields;
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param rowNum
     * @param hs
     * @see [类、类#方法、类#成员]
     */
    public void addHeader(String headerName, int row, int columnStart)
    {
        addHeader(headerName, row, 1, columnStart, 1);
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param headerNames
     * @param row
     * @param rowStep
     * @param columnStep
     * @see [类、类#方法、类#成员]
     */
    public void addHeader(String[] headerNames, int row, int rowStep, int columnStep){
        if(headerNames == null)
        {
            return;
        }
        
        for(int index = 0; index < headerNames.length; index++)
        {
            addHeader(headerNames[index], row, rowStep, index, columnStep);
        }
        
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param headerNames
     * @param row
     * @param rowStep
     * @see [类、类#方法、类#成员]
     */
    public void addHeader(String[] headerNames, int row, int rowStep){
        if(headerNames == null)
        {
            return;
        }
        
        for(int index = 0; index < headerNames.length; index++)
        {
            addHeader(headerNames[index], row, rowStep, index, 1);
        }
        
    }
    
    /**
     * 添加头信息
     * <功能详细描述>
     * @param rowNum
     * @param hs
     * @see [类、类#方法、类#成员]
     */
    public void addHeader(String headerName, int row, int rowStep, int columnStart, int columnStep)
    {
        headers.add(new Header(headerName, row, rowStep, columnStart, columnStep));
        
        initDataStart(row, columnStart);
    }
    
    public List<Header> getHeaders()
    {
        return headers;
    }
    
    public String[] getFields()
    {
        return fields;
    }

    public void setFields(String[] fields)
    {
        this.fields = fields;
    }
    
    public void setFieldsList(List<String> fields)
    {
        this.fields = fields.toArray(new String[fields.size()]);
    }
    
    public void initDataStart(int row, int col){
        if(dataStartNum <= row){
            dataStartNum = row + 1;
        }
        
        if(dataColNumMin >= col){
    		dataColNumMin = col;
    	}
    }

    public List<?> getExportData()
    {
        return exportData;
    }

    public void setExportData(List<?> exportData)
    {
        this.exportData = exportData;
    }
    
    public List<?> getCountData()
    {
        return countData;
    }

    public void setCountData(List<?> countData)
    {
        this.countData = countData;
    }

    public int getDataStartNum()
    {
        return dataStartNum;
    }
    
    public void addNum(){
        dataStartNum++;
    }
    
    public int getDataColNumMin() {
		return dataColNumMin;
	}

	public void setDataColNumMin(int dataColNumMin) {
		this.dataColNumMin = dataColNumMin;
	}

	@Override
    public String toString()
    {
        return "DocuModel [headers=" + headers + ", fields=" + Arrays.toString(fields) + ", dataStartNum="
            + dataStartNum + ", exportData=" + exportData + "]";
    }

}
