package com.enation.test.db;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.enation.app.base.core.service.IDataSourceCreator;
import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.app.base.core.service.dbsolution.IDBSolution;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.test.SpringTestSupport;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBSolutionTest extends SpringTestSupport {
	protected List<String> tables;
	protected IDaoSupport baseDaoSupport;
	
	@Before
	public void prepareData() {
		//this.prepareShopData();
		this.prepareBaseData();
	}
	 
	
	public void prepareEopData() {
 
		
		tables = new ArrayList<String>();
		tables.add("eop_component");
		
	}
	
	public void prepareBaseData() {
		//tables = new String[3];
//		tables[0] = "eop_app";
//		tables[1] = "eop_site";
//		tables[2] = "eop_user";
		
		tables = new ArrayList<String>();
		tables.add("es_adv");
		tables.add("es_adv");
		tables.add("es_adcolumn");
		tables.add("es_admintheme"); 
		tables.add("es_friends_link");
		tables.add("es_settings");
		tables.add("es_site_menu");
		tables.add("es_guestbook");
		tables.add("es_access");
		tables.add("es_menu");
		tables.add("es_role");
		tables.add("es_adminuser");		
		tables.add("es_role_auth");
		tables.add("es_user_role");
		tables.add("es_auth_action");
		tables.add("es_theme");
		tables.add("es_themeuri");
		tables.add("es_index_item");		
		tables.add("es_smtp");
		
	}
	
	
	private void prepareShopData(){
		
		tables = new ArrayList<String>();
		tables.add("es_goods");
		tables.add("es_goods_spec");
		tables.add("es_product");//lzf add
		
		//商品类别表
		tables.add("es_goods_cat");
		
		//创建品牌表
		tables.add("es_brand");
		
		//创建类型相关表
		tables.add("es_goods_type");
		tables.add("es_type_brand"); //类型品牌关联表
		
		//赠品,lzf add
		tables.add("es_freeoffer");
		tables.add("es_freeoffer_category");
		
		//商品标签
		tables.add("es_tags");
		tables.add("es_tag_rel");
		
		//会员
		tables.add("es_member");
		tables.add("es_member_lv");
		tables.add("es_goods_lv_price");
		
		//代理商
		tables.add("es_agent");
		tables.add("es_agent_transfer");
		
		//配送方式标签
		tables.add("es_dly_type");
		tables.add("es_dly_area");
		tables.add("es_dly_type_area");
		
		//物流公司
		tables.add("es_logi_company");
		
		//商品评论
		tables.add("es_comments");

		//规格相关表
		tables.add("es_specification");
		tables.add("es_spec_values");
		
		//订单相关
		tables.add("es_cart");
		tables.add("es_order");
		tables.add("es_order_items");
		tables.add("es_order_log");
		
		tables.add("es_delivery");
		tables.add("es_delivery_item");
		tables.add("es_payment_cfg");
		tables.add("es_payment_logs");
		tables.add("es_regions");
		tables.add("es_member_address");
		tables.add("es_message");
		tables.add("es_order_gift");
		//营销推广相关
		tables.add("es_gnotify");
		tables.add("es_point_history");
		tables.add("es_coupons");
		tables.add("es_promotion");
		tables.add("es_member_coupon");
		tables.add("es_pmt_member_lv");
		tables.add("es_pmt_goods");
		tables.add("es_favorite");
		tables.add("es_advance_logs");
		tables.add("es_promotion_activity");
		tables.add("es_goods_complex");
		tables.add("es_goods_adjunct");
		tables.add("es_goods_articles");
		tables.add("es_goods_field");
		tables.add("es_group_buy_count");
		tables.add("es_limitbuy");
		tables.add("es_limitbuy_goods");
		tables.add("es_article");
		tables.add("es_article_cat");
		tables.add("es_package_product");
		tables.add("es_dly_center");
		tables.add("es_print_tmpl");
		tables.add("es_order_pmt");
		tables.add("es_group_buy");
		
		tables.add("es_member_comment");
		tables.add("es_warn_num	");
		tables.add("es_freeze_point");
		tables.add("es_member_lv_discount");
		tables.add("es_order_pay");
		tables.add("es_order_meta");
		tables.add("es_coupons");
		tables.add("es_member_coupon");
		tables.add("es_member_order_item");
		tables.add("es_store_log");
		tables.add("es_depot_user");
		tables.add("es_product_store");
		tables.add("es_depot");
		tables.add("es_goods_depot");
		tables.add("es_allocation_item");
		tables.add("es_returns_order");
		tables.add("es_invoice");
		
		
	}

	protected void changeToOracle() {
		ComboPooledDataSource dataSource = (ComboPooledDataSource) jdbcTemplate
				.getDataSource();
		try {
			dataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
			dataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:XE");
			dataSource.setUser("javashop");
			dataSource.setPassword("752513");
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void changeToMySQL() {
		IDataSourceCreator dataSourceCreator = this
				.getBean("dataSourceCreator");
		jdbcTemplate
				.setDataSource(dataSourceCreator
						.createDataSource(
								"com.mysql.jdbc.Driver",
								"jdbc\\:mysql\\://127.0.0.1\\:3306/javashop?useUnicode\\=true&characterEncoding\\=utf8",
								"root", "752513"));
	}

	protected void testExport() {
		IDBSolution dbsolution = DBSolutionFactory.getDBSolution();
		
	  
		String[] temp  =  tables.toArray(new String[]{});
		dbsolution.dbExport(temp, "D:\\dbsolution.xml");
	}

	protected void testImport() {
		IDBSolution dbsolution = DBSolutionFactory.getDBSolution();
		dbsolution.setPrefix("es_");
		dbsolution.dbImport("file:com/enation/app/base/base.xml");
	}
	
	@Test
	public void testDBSolution() {
		testExport();
	}
}
