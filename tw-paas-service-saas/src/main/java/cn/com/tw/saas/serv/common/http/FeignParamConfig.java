package cn.com.tw.saas.serv.common.http;

import feign.RequestInterceptor;
import feign.RequestTemplate;

//@Configuration
public class FeignParamConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //requestTemplate.header("token", getHeaders(getHttpServletRequest()).get("token"));
    }

   /* private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }*/
}