package cn.com.tw.common.web.entity.page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Page implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 当前页
	 */
	private int page;

	/**
	 * 分页获得的数据
	 */
	private List<?> data;

	/**
	 * 一页显示多少记录
	 */
	private int rows;

	/**
	 * 总记录数
	 */
	private long totalRecord;
	
	/**
	 * 其他的参数我们把它分装成一个Map对象  
	 */
	private Object paramObj = null;

	public Page() {
		totalPage = 0;
		page = 1;
		rows = 20;
		totalRecord = 0;
		paramObj = new HashMap<String, Object>();
	}
	
	public Page(int page, int rows){
		totalPage = 0;
		this.page = page;
		this.rows = rows;
		totalRecord = 0;
		paramObj = new HashMap<String, Object>();
	}
	
	public Object getParamObj() {
		return paramObj;
	}

	public void setParamObj(Object paramObj) {
		this.paramObj = paramObj;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
		int totalPage = (int) (totalRecord % rows == 0 ? totalRecord / rows
				: totalRecord / rows + 1);
		this.setTotalPage(totalPage);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if(page < 1){
			page = 1;
		}
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	@Override
	public String toString() {
		return "Page [totalPage=" + totalPage + ", page=" + page + ", data="
				+ data + ", rows=" + rows + ", totalRecord=" + totalRecord
				+ ", paramObj=" + paramObj + "]";
	}

}
