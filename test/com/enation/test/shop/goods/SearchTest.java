package com.enation.test.shop.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.framework.database.Page;
import com.enation.framework.test.SpringTestSupport;
import com.enation.javashop.core.model.support.GoodsView;
import com.enation.javashop.core.service.IGoodsSearchManager;

public class SearchTest extends SpringTestSupport {
	
	private IGoodsSearchManager goodsSearchManager;
	
	@Before
	public void mock(){
		
		goodsSearchManager = this.getBean("goodsSearchManager");
		
        EopSite site = new EopSite();
        site.setUserid(2);
        site.setId(2);
		EopContext context  = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);		
		
	}
	
	
	@Test
	public void testSearch(){
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("cat_path", "0|1");
//		params.put("propStr", "0_4");
		//params.put("keyword", "百搭裙子");
//		params.put("", "");
//		params.put("", "");
		
		Page page  = goodsSearchManager.search(1, 100, params);
		List list = (List)page.getResult();
		for(int i=0;i<list.size();i++){
			GoodsView goods= (GoodsView) list.get(i);
			System.out.println(goods.getName());
		}
	}
	
	/**
	 * 		String cat_path = params.get("cat_path");
		String order = params.get("order");
		String brandStr = params.get("brandStr");
		String propStr = params.get("propStr");
		String keyword = params.get("keyword");
		String minPrice = params.get("minPrice");
		String maxPrice = params.get("maxPrice");
	 */
	
	
}
