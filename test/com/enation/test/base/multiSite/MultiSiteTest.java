package com.enation.test.base.multiSite;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.junit.Before;
import org.junit.Test;

import com.enation.app.base.core.model.MultiSite;
import com.enation.app.base.core.service.IMultiSiteManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.resource.model.Theme;
import com.enation.eop.sdk.context.EopContext;
import com.enation.framework.test.SpringTestSupport;
import com.enation.framework.util.FileUtil;

public class MultiSiteTest extends SpringTestSupport {
	
	private IMultiSiteManager multiSiteManager;
	
	@Before
	public void mock(){
		multiSiteManager=this.getBean("multiSiteManager");
		EopSite site = new EopSite();
		site.setUserid(2);
		site.setId(2);
		EopContext context = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);
	}
	
	
	@Test
	public void testAdd(){
//		MultiSite site = new MultiSite();
//		site.setName("朝阳站");
//		site.setParentid(1);
//		site.setDomain("chaoyan.enationsoft.com");
//		site.setCode(2);
//		this.multiSiteManager.add(site);
		MultiSite site = new MultiSite();
		site.setSiteid(1);
		site.setDomain("chaoyan.enationsoft.com");
		site.setName("朝阳站");
		site.setParentid(0);
		site.setThemeid(1);
		this.multiSiteManager.add(site);
	}
	
	@Test
	public void testDelete(){
		this.multiSiteManager.delete(1);
	}
	
	@Test
	public void testList(){
		List<Map> list = this.multiSiteManager.list();
		JSONArray jsonArray1 = JSONArray.fromObject( list );       
		System.out.println( jsonArray1 );
	}
	
}
