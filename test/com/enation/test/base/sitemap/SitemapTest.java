package com.enation.test.base.sitemap;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.enation.app.base.core.model.SiteMapUrl;
import com.enation.app.base.core.service.ISitemapManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.eop.sdk.utils.DateUtil;
import com.enation.framework.test.SpringTestSupport;

public class SitemapTest extends SpringTestSupport {
	
	private ISitemapManager sitemapManager;
	
	@Before
	public void mock(){
		sitemapManager=this.getBean("sitemapManager");
		EopSite site = new EopSite();
		site.setUserid(2);
		site.setId(2);
		EopContext context = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);
	}
	
	
	@Test
	public void testAdd(){
		SiteMapUrl url = new SiteMapUrl();
		url.setLoc("http://www.enationsoft.com/index.html");
		url.setLastmod(DateUtil.toDate("2010-09-01", "yyyy-MM-dd").getTime());
		url.setChangefreq("daily");
		url.setPriority("0.8");
		this.sitemapManager.addUrl(url);
		System.out.println(this.sitemapManager.getsitemap());
	}
	
	@Test
	public void testEdit(){
		this.testAdd();
		this.sitemapManager.editUrl("http://www.enationsoft.com/index.html", System.currentTimeMillis());
		System.out.println(this.sitemapManager.getsitemap());
	}

}
