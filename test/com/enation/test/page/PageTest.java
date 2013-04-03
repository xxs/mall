package com.enation.test.page;

import org.junit.Before;
import org.junit.Test;

import com.enation.eop.processor.IPageParamJsonGetter;
import com.enation.eop.processor.IPagePaser;
import com.enation.eop.processor.IPageUpdater;
import com.enation.eop.processor.facade.support.PageEditModeWrapper;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.test.SpringTestSupport;

/**
 * 前台页面相关测试类
 * @author kingapex
 * 2010-2-4下午04:01:59
 */
public class PageTest extends SpringTestSupport {
	
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
	
	
	/**
	 * 挂件解析测试
	 */
	@Test
	public void pageParserTest(){
		  String url ="/index.html";
		  IPagePaser pagePaser = this.getBean("facadePagePaser");
		  String content  = pagePaser.pase(url);
		  System.out.println(content);
		  
	}
	
	/**
	 * 编辑模式解析测试
	 */
	@Test
	public void editModeWrapperTest(){
		 String url ="/index.html?mode=yes";
		  IPagePaser pagePaser = this.getBean("facadePagePaser");
		  pagePaser = new PageEditModeWrapper(pagePaser);
		  String content  = pagePaser.pase(url);
		  System.out.println(content);
	}
	
	/**
	 * 页面更新测试
	 */
	@Test
	public void updateTest(){
		String json="["
			+"{'id':'1','type':'goods_list','tag_id':'1','border':'border1'},"
			+"{'id':'2','type':'goods_detail','border':'none'}"
			+"]";
		
		 String url ="/goods-1.html"; //or /goods-1.html?mode=yes
		 IPageUpdater pageUpdater = this.getBean("facadePageUpdater");
		 pageUpdater.update(url, "t$est",json);
		 
		 this.pageParserTest();
	}
	
	
	/**
	 * 页面挂件参数json格式获取测试
	 */
	@Test
	public void paramJsonTest(){
		String url ="/goods-1.html";
		 IPageParamJsonGetter pageParamJsonGetter = getBean("pageParamJsonGetter");
		 String json = pageParamJsonGetter.getJson(url);
		 System.out.println(json);
	}
}
