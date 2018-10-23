package cn.com.tw.paas.service.es.dao.realdata;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.es.common.utils.CamelCaseUtils;

@Repository
public class RealDataDao {
	
	@Autowired
	private TransportClient client;
	
	//表示mysql的数据库名
	private String indexName = "paasdb";
	
	//表示mysql的数据表名
	private String typeName = "terminal_data";
	
	/**
	 * 创建信息（创建一个document）
	 * @param client
	 */
	/*public void createEmployee() throws Exception {
		IndexResponse response = client.prepareIndex("company", "employee", "1")
				.setSource(XContentFactory.jsonBuilder().startObject()
				.field("name", "jack")
				.field("age", 27)
				.field("position", "technique")
				.field("country", "china")
				.field("join_date", "2017-01-01")
				.field("salary", 10000).endObject()).get();
		System.out.println(response.getShardInfo()); 
	}*/
	
	public List<Object> queryReadHistory(Page page) {
		
		List<Object> result = new ArrayList<Object>();
		
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName)
				.setTypes(typeName)
				.setQuery(QueryBuilders.matchAllQuery())//查询所有
				//.setQuery(QueryBuilders.matchQuery("position", "technique"))
				//.setPostFilter(QueryBuilders.rangeQuery("age").from(30).to(40))
				.addSort("create_time", SortOrder.DESC)//通过创建时间排序
				.setFrom(((page.getPage() - 1) < 0 ? 0 : (page.getPage() - 1)) * page.getRows()).setSize(page.getRows());//排序
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>) page.getParamObj();
		
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();  
  
		//仪表类型
		String meterType = (String) params.get("meterType");
		if (!StringUtils.isEmpty(meterType)) { 
			queryBuilder.must(QueryBuilders.queryStringQuery(meterType).field("meter_type"));  
		}  
		
		//机构
		String orgId = (String) params.get("orgId");
		if (!StringUtils.isEmpty(orgId)) { 
			queryBuilder.must(QueryBuilders.queryStringQuery(orgId).field("org_id"));  
		} 
		
		//开始时间
		String startTimeStr = (String) params.get("startTime");
		//结束时间
		String endTimeStr = (String) params.get("endTime");
		if (!StringUtils.isEmpty(startTimeStr) || !StringUtils.isEmpty(endTimeStr)) { 
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = null;
			Date endTime = null;
			try {
				startTime = df.parse(startTimeStr);
				endTime = df.parse(endTimeStr); 
			} catch (ParseException e) {
				e.printStackTrace();
			}  
			
			queryBuilder.must(QueryBuilders.rangeQuery("read_time").from(startTime.getTime()).to(endTime.getTime()));  
		} 
		
  
		//仪表地址
		String meterAddr = (String) params.get("meterAddr");
		if (!StringUtils.isEmpty(meterAddr)) {  
			queryBuilder.must(QueryBuilders.queryStringQuery(meterAddr).field("meter_addr"));  
		}  
  
		//如果都为空，查询所有, 如果不是，按条件查询
		if (StringUtils.isEmpty(meterType) && StringUtils.isEmpty(meterAddr) && StringUtils.isEmpty(orgId) && StringUtils.isEmpty(startTimeStr) && StringUtils.isEmpty(endTimeStr)) {  
			searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());  
		}else {
			searchRequestBuilder.setQuery(queryBuilder);
		}
		
		//将查询到的信息返回到response对象中
		SearchResponse response = searchRequestBuilder.get();
		
		SearchHit[] searchHits = response.getHits().getHits();
		
		for(int i = 0; i < searchHits.length; i++) {
			@SuppressWarnings("unchecked")
			Map<String, Object> document = JsonUtils.jsonToPojo(CamelCaseUtils.toCamelCase(searchHits[i].getSourceAsString()), Map.class);
			if (document == null) {
				continue;
			}
			result.add(document);
		}
		
		page.setTotalRecord(response.getHits().getTotalHits());
		
		return result;
		//GetResponse response = client.prepareGet(indexName, typeName, "1").get();
		
	}
	
	/**
	 * 获取员工信息
	 * @param client
	 * @throws Exception
	 */
	public void getEmployee() throws Exception {
		GetResponse response = client.prepareGet("company", "employee", "1").get();
		System.out.println(response.getSourceAsString()); 
	}
	
	/**
	 * 修改员工信息
	 * @param client
	 * @throws Exception
	 */
	public void updateEmployee() throws Exception {
		UpdateResponse response = client.prepareUpdate("company", "employee", "1") 
				.setDoc(XContentFactory.jsonBuilder().startObject().field("position", "technique manager").endObject())
				.get();
		System.out.println(response.getShardInfo());  
 	}
	
	/**
	 * 删除 员工信息
	 * @param client
	 * @throws Exception
	 */
	public void deleteEmployee() throws Exception {
		DeleteResponse response = client.prepareDelete("company", "employee", "1").get();
		System.out.println(response.getShardInfo());  
	}
	
	/**
	 * 执行搜索操作
	 * @param client
	 */
	public void executeSearch(TransportClient client) {
		SearchResponse response = client.prepareSearch("company")
				.setTypes("employee")
				.setQuery(QueryBuilders.matchQuery("position", "technique"))
				.setPostFilter(QueryBuilders.rangeQuery("age").from(30).to(40))
				.setFrom(0).setSize(1)
				.get();
		
		SearchHit[] searchHits = response.getHits().getHits();
		for(int i = 0; i < searchHits.length; i++) {
			System.out.println(searchHits[i].getSourceAsString()); 
		}
	}
	
	/*public void aggr() {
		
		SearchResponse searchResponse = client.prepareSearch("company") 
				.addAggregation(AggregationBuilders.terms("group_by_country").field("country")
						.subAggregation(AggregationBuilders
								.dateHistogram("group_by_join_date")
								.field("join_date")
								.dateHistogramInterval(DateHistogramInterval.YEAR)
								.subAggregation(AggregationBuilders.avg("avg_salary").field("salary")))
				)
				.execute().actionGet();
		
		Map<String, Aggregation> aggrMap = searchResponse.getAggregations().asMap();
		
		StringTerms groupByCountry = (StringTerms) aggrMap.get("group_by_country");
		Iterator<Bucket> groupByCountryBucketIterator = groupByCountry.getBuckets().iterator();
		while(groupByCountryBucketIterator.hasNext()) {
			Bucket groupByCountryBucket = groupByCountryBucketIterator.next();
			System.out.println(groupByCountryBucket.getKey() + ":" + groupByCountryBucket.getDocCount()); 
		
			Histogram groupByJoinDate = (Histogram) groupByCountryBucket.getAggregations().asMap().get("group_by_join_date");
			@SuppressWarnings("unchecked")
			Iterator<Bucket> groupByJoinDateBucketIterator = (Iterator<Bucket>) groupByJoinDate.getBuckets().iterator();
			while(groupByJoinDateBucketIterator.hasNext()) {
				org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket groupByJoinDateBucket = (org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket) groupByJoinDateBucketIterator.next();
				System.out.println(groupByJoinDateBucket.getKey() + ":" +groupByJoinDateBucket.getDocCount()); 
			
				Avg avg = (Avg) groupByJoinDateBucket.getAggregations().asMap().get("avg_salary"); 
				System.out.println(avg.getValue()); 
			}
		}
		
	}*/

}
