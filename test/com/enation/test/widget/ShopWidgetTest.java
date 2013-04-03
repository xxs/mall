package com.enation.test.widget;

import org.junit.Before;
import org.junit.Test;

import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.eop.sdk.widget.IWidget;
import com.enation.framework.test.SpringTestSupport;
import com.enation.javashop.core.plugin.goods.IGoodsBaseShowEvent;

/**
 * 网店挂件测试
 * @author kingapex
 * 2010-2-17上午10:30:16
 */
 
public class ShopWidgetTest extends SpringTestSupport{
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
	public void goodsDetailTest(){
		
		IWidget detailWidget  = this.getBean("goods_detail");
		
		System.out.println(detailWidget.process(null));
	}
	
	
	/**
	 * 测试基本信息显示插件解析
	 */
	@Test
	public void basePluginTest(){
		IGoodsBaseShowEvent baseShowEvent = this.getBean("shopexGoodsBase");
		String html = baseShowEvent.onBaseShow(null);
		System.out.println(html);
	}
	
	
 
	
	@Test
	public void memeberIndexTest(){
		IWidget detailWidget  = this.getBean("member_order");
		System.out.println(detailWidget.process(null));
	}
	
}
