package cn.com.tw.common.hb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.NullComparator;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.com.tw.common.hb.entity.ColumnInfo;
import cn.com.tw.common.hb.entity.HBPage;
import cn.com.tw.common.hb.utils.ClassUtils;

@Component
public class HBaseOperaUtils {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    /* @Autowired
    private Configuration configuration;*/
    
    @Autowired
    private Connection connection;

    public void createTable(String tableName, String... families) {
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
        
        try {
        	
            Admin admin = connection.getAdmin();
            for (String family : families) {
                tableDescriptor.addFamily(new HColumnDescriptor(family));
            }
            if (admin.tableExists(TableName.valueOf(tableName))) {
                System.out.println("Table Exists");
                logger.info("Table:[" + tableName + "] Exists");
            } else {
                admin.createTable(tableDescriptor);
                System.out.println("Create table Successfully!!!Table Name:[" + tableName + "]");
                logger.info("Create table Successfully!!!Table Name:[" + tableName + "]");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public void deleteTable(String tableName) throws IOException {
        Admin admin = connection.getAdmin();
        TableName table = TableName.valueOf(tableName);
        if (!admin.tableExists(TableName.valueOf(tableName))) {
            logger.info("[" + tableName + "] is not existed. Delete failed!");
            return;
        }
        admin.disableTable(table);
        admin.deleteTable(table);
        System.out.println("delete table " + tableName + " successfully!");
        logger.info("delete table " + tableName + " successfully!");
    }

    public void putRowValue(String tableName, String rowKey, String familyColumn, String columnName, String value) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(familyColumn), Bytes.toBytes(columnName), Bytes.toBytes(value));
        table.put(put);
        logger.info("update table:" + tableName + ",rowKey:" + rowKey + ",family:" + familyColumn + ",column:" + columnName + ",value:" + value + " successfully!");
        System.out.println("Update table success");
    }

    public void putRowValueBatch(String tableName, String rowKey, String familyColumn, List<String> columnNames, List<String> values) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        for (int j = 0; j < columnNames.size(); j++) {
            put.addColumn(Bytes.toBytes(familyColumn), Bytes.toBytes(columnNames.get(j)), Bytes.toBytes(values.get(j)));
        }
        table.put(put);
        logger.info("update table:" + tableName + ",rowKey:" + rowKey + ",family:" + familyColumn + ",columns:" + columnNames + ",values:" + values + " successfully!");
        System.out.println("Update table success");
    }

    public void putRowValueBatch(String tableName, String rowKey, String familyColumn, Map<String, Object> columnValues) throws IOException {
    	logger.info("begin to update table:" + tableName + ",rowKey:" + rowKey + ",family:" + familyColumn + ",columnValues:" + columnValues.toString());
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        for (Map.Entry<String, Object> entry : columnValues.entrySet()) {

        	Object value = entry.getValue();
        	
        	if (StringUtils.isEmpty(value)) {
        		continue;
        	}
        	
        	byte[] valBts = ClassUtils.obj2Byte(value);
        	
        	if (valBts == null) {
        		continue;
        	}
        	
            put.addColumn(Bytes.toBytes(familyColumn), Bytes.toBytes(entry.getKey()), valBts);
        }
        table.put(put);
        logger.info("update table:" + tableName + ",rowKey:" + rowKey + " successfully!");
        System.out.println("Update table success");
    }
    
    public long incrColumnValue(String tableName, String rowKey, String familyName, String colName) throws IOException {
    	Table table = connection.getTable(TableName.valueOf(tableName));
    	long cnt1=table.incrementColumnValue(Bytes.toBytes(rowKey), Bytes.toBytes(familyName), Bytes.toBytes(colName),1L);
        return cnt1;
    }
    
    public Result getRowValue(String tableName, String rowKey, List<String> cfNames) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        
        if (cfNames != null && !cfNames.isEmpty()) {
        	for (String cfName : cfNames) {
        		if (StringUtils.isEmpty(cfName) || cfName.indexOf(":") == -1) {
        			continue;
        		}
        		
        		String[] cfNameArr = cfName.split(":");
        		
        		if (cfNameArr.length < 2) {
        			continue;
        		}
        		get.addColumn(Bytes.toBytes(cfNameArr[0]), Bytes.toBytes(cfNameArr[1]));
        		//get.addFamily(Bytes.toBytes(cfName));
        	}
        }
        Result res = table.get(get);
        return res;
    }

    /**
     * 通过abc结尾后缀查询 regexKey = ".*abc$"
     * 包含abc查询 Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,new SubstringComparator("abc"));
     * abc开头的查询Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,new BinaryPrefixComparator("abc".getBytes()));
     * @param tableName
     * @param regexKey
     * @return
     * @throws IOException
     */
    public ResultScanner scanRegexRowKey(String tableName, String regexKey) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexKey));
        scan.setFilter(filter);
        ResultScanner rs = table.getScanner(scan);
        return rs;
    }
    
    public ResultScanner scanByColumnValue(String tableName, String familyColumn, String colValue) throws IOException {
    	
    	if (StringUtils.isEmpty(familyColumn) || familyColumn.indexOf(":") == -1) {
    		return null;
    	}
    	
    	String[] familyColArr = familyColumn.split(":");
    	
    	if (familyColArr.length < 2) {
    		return null;
    	}
    	
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(familyColArr[0]), Bytes.toBytes(familyColArr[1]), CompareOp.EQUAL, Bytes.toBytes(colValue));
        scan.setFilter(filter);
        ResultScanner rs = table.getScanner(scan);
        return rs;
    }

    public void deleteAllColumn(String tableName, String rowKey) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delAllColumn = new Delete(Bytes.toBytes(rowKey));
        table.delete(delAllColumn);
        System.out.println("Delete AllColumn Success");
        logger.info("Delete rowKey:" + rowKey + "'s all Columns Successfully");
    }

    public void deleteColumn(String tableName, String rowKey, String familyName, String columnName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delColumn = new Delete(Bytes.toBytes(rowKey));
        delColumn.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        table.delete(delColumn);
        System.out.println("Delete Column Success");
        logger.info("Delete rowKey:" + rowKey + "'s Column:" + columnName + " Successfully");
    }
    
    public ResultScanner scanListRowKey(String tableName, String startRow, String stopRow, List<ColumnInfo> colInfoList) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        
        FilterList filters = new FilterList();
        Scan scan = new Scan();
        
        for (ColumnInfo colInfo : colInfoList) {
        	if (colInfo != null && !colInfo.isFdEmpty()) {
        		SingleColumnValueFilter rowFilter = new SingleColumnValueFilter(Bytes.toBytes(colInfo.getColfamily()), Bytes.toBytes(colInfo.getColName()),   
        				CompareOp.EQUAL, Bytes.toBytes(colInfo.getColValue())); 
        		filters.addFilter(rowFilter);
        	}
        }
        
        if (!filters.getFilters().isEmpty()) {
        	scan.setFilter(filters);
        }
        
        scan.setStartRow(Bytes.toBytes(startRow));
        scan.setStopRow(Bytes.toBytes(stopRow));
        
        //缓存1000条数据  
        //scan.setCaching(1000);  
        //scan.setCacheBlocks(false);  
        ResultScanner rs = table.getScanner(scan);
        
        return rs;
    }
    
    public ResultScanner scanListRowKeyAndQualifier(String tableName, String startRow, String stopRow, List<ColumnInfo> colInfoList, List<String> cfNames, String qualifierName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        
        setCfNames(scan, cfNames);
        
        FilterList filters = new FilterList();
        
        for (ColumnInfo colInfo : colInfoList) {
        	if (colInfo != null && !colInfo.isFdEmpty()) {
        		SingleColumnValueFilter rowFilter= new SingleColumnValueFilter(Bytes.toBytes(colInfo.getColfamily()), Bytes.toBytes(colInfo.getColName()),   
        				CompareOp.EQUAL, Bytes.toBytes(colInfo.getColValue()));
        		filters.addFilter(rowFilter);
        	}
        }
        
        if (!StringUtils.isEmpty(qualifierName)) {
        	/*QualifierFilter filter = new QualifierFilter(CompareOp.EQUAL, new NullComparator(Bytes.toBytes(qualifierName))); // 列名为 my-column
            filters.addFilter(filter);*/
        	SingleColumnValueFilter rowFilter= new SingleColumnValueFilter(Bytes.toBytes("item"), Bytes.toBytes(qualifierName),   
    				CompareOp.NOT_EQUAL, new NullComparator());
        	filters.addFilter(rowFilter);
        }
        
        if (!filters.getFilters().isEmpty()) {
        	scan.setFilter(filters);
        }
        
        scan.setStartRow(Bytes.toBytes(startRow));
        scan.setStopRow(Bytes.toBytes(stopRow));
        
        //缓存1000条数据  
        //scan.setCaching(1000);  
        //scan.setCacheBlocks(false);  
        ResultScanner rs = table.getScanner(scan);
        
        return rs;
    }
    
    /**
     * 分页，将数据通过条件查询之后，在进行分页
     * @param tableName
     * @param startRow
     * @param stopRow
     * @param page
     * @param colInfoList
     * @param cfNames
     * @return
     * @throws IOException
     */
    public Result[] scanPageRowKey(String tableName, String startRow, String stopRow, HBPage page, List<ColumnInfo> colInfoList, List<String> cfNames) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        
        FilterList filters = new FilterList();
        Scan scan = new Scan();
        setCfNames(scan, cfNames);
        
    	for (ColumnInfo colInfo : colInfoList) {
    		if (colInfo != null && !colInfo.isFdEmpty()) {
    			SingleColumnValueFilter rowFilter = new SingleColumnValueFilter(Bytes.toBytes(colInfo.getColfamily()), Bytes.toBytes(colInfo.getColName()),   
    					CompareOp.EQUAL, Bytes.toBytes(colInfo.getColValue())); 
    			filters.addFilter(rowFilter);
    		}
    	}
        
        if (!filters.getFilters().isEmpty()) {
        	scan.setFilter(filters);
        }
        
        scan.setStartRow(Bytes.toBytes(startRow));
        scan.setStopRow(Bytes.toBytes(stopRow));
        
        //计算起始页
        int firstPage = (page.getPage() - 1) * page.getRows();
        int endPage = firstPage + page.getRows();
        //缓存1000条数据  
        //scan.setCaching(1000);  
        //scan.setCacheBlocks(false);  
        ResultScanner rs = table.getScanner(scan);
        
        int count = 0; 
        
        List<Result> resultList = new ArrayList<Result>();
        //List<byte[]> rowList = new LinkedList<byte[]>();  
        // 遍历扫描器对象， 并将需要查询出来的数据row key取出  
        for (Result result : rs) {  
            //String row = Bytes.toString(result.getRow());  
            if (count >= firstPage && count < endPage) {  
            	resultList.add(result);  
            }  
            count++;  
        }
        
        //List<Get> getList = getList(rowList);
        //Result[] results = table.get(getList);
        page.setTotalRecord(count);
        return resultList.toArray(new Result[resultList.size()]);
    }
    
    /**
     * 通过PageFiler分页
     * @param tableName
     * @param startRow
     * @param stopRow
     * @param page
     * @param colInfoList
     * @param cfNames
     * @return
     * @throws Exception 
     */
    public Result[] scanPageByPageFiler(String tableName, String startRow, String stopRow, HBPage page, List<ColumnInfo> colInfoList, List<String> cfNames) throws Exception{
    	//int count = checkStr(startRow);
    	int pageNum = page.getPage();
    	int rows = page.getRows();
    	
    	/*if (pageNum == 1 && count != 3) {
    		rows = rows / 2;
    	}else */
    	if (pageNum > 1) {
    		startRow = getStartRowKey(tableName, startRow, stopRow, page.getPage(), rows, colInfoList, cfNames);
    	}
		ResultScanner pagedate = getDataByStartRowKeyAndSize(tableName, startRow, stopRow, rows, colInfoList, cfNames);
    	
		List<Result> resultList = new ArrayList<Result>();
	    //List<byte[]> rowList = new LinkedList<byte[]>();  
		// 遍历扫描器对象， 并将需要查询出来的数据row key取出  
		for (Result result : pagedate) {  
		    resultList.add(result);  
		}
		
		if (resultList.size() < page.getRows()) {
			page.setTotalRecord((pageNum - 1) * page.getRows() + resultList.size());
		}else {
			page.setTotalRecord(pageNum * page.getRows() + page.getRows() * 2);
		}
		
		//page.setTotalRecord(pageNum * page.getRows() + page.getRows() * 2);
		
		return resultList.toArray(new Result[resultList.size()]);
    }
    
    /**
     * 判断rowkey 是不是完整 格式
     * @param content
     * @return
     */
    private int checkStr(String content){
    	if (StringUtils.isEmpty(content)) {
    		return 0;
    	}
    	
    	char[] contents = content.toCharArray();
    	int count = 0;
    	for (char cont : contents) {
    		if(cont == '|') {
    			count++;
    		}
    	}
    	return count;
    }
    
    /**
	 * 获取指定页的startRowKey
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception 
	 */
	public String getStartRowKey(String tableName, String startRow, String stopRowKey, int page, int pagesize, List<ColumnInfo> colInfoList, List<String> cfNames) throws Exception{
		
		//解决思路 我们每页获取pagesize+1条记录
		//每次遍历记录下最后一条记录的rowkey，就是下一页的开始rowkey
		String startRowKey = startRow;
		
		for (int i = 1; i < page; i++) {
			//int count = checkStr(startRowKey);
			/*if (i == 1 && count != 3) {
				pagesize = pagesize / 2;
			}*/
			
			ResultScanner data = getDataByStartRowKeyAndSize(tableName, startRowKey, stopRowKey, pagesize + 1, colInfoList, cfNames);
			Iterator<Result> iterator = data.iterator();
			Result next = null;
			int count = 0;
			while (iterator.hasNext()) {
				 next = iterator.next();
				 count++;
			}
			
			if ((count - pagesize) != 1) {
				return startRow;
			}
			startRowKey = Bytes.toString(next.getRow());
		}
		
		return startRowKey;
	}
	

	/**
	 * 通过pageFilter获取指定页的数据
	 * @param startRowKey
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public ResultScanner getDataByStartRowKeyAndSize(String tableName, String startRowKey, String stopRowKey, long pagesize, List<ColumnInfo> colInfoList, List<String> cfNames) throws Exception{
		
		Table table = connection.getTable(TableName.valueOf(tableName));
		
		FilterList filters = new FilterList();
		
		Scan scan = new Scan();
		
		setCfNames(scan, cfNames);
        
    	for (ColumnInfo colInfo : colInfoList) {
    		if (colInfo != null && !colInfo.isFdEmpty()) {
    			SingleColumnValueFilter rowFilter = new SingleColumnValueFilter(Bytes.toBytes(colInfo.getColfamily()), Bytes.toBytes(colInfo.getColName()),   
    					CompareOp.EQUAL, Bytes.toBytes(colInfo.getColValue())); 
    			filters.addFilter(rowFilter);
    		}
    	}
		
		Filter pageFilter = new PageFilter(pagesize);
		filters.addFilter(pageFilter);
		scan.setStartRow(startRowKey.getBytes());
		if (!StringUtils.isEmpty(stopRowKey)) {
			scan.setStopRow(stopRowKey.getBytes());
		}
		scan.setFilter(filters);
		ResultScanner scanner = table.getScanner(scan);
		
		return scanner;
	}
    
    public Result[] getListByKeys(String tableName, List<byte[]> rowList, List<String> cfNames) throws IOException {
		Table table = connection.getTable(TableName.valueOf(tableName));
		List<Get> getList = getList(rowList, cfNames);
        Result[] results = table.get(getList);
        return results;
    }
    
    /**
     * 查询返回hbase表哪些列
     * @param scan
     * @param cfNames
     */
    private void setCfNames(Scan scan, List<String> cfNames) {
            
        if (cfNames != null && !cfNames.isEmpty()) {
        	
        	for (String cfName : cfNames) {
        		
        		if (StringUtils.isEmpty(cfName) || cfName.indexOf(":") == -1) {
        			continue;
        		}
        		
        		String[] cfNameArr = cfName.split(":");
        		
        		if (cfNameArr.length < 2) {
        			continue;
        		}
        		scan.addColumn(Bytes.toBytes(cfNameArr[0]), Bytes.toBytes(cfNameArr[1]));
        	}
        }
    }
    
    private List<Get> getList(List<byte[]> rowList, List<String> cfNames) {
        List<Get> list = new LinkedList<Get>();  
        for (byte[] row : rowList) {  
            Get get = new Get(row);
            
            if (cfNames != null && !cfNames.isEmpty()) {
            	for (String cfName : cfNames) {
            		
            		if (StringUtils.isEmpty(cfName) || cfName.indexOf(":") == -1) {
            			continue;
            		}
            		
            		String[] cfNameArr = cfName.split(":");
            		
            		if (cfNameArr.length < 2) {
            			continue;
            		}
            		get.addColumn(Bytes.toBytes(cfNameArr[0]), Bytes.toBytes(cfNameArr[1]));
            		
            		//get.addFamily(Bytes.toBytes(cfName));
            	}
            }
            list.add(get);  
        }  
        return list;  
    }
}
