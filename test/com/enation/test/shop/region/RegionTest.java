package com.enation.test.shop.region;

import java.util.List;

import org.apache.poi.hssf.record.formula.functions.T;
import org.junit.Before;
import org.junit.Test;

import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.framework.test.SpringTestSupport;
import com.enation.javashop.core.service.IRegionsManager;

/**
 * 区域测试
 * @author kingapex
 *2010-3-26下午05:15:31
 */ 
public class RegionTest extends SpringTestSupport{
	private IRegionsManager regionsManager;
	
	@Before
	public void mock(){
		regionsManager = this.getBean("regionsManager");
        EopSite site = new EopSite();
        site.setUserid(2);
        site.setId(2);
		EopContext context  = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);		
	}
	
	@Test
	public void testGetChildrenJson(){
		String json =regionsManager.getChildrenJson(2);
		System.out.println(json);
	}
	
	@Test
	public void testListChildrenByIdstr(){
		List<Integer > list  =regionsManager.listChildren("2,21");
	 
		for(Integer id:list){
			System.out.println(id);
		}
	}
}
