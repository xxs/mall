package com.enation.test.widget;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.eop.sdk.widget.IWidget;
import com.enation.framework.test.SpringTestSupport;

public class WidgetTest extends SpringTestSupport {
	
	@Before
	public void mock(){
		 UserServiceFactory.isTest=1;
		 
        EopSite site = new EopSite();
        site.setUserid(2);
        site.setId(2);
		EopContext context  = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);		
	}
	
	@Test
	public void goodsWidgetTest() throws ServletException, IOException{
 
	    
		Map<String,String>   params =new HashMap<String,String>();
		params.put("tag_id", "1");		
		
		IWidget widget= (IWidget)this.getBean("goods_list");
		String content = widget.process(params);
		System.out.println(content);
	  

	}
	
	
	
	@Test
	public void catWidgetTest() throws ServletException, IOException{
 
	    
		Map<String,String>   params =new HashMap<String,String>();
		params.put("tag_id", "1");		
		
		IWidget widget= (IWidget)this.getBean("goods_cat");
		String content = widget.process(params);
		System.out.println(content);
	  

	}
		
	
	
	
}
