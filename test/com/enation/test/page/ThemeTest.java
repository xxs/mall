package com.enation.test.page;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.enation.eop.resource.IThemeManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.resource.model.Theme;
import com.enation.eop.sdk.context.EopContext;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.test.SpringTestSupport;

public class ThemeTest extends SpringTestSupport {
	
	
	
	@Before
	public void mock(){
		 UserServiceFactory.isTest=1;
		 
        EopSite site = new EopSite();
        site.setUserid(2);
        site.setId(2);
		EopContext context  = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);		
	}
	
	@Test
	public void themeGetTest(){
		
		IThemeManager themeManager  = this.getBean("themeManager");
		Theme theme = themeManager.getTheme( 1);
		assertEquals(theme.getPath(),"default");
	}
	
	
}
