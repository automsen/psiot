package cn.com.tw.gateway.filter;

import org.springframework.stereotype.Component;

import cn.com.tw.gateway.enm.ReqEum;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PostFilter extends ZuulFilter{

	@Override
	public Object run() {
		
		RequestContext context = RequestContext.getCurrentContext();
		
		//清空
		clearCtx(context);
		
		/*RequestContext context = RequestContext.getCurrentContext();

        InputStream stream = context.getResponseDataStream();
        try {
			String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
			context.setResponseBody(body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		return "post";
	}

	private void clearCtx(RequestContext ctx){
		ctx.remove(ReqEum.reqUrl.name());
		ctx.remove(ReqEum.reqMethod.name());
		ctx.remove(ReqEum.reqContentType.name());
		ctx.remove(ReqEum.reqContent.name());
	}
}
