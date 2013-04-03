package com.enation.test.shop.promotion;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.framework.database.IDBRouter;
import com.enation.framework.database.Page;
import com.enation.framework.test.SpringTestSupport;
import com.enation.javashop.core.model.Coupons;
import com.enation.javashop.core.service.ICouponManager;


/**
 * 优惠卷测试
 * @author kingapex
 *2010-4-20下午05:30:04
 */
public class CouponTest extends SpringTestSupport {
	private ICouponManager couponManager;
	protected IDBRouter shopSaasDBRouter;
	@Before
	public void mock(){
		couponManager = this.getBean("couponManager");
		shopSaasDBRouter = this.getBean("shopSaasDBRouter");
        EopSite site = new EopSite();
        site.setUserid(2);
        site.setId(2);
		EopContext context  = new EopContext();
		context.setCurrentSite(site);
		EopContext.setContext(context);		

	}
	
	
	/**
	 * 清除数据并建立表结构
	 */
	private void clean(){

		this.jdbcTemplate.execute("drop table if exists js_coupons_2");
		shopSaasDBRouter.createTable( "coupons");

	}
	
	
	/**
	 * 测试添加
	 */
	@Test
	public void testAdd(){
		
		this.clean();
		Coupons coupons = new Coupons();
		coupons.setCpns_name("满500立减50");
		coupons.setCpns_prefix("A0000001");
		coupons.setPmt_id(1); //促销规则id为1
		coupons.setDisabled("true");
		this.couponManager.add(coupons);
		
		//断言
		Coupons couponDb = couponManager.get(1);
		Assert.assertEquals(couponDb.getCpns_type(), 0);
		Assert.assertEquals(couponDb.getCpns_status(), 0);
		Assert.assertEquals(couponDb.getDisabled(),"false");
		
		Assert.assertEquals(couponDb.getCpns_prefix(), "A0000001");
		
		//失败性测试
		 coupons = new Coupons();
		 try{
			 this.couponManager.add(coupons);
			 Assert.fail("参数不正确不能执行");
		 }catch(IllegalArgumentException e){
			 
		 }
		
	}
	
	
	/**
	 * 测试修改
	 */
	@Test
	public void testEdit(){
		this.testAdd(); //模拟数据
		
		Coupons coupons = new Coupons();
		coupons.setCpns_id(1);
		coupons.setCpns_name("满400立减50");
		coupons.setCpns_prefix("A0000001");
		coupons.setPmt_id(2); //修改促销规则id为2
		coupons.setDisabled("true");
		coupons.setCpns_type(1);
		coupons.setCpns_status(1);
		this.couponManager.edit(coupons);
		
		//断言
		Coupons couponDb = couponManager.get(1);
		Assert.assertEquals(couponDb.getCpns_type(), 1);
		Assert.assertEquals(couponDb.getCpns_status(), 1);
		Assert.assertEquals(couponDb.getDisabled(),"false");
		
		Assert.assertEquals(couponDb.getCpns_prefix(), "A0000001");
		
		//失败性测试
		 coupons = new Coupons();
		 try{
			 this.couponManager.edit(coupons);
			 Assert.fail("参数不正确不能执行");
		 }catch(IllegalArgumentException e){
			 
		 }
	}
	
	/**
	 * 测试读取
	 */
	@Test
	public void testGet(){
		this.testAdd();
		Coupons couponDb = this.couponManager.get(1);
		Assert.assertEquals(couponDb.getCpns_type(), 0);
		Assert.assertEquals(couponDb.getCpns_status(), 0);
		Assert.assertEquals(couponDb.getDisabled(),"false");
		
		Assert.assertEquals(couponDb.getCpns_prefix(), "A0000001");
	}
	
	/**
	 * 测试删除
	 */
	@Test
	public void testDelete(){
		
		this.testAdd();
		this.couponManager.delete(null,null);
		int count  = this.jdbcTemplate.queryForInt("select count(0) from js_coupons_2");
		Assert.assertEquals(count,1);
		
		this.couponManager.delete(new Integer[]{1}, new Integer[]{1});
		count  = this.jdbcTemplate.queryForInt("select count(0) from js_coupons_2");
		Assert.assertEquals(count,0);
		
		//失败性测试
		try{
			this.couponManager.delete(new Integer[]{1,2}, new Integer[]{1});
			 Assert.fail("参数不正确不能执行");
		}catch(IllegalArgumentException e){
			
		}
	}
	
	/**
	 * 测试添加兑换规则
	 */
	@Test
	public void testAddExchange(){
		this.clean();
		Coupons coupons = new Coupons();
		coupons.setCpns_name("满500立减50");
		coupons.setCpns_prefix("A0000002");
		coupons.setPmt_id(1); //促销规则id为1
		coupons.setCpns_status(1);//可用
		coupons.setCpns_type(1); //B类优惠卷
	 
		this.couponManager.add(coupons);
		
		this.couponManager.saveExchange(1, 500); //500积分可兑换此优惠卷
		
		coupons = this.couponManager.get(1);
		Assert.assertEquals(coupons.getCpns_point(),500);
		
		
		this.couponManager.saveExchange(1, 400); //修改为400可兑换
		coupons = this.couponManager.get(1);
		Assert.assertEquals(coupons.getCpns_point(),400);
		
		//失败性测试
		try{
			this.couponManager.saveExchange(null, 500);
			 Assert.fail("参数不正确不能执行");
		}catch(IllegalArgumentException e){
			
		}
		
		try{
			this.couponManager.saveExchange(1, null);
			 Assert.fail("参数不正确不能执行");
		}catch(IllegalArgumentException e){
			
		}
		
	}
	

	
	 
	
	
	/**
	 * 读取可以设置兑换规则的优惠卷
	 */
	@Test
	public void testListCanExchange(){
		
		this.clean();
		Coupons coupons = new Coupons();
		coupons.setCpns_name("满500立减50");
		coupons.setCpns_prefix("A0000002");
		coupons.setPmt_id(1); //促销规则id为1
		coupons.setCpns_status(1);//可用
		coupons.setCpns_type(1); //B类优惠卷
		this.couponManager.add(coupons);
				
		List list  = this.couponManager.listCanExchange();
		Assert.assertEquals(list.size(),1);
		
		//设置规则后，则无可设置规则的优惠卷了
		this.couponManager.saveExchange(1, 500); //500积分可兑换此优惠卷
		list  = this.couponManager.listCanExchange();
		Assert.assertEquals(list.size(),0);
	}
	
	
	/**
	 * 测试读取优惠卷兑换规则列表
	 */
	@Test
	public void testListExchange(){
		this.testAddExchange(); //添加兑换规则
		Page page  = this.couponManager.listExchange(1, 20); 
		Assert.assertEquals(page.getTotalCount(),1);
	}
	
	
	
}
