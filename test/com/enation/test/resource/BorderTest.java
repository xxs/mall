package com.enation.test.resource;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.enation.eop.resource.IBorderManager;
import com.enation.eop.resource.model.Border;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.framework.test.SpringTestSupport;

public class BorderTest extends SpringTestSupport {
	@Before
	public void mock(){
        EopSite site = new EopSite();
        site.setUserid(2);
        site.setId(2);
		EopContext context  = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);		
	}
	
	
	@Test
	public void addTest(){
		Border border = new Border();
		border.setBorderid("border1");
		border.setBordername("商品列表");
		
		IBorderManager borderManager = this.getBean("borderManager");
		borderManager.add(border);
		
	}
	
	@Test
	public void listTest(){
		IBorderManager borderManager = this.getBean("borderManager");
		List<Border> list = borderManager.list();
		
		for(Border border:list){
			System.out.println(border.getBordername());
		}
		assertEquals(true,list!=null && !list.isEmpty() && list.size()>0);
	}
	
	
}
