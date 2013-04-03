package com.enation.test.shop.goods;

import org.junit.Before;

import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.framework.test.SpringTestSupport;
import com.enation.javashop.core.service.IGoodsCatManager;

public class GoodsCatTest extends SpringTestSupport {
	
	private IGoodsCatManager goodsCatManager;
	
	@Before
	public void mock(){
		
		goodsCatManager = this.getBean("goodsCatManager");
		
        EopSite site = new EopSite();
        site.setUserid(2);
        site.setId(2);
		EopContext context  = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);		
		
	}

}
