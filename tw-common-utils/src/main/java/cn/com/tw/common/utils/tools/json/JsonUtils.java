package cn.com.tw.common.utils.tools.json;

import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json转换
 * @author admin
 *
 */
public class JsonUtils {

    //定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 对象转json
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
    	if(data == null){
    		return null;
    	}
    	
    	try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
   /**
    * json转对象
    * @param jsonData
    * @param beanType
    * @return
    */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
    	if(StringUtils.isEmpty(jsonData)){
    		return null;
    	}
    	
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * json转list
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	
    	if(StringUtils.isEmpty(jsonData)){
    		return null;
    	}
    	
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = MAPPER.readValue(jsonData, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
}
