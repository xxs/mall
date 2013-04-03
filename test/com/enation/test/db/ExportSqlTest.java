package com.enation.test.db;

import org.junit.Before;
import org.junit.Test;

import com.enation.app.base.core.service.solution.IProductService;
import com.enation.app.base.core.service.solution.impl.ProfileCreator;
import com.enation.app.base.core.service.solution.impl.SqlExportService;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.framework.test.SpringTestSupport;


public class ExportSqlTest extends SpringTestSupport {
	@Before
	public void mock() {
		EopSite site = new EopSite();
		site.setUserid(1);
		site.setId(1);
		site.setThemeid(1);
		EopContext context = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);
	}
	

	@Test
	public void testExportSql(){
		SqlExportService sqlExportService = this.getBean("sqlExportService");
		String sql  =sqlExportService.dumpSql();
		System.out.println(sql);
		
	}
	
	//@Test
	public void tesetCreateProfile(){
		ProfileCreator profileCreator = this.getBean("profileCreator");
		profileCreator.createProfile("d:/a.xml");
	}
}
