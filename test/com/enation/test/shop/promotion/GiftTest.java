package com.enation.test.shop.promotion;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.framework.test.SpringTestSupport;
import com.enation.javashop.core.service.IFreeOfferManager;

/**
 * 赠品测试
 * @author kingapex
 *2010-4-23下午04:01:21
 */
public class GiftTest extends SpringTestSupport {
	private IFreeOfferManager freeOfferManager;
	
	@Before
	public void mock(){
		freeOfferManager = this.getBean("freeOfferManager");
        EopSite site = new EopSite();
        site.setUserid(2);
        site.setId(2);
		EopContext context  = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);		

	}
	
	/**
	 * 测试根据数组读取赠品列表
	 */
	@Test
	public void testListByIds(){
		List<Map> list  = freeOfferManager.list(new Integer[] {1,2});
		for(Map gift :list ){
			System.out.println(gift.get("fo_name"));
		}
	}
	
	
}
