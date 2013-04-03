package com.enation.test.page;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.enation.eop.processor.widget.IWidgetCfgHtmlParser;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.test.SpringTestSupport;

/**
 * 挂件设置页面解析测试
 * @author kingapex
 * 2010-2-12下午09:33:06
 */
public class WidgetConfigTest extends SpringTestSupport{
	
	@Before
	public void mock(){
		 UserServiceFactory.isTest=1;
		 
        EopSite site = new EopSite();
        site.setUserid(2);
        site.setId(2);
        site.setThemeid(1);
		EopContext context  = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);		
	}
	
	@Test
	public void parseTest(){
		Map<String,String> params = new HashMap<String, String>();
		
		params.put("id", "1");
		params.put("type", "goods_list");
		params.put("tag_id", "1");
		
		IWidgetCfgHtmlParser widgetCfgParser = this.getBean("localWidgetCfgPaser");  
		String content = widgetCfgParser.pase( params);
		System.out.println(content);
	}
	
}
