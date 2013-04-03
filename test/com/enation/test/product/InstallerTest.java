package com.enation.test.product;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.enation.app.base.core.service.solution.IInstaller;
import com.enation.app.base.core.service.solution.IProductService;
import com.enation.app.base.core.service.solution.IProfileLoader;
import com.enation.eop.resource.IUserManager;
import com.enation.eop.resource.dto.SiteDTO;
import com.enation.eop.resource.dto.UserDTO;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.resource.model.EopSiteAdmin;
import com.enation.eop.resource.model.EopSiteDomain;
import com.enation.eop.resource.model.EopUser;
import com.enation.eop.resource.model.EopUserDetail;
import com.enation.eop.sdk.context.EopContext;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.user.IUserService;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.test.SpringTestSupport;
import com.enation.framework.util.StringUtil;

/**
 * 为用户安装product的测试
 * @author kingapex
 * 2010-1-20下午10:53:54
 */
//@RunWith(JMock.class)
public class InstallerTest extends SpringTestSupport {
	
	//private  Mockery context = new JUnit4Mockery();
	 
 
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
	
	
	/**
	 * 测试为一个为已经登陆用户安装一个product
	 */
	@Test
	public void installTestForLogined(){
/*		
		//清空
		this.cleanAdminTheme();
		this.cleanMenu();
		this.cleanTheme();
		this.cleanUri();
		this.cleanWidget();
		IUserService userService = UserServiceFactory.getUserService();
		String sql ="truncate table base_menu_"+ userService.getCurrentUserId() ;
		this.jdbcTemplate.execute(sql);	


UserDTO userDTO = new UserDTO();
		
		EopUser eopUser = new EopUser();
		eopUser.setAddress("北京市昌平区");
		eopUser.setUsername("易族智汇（北京）科技有限公司");
		eopUser.setLinkman("刘志毅");
		eopUser.setTel("01061750491");
		eopUser.setEmail("enation@126.com");
		userDTO.setUser(eopUser);
		
		EopUserDetail eopUserDetail = new EopUserDetail();
		eopUserDetail.setBussinessscope("软件制作、维护服务");
		userDTO.setUserDetail(eopUserDetail);
		
		AdminUser userAdmin = new AdminUser();
		userAdmin.setUsername("admin");
		userAdmin.setPassword(StringUtil.md5("admin"));
		userDTO.setUserAdmin(userAdmin);
		
		///////////////
		SiteDTO siteDTO = new SiteDTO();
		EopSite site = new EopSite();
		site.setThemeid(1);
		site.setThemepath("zqzy");
		site.setAdminthemeid(1);
		site.setSitename("测试站点");
		siteDTO.setSite(site);
		
		EopSiteDomain domain = new EopSiteDomain();
		domain.setDomain("kingapex.eop.com");
		siteDTO.setDomain(domain);
		
		EopSiteAdmin siteAdmin = new EopSiteAdmin();
		siteDTO.setSiteAdmin(siteAdmin);
		
		///////////////////////////////////////////////////
		
		userDTO.setSiteDTO(siteDTO);
		
		IUserManager userManager = getBean("userManager");
		Integer userid = userManager.createUser(userDTO);

		
		
		//为用户安装一个书店product
		IProductService productService = this.getBean("productService");
		productService.install(userid, userDTO.getSiteid(), "base");
 		productService.install(userid, userDTO.getSiteid(),"bookstore");
 		
 		
 		//验证断言
 		this.adminThemeInstallAssert();
 		this.menuInstallAssert();
 		this.uriInstallAssert();
 		this.themeInstallAssert();
 		this.widgetInstallAssert();*/
	}	
	
	
	/**
	 * 加载一个产品配置文件测试
	 */
	//@Test
	public void loadProfileTest(){
		
		UserServiceFactory.isTest=1;
		final String productName ="clothingstore"; //以服装网店做为测试对象
		
		IProfileLoader loader = this.getBean("profileLoader");
		Document profileDoc = loader.load(productName);
		
		//document的子为不null
		NodeList nodeList = profileDoc.getChildNodes();
		assertEquals(nodeList!=null,true);
		
		//只有一个子结点
		int length = nodeList.getLength();
		assertEquals(1,length);
		
		//这个子结点的名字为product
		Node firstNode =nodeList.item(0);
		String firstNodeName = firstNode.getNodeName();
		assertEquals(firstNodeName,"product");
		
		nodeList = profileDoc.getElementsByTagName("urls");
		Node urlNode =nodeList.item(0);
		
		assertEquals(urlNode!=null,true);
		nodeList  = urlNode.getChildNodes();
		assertEquals(urlNode.getChildNodes().getLength(),2);
		
	}

	// --- --- --- --- 清理相关 --- --- --- --- --- //
	private void cleanMenu(){
		IUserService userService = UserServiceFactory.getUserService();
		String sql ="truncate table base_menu_"+ userService.getCurrentUserId() ;
		this.jdbcTemplate.execute(sql);	
	}

	private void cleanWidget(){
		IUserService userService = UserServiceFactory.getUserService();
		String sql ="truncate table base_widgetbundle_"+ userService.getCurrentUserId() ;
		this.jdbcTemplate.execute(sql);	
	}
	
	
	private void cleanUri(){
		IUserService userService = UserServiceFactory.getUserService();
		String sql ="truncate table base_themeuri_"+ userService.getCurrentUserId() ;
		this.jdbcTemplate.execute(sql);	
	}
		
	
	private void cleanAdminTheme(){
		//清除主题相关数据
		IUserService userService = UserServiceFactory.getUserService();
		String sql ="truncate table base_admintheme_"+ userService.getCurrentUserId() ;
		this.jdbcTemplate.execute(sql);	
		
		//删除用户的主题文件
		File file = new File(getDedePath());
		if(file.exists() )  file.delete();
	}
	
	private void cleanTheme(){
		//清除主题相关数据
		IUserService userService = UserServiceFactory.getUserService();
		String sql ="truncate table base_theme_"+ userService.getCurrentUserId() ;
		this.jdbcTemplate.execute(sql);	
		
		//删除用户的主题文件
		File file = new File( this.getThemePath());
		if(file.exists() )  file.delete();
	}
	
	
	/**
	 * 测试uri安装
	 */
	@Test
	public void installUriTest(){
		
		cleanUri();
		
		IProfileLoader loader = this.getBean("profileLoader");
		Document proFileDoc = loader.load("bookstore");
		
		IInstaller adminThemeInstaller =  this.getBean("uriInstaller");
		adminThemeInstaller.install("bookstore",proFileDoc.getElementsByTagName("urls").item(0));
	 				
		this.uriInstallAssert();
		
	}

	/**
	 * 测试主题安装
	 */
	@Test
	public void installThemeTest(){
		
		this.cleanTheme();
		
		IProfileLoader loader = this.getBean("profileLoader");
		Document proFileDoc = loader.load("bookstore");
		
		IInstaller themeInstaller =  this.getBean("themeInstaller");
		themeInstaller.install("bookstore",proFileDoc.getElementsByTagName("themes").item(0));
	 				
		this.themeInstallAssert();
		
	}
	
	
	/**
	 * 测试后台主题安装
	 */
	@Test
	public void installAdminThemeTest(){
		 
		this.cleanAdminTheme();
		
		IProfileLoader loader = this.getBean("profileLoader");
		Document proFileDoc = loader.load("base");
		
		IInstaller adminThemeInstaller =  this.getBean("adminThemeInstaller");
		adminThemeInstaller.install("base",proFileDoc.getElementsByTagName("adminThemes").item(0));
	 		
		adminThemeInstallAssert();
	}
	
	

	/**
	 * 测试安装为一个用户 安装后台菜单
	 */
	@Test
	public void installMenuTest(){
		//清理
		this.cleanMenu();
		
		IProfileLoader loader = this.getBean("profileLoader");
		Document proFileDoc = loader.load("bookstore");
	 
		//安装菜单
		IInstaller menuInstaller =  this.getBean("menuInstaller");
		menuInstaller.install("bookstore",proFileDoc.getElementsByTagName("menu").item(0));
		
		//验证断言
		menuInstallAssert();
	}
	
	
	/**
	 * 测试安装为一个用户 安装挂件
	 */
	@Test
	public void installWidgetTest(){
		//清理
		this.cleanWidget();
		
		IProfileLoader loader = this.getBean("profileLoader");
		Document proFileDoc = loader.load("bookstore");
	 
		//安装挂件
		IInstaller menuInstaller =  this.getBean("widgetInstaller");
		menuInstaller.install("bookstore",proFileDoc.getElementsByTagName("widgets").item(0));
		
		//验证断言
		widgetInstallAssert();
	}
	
	
	
	
	
	
	
	
	// --- --- --- --- 断言相关 --- --- --- --- --- //
	
	/**
	 * 测试产品是否安装成功的断言
	 */
	private void productInstallAssert(){
		menuInstallAssert();
	}	
	
	
	
	/**
	 * 测试菜单是否安装成功的断言
	 * 查询数据库是否有记录
	 */
	private void menuInstallAssert(){
		IUserService userService = UserServiceFactory.getUserService();
		String sql  ="select count(0) from base_menu_"+ userService.getCurrentUserId() +" where siteid=?";
		int count = this.simpleJdbcTemplate.queryForInt(sql,userService.getCurrentSiteId());
		assertEquals(true,count>0);
	}
	
	/**
	 * 测试后台主题是否安装成功的断言
	 * 
	 */
	private void adminThemeInstallAssert(){
		
		
		//查询数据库是否有记录
		IUserService userService = UserServiceFactory.getUserService();
		String sql  ="select count(0) from base_adminTheme_"+ userService.getCurrentUserId() +" where siteid=?";
		int count = this.simpleJdbcTemplate.queryForInt(sql,userService.getCurrentSiteId());
		assertEquals(true,count>0);
		
		//检测文件是否拷贝正常
		File file  = new File(  getDedePath() );
		assertEquals(true,file.exists());
		
		file  = new File( 
				EopSetting.IMG_SERVER_PATH +"/user/"
				+userService.getCurrentUserId()
				+"/"
				+userService.getCurrentSiteId()
				+
				EopSetting.ADMINTHEMES_STORAGE_PATH
				+"/dede"
				);
		assertEquals(true,file.exists());
		
		
	}
	

	private void themeInstallAssert(){
		
		
		//查询数据库是否有记录
		IUserService userService = UserServiceFactory.getUserService();
		String sql  ="select count(0) from base_theme_"+ userService.getCurrentUserId() +" where siteid=?";
		int count = this.simpleJdbcTemplate.queryForInt(sql,userService.getCurrentSiteId());
		assertEquals(true,count>0);
		
		//检测文件是否拷贝正常
		File file  = new File( this.getThemePath());
		assertEquals(true,file.exists());
		
	}	
	
	
	/**
	 * uri安装的断言 
	 */
	private void uriInstallAssert(){
		IUserService userService = UserServiceFactory.getUserService();
		String sql  ="select count(0) from base_themeuri_"+ userService.getCurrentUserId() +" where siteid=?";
		int count = this.simpleJdbcTemplate.queryForInt(sql,userService.getCurrentSiteId());
		assertEquals(true,count>0);
	}

	/**
	 * 挂件安装的断言
	 */
	private void widgetInstallAssert(){
		IUserService userService = UserServiceFactory.getUserService();
		String sql  ="select count(0) from base_widgetbundle_"+ userService.getCurrentUserId() +" where siteid=?";
		int count = this.simpleJdbcTemplate.queryForInt(sql,userService.getCurrentSiteId());
		assertEquals(true,count>0);
	}
	
	
	
	/**
	 * 拼装 dede后台模板的路径
	 * @return
	 */
	private String getDedePath(){
		IUserService userService = UserServiceFactory.getUserService();
	return 	EopSetting.EOP_PATH +"/user/"
		+userService.getCurrentUserId()
		+"/"
		+userService.getCurrentSiteId()
		+
		EopSetting.ADMINTHEMES_STORAGE_PATH
		+"/dede"	;	
	}
	
	/**
	 * 获取默认主题路径
	 * @return
	 */
	private String getThemePath(){
		IUserService userService = UserServiceFactory.getUserService();
		return 	EopSetting.EOP_PATH +"/user/"
			+userService.getCurrentUserId()
			+"/"
			+userService.getCurrentSiteId()
			+
			EopSetting.THEMES_STORAGE_PATH
			+"/default"	;	
	}
	
	
	
	
}
