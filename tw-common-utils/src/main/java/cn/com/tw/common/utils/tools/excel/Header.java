package cn.com.tw.common.utils.tools.excel;
import java.util.Map;

/**
 * excel头部信息
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  admin
 * @version  [版本号, 2015年7月7日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Header
{
    /**
     * 行
     */
    private int row;
    
    /**
     * 行跨数
     */
    private int rowStep;
    
    /**
     * 从第几列开始
     */
    private int columnStart;
    
    /**
     * 列跨数
     */
    private int columnStep;
    
    /**
     * 头信息
     */
    private String headerName;
    
    /**
     * 扩展
     */
    private Map<String, Object> attr;

    /**
     * <默认构造函数>
     */
    public Header(String headerName, int row, int rowStep, int columnStart, int columnStep)
    {
        this.headerName = headerName;
        this.row = row;
        this.rowStep = rowStep;
        this.columnStart = columnStart;
        this.columnStep = columnStep;
    }
    
    public Map<String, Object> getAttr()
    {
        return attr;
    }

    public void setAttr(Map<String, Object> attr)
    {
        this.attr = attr;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getColumnStart()
    {
        return columnStart;
    }

    public void setColumnStart(int columnStart)
    {
        this.columnStart = columnStart;
    }

    public int getRowStep()
    {
        return rowStep;
    }

    public void setRowStep(int rowStep)
    {
        this.rowStep = rowStep;
    }

    public int getColumnStep()
    {
        return columnStep;
    }

    public void setColumnStep(int columnStep)
    {
        this.columnStep = columnStep;
    }

    public String getHeaderName()
    {
        return headerName;
    }

    public void setHeaderName(String headerName)
    {
        this.headerName = headerName;
    }

    @Override
    public String toString()
    {
        return "Header [row=" + row + ", rowStep=" + rowStep + ", columnStart=" + columnStart + ", columnStep="
            + columnStep + ", headerName=" + headerName + ", attr=" + attr + "]";
    }


}

