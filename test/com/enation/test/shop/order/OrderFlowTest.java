package com.enation.test.shop.order;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.enation.app.base.core.model.Member;
import com.enation.framework.database.ObjectNotFoundException;
import com.enation.javashop.core.model.Delivery;
import com.enation.javashop.core.model.DeliveryItem;
import com.enation.javashop.core.model.Order;
import com.enation.javashop.core.model.OrderItem;
import com.enation.javashop.core.model.PaymentLog;
import com.enation.javashop.core.service.IOrderFlowManager;
import com.enation.javashop.core.service.OrderStatus;
@RunWith(JMock.class)
public class OrderFlowTest  extends OrderTest{
	private IOrderFlowManager orderFlowManager;
	private  Mockery context = new JUnit4Mockery();
	private void cleanFlow(){
		
		this.jdbcTemplate.execute("drop table if exists js_payment_logs_2");
		shopSaasDBRouter.createTable( "payment_logs");

		this.jdbcTemplate.execute("drop table if exists js_delivery_2");
		shopSaasDBRouter.createTable( "delivery");
		
		this.jdbcTemplate.execute("drop table if exists js_delivery_item_2");
		shopSaasDBRouter.createTable( "delivery_item");		
		
		this.jdbcTemplate.execute("update js_member_2 set biz_money=1000"); //更新会员预存存为1000元
		
	}
	
	/**
	 * 测试支付（后台）
	 */
	@Test
	public void testPay(){
		this.testAdd();
		orderFlowManager = this.getBean("orderFlowManager");
		cleanFlow();
		
		/********************进行参数检查测试************************************/
		PaymentLog paymentLog =  null;
		try{
			this.orderFlowManager.pay(paymentLog,false);
			Assert.fail("参数不正确定，不能支付");
		}catch(IllegalArgumentException e){
			
		}
		
		//不指明orderid
		paymentLog = new PaymentLog();
		paymentLog.setMoney(2000D);
		try{
			this.orderFlowManager.pay(paymentLog,false);
			Assert.fail("参数不正确定，不能支付");
		}catch(RuntimeException e){
			
		}
		//不指明money
		paymentLog = new PaymentLog();
		paymentLog.setOrder_id(1);
		try{
			this.orderFlowManager.pay(paymentLog,false);
			Assert.fail("参数不正确定，不能支付");
		}catch(RuntimeException e){
			
		}		
		
		
		
		/********************进行付款且金额不足************************************/
		//进行部分付款100元
		paymentLog = new PaymentLog();
		paymentLog.setOrder_id(1);
		paymentLog.setMoney(2000D);
		paymentLog.setBank("工商银行");
		paymentLog.setAccount("6000101010111");
		paymentLog.setPay_method("预存款支付");
		paymentLog.setPay_type("预存款支付");
		paymentLog.setPay_user("王峰");
		try{
			this.orderFlowManager.pay(paymentLog,false);
			Assert.fail("用户余额不足，不应支付");
		}catch(RuntimeException e){
			
		}
		
		
		/********************进行第一次付款************************************/
		//进行部分付款100元
	 
		paymentLog.setOrder_id(1);
		paymentLog.setMoney(100D);
		paymentLog.setBank("工商银行");
		paymentLog.setAccount("6000101010111");
		paymentLog.setPay_method("预存款支付");
		paymentLog.setPay_type("预存款支付");
		paymentLog.setPay_user("王峰");
		this.orderFlowManager.pay(paymentLog,false);
		
		Order order = this.orderManager.get(1);
		//验证支付状态为部分付款
		Assert.assertEquals(order.getPay_status().intValue() ,OrderStatus.PAY_PARTIAL_PAYED);
		
		//验证订单状态为已付款
		Assert.assertEquals(order.getStatus().intValue() ,OrderStatus.ORDER_PAY);
		

		
		/********************进行第二次付款************************************/
		//进行部分付款347元  完全付完
		PaymentLog paymentLog1 = new PaymentLog();
		paymentLog1.setOrder_id(1);
		paymentLog1.setMoney(347D);
		paymentLog1.setBank("工商银行");
		paymentLog1.setAccount("6000101010111");
		paymentLog1.setPay_method("预存款支付");
		paymentLog1.setPay_type("预存款支付");
		paymentLog1.setPay_user("王峰");
		this.orderFlowManager.pay(paymentLog1,false);
		order = this.orderManager.get(1);
		// 验证支付状态为部分付款
		Assert.assertEquals(order.getPay_status().intValue(),OrderStatus.PAY_YES);

		// 验证订单状态为已付款
		Assert.assertEquals(order.getStatus().intValue(),OrderStatus.ORDER_PAY);
		
		// 验证会员预存款=1000-447
		int biz_money = this.jdbcTemplate.queryForInt("select biz_money from js_member_2 where member_id=1" );
		Assert.assertEquals(biz_money,553);

	}

	
	
	
	 
	
	/***
	 * 退款测试
	 */
	@Test
	public void testRefund(){
		
		//进行支付
		this.testPay();
		
		/********************进行参数检查测试************************************/
		PaymentLog paymentLog =  null;
		try{
			this.orderFlowManager.refund(paymentLog);
			Assert.fail("参数不正确定，不能退款");
		}catch(IllegalArgumentException e){
			
		}
		
		//不指明orderid
		paymentLog = new PaymentLog();
		paymentLog.setMoney(2000D);
		try{
			this.orderFlowManager.refund(paymentLog);
			Assert.fail("参数不正确定，不能退款");
		}catch(RuntimeException e){
			
		}
		//不指明money
		paymentLog = new PaymentLog();
		paymentLog.setOrder_id(1);
		try{
			this.orderFlowManager.refund(paymentLog);
			Assert.fail("参数不正确定，不能退款");
		}catch(RuntimeException e){
			
		}		
		
		
		/********************进行退款且退款金额大于订单金额************************************/
		//进行退款500元
		paymentLog = new PaymentLog();
		paymentLog.setOrder_id(1);
		paymentLog.setMoney(500D);
		paymentLog.setBank("工商银行");
		paymentLog.setAccount("6000101010111");
		paymentLog.setPay_method("预存款支付");
		paymentLog.setPay_type("预存款支付");
		paymentLog.setPay_user("王峰");
		try{
			this.orderFlowManager.refund(paymentLog);
			Assert.fail("退款金额大于订单金额，不能退款");
		}catch(RuntimeException e){
			
		}
		/********************进行第一次退款************************************/
		//进行部分退款200元
		paymentLog = new PaymentLog();
		paymentLog.setOrder_id(1);
		paymentLog.setMoney(200D);
		paymentLog.setBank("工商银行");
		paymentLog.setAccount("6000101010111");
		paymentLog.setPay_method("预存款支付");
		paymentLog.setPay_type("预存款支付");
		paymentLog.setPay_user("王峰");
		this.orderFlowManager.refund(paymentLog);
		
		Order order = this.orderManager.get(1);
		//验证支付状态为部分退款
		Assert.assertEquals(order.getPay_status().intValue() ,OrderStatus.PAY_PARTIAL_REFUND);
		
		//验证订单状态为退款
		Assert.assertEquals(order.getStatus().intValue() ,OrderStatus.ORDER_CANCEL_PAY);
		

		
		/********************进行第二次退款************************************/
		//进行部分退款247元  完全退完
		PaymentLog paymentLog1 = new PaymentLog();
		paymentLog1.setOrder_id(1);
		paymentLog1.setMoney(247D);
		paymentLog1.setBank("工商银行");
		paymentLog1.setAccount("6000101010111");
		paymentLog1.setPay_method("预存款支付");
		paymentLog1.setPay_type("预存款支付");
		paymentLog1.setPay_user("王峰");
		this.orderFlowManager.refund(paymentLog1);
		order = this.orderManager.get(1);
		// 验证支付状态为退款
		Assert.assertEquals(order.getPay_status().intValue(),OrderStatus.PAY_CANCEL);

		// 验证订单状态为已退款
		Assert.assertEquals(order.getStatus().intValue(),OrderStatus.ORDER_CANCEL_PAY);
		
		
		// 验证会员预存款=1000
		int biz_money = this.jdbcTemplate.queryForInt("select biz_money from js_member_2 where member_id=1" );
		Assert.assertEquals(biz_money,1000);

	}
	
	
	/**
	 * 发货测试<br>
	 * 两个货品，购买量为1、2<br>
	 * 第一次分别发一个<br>
	 * 第二次将没发完的货品发完<br>
	 * 第三次第二个货品发2个，此时订单发货状态为已经发货完成应不能再发货
	 */
	@Test
	public void shippingTest1()
	{
		
		this.jdbcTemplate.execute("update js_goods_2 set store=10");
		this.jdbcTemplate.execute("update js_product_2 set store=10");
		//创建订单
		this.testAdd();
		orderFlowManager = this.getBean("orderFlowManager");
		cleanFlow();
		
		/************************第一次发货，2个商品发货量均为1******************************/
		//创建发货单
		Delivery delivery = new Delivery();
		delivery.setOrder_id(1);
		delivery.setMoney(10D);
		delivery.setIs_protect(0);
		delivery.setProtect_price(0D);
		//delivery.setMember_id(1);
		delivery.setLogi_id(1);
		delivery.setLogi_name("EMS");
		delivery.setLogi_no("10000001");
		delivery.setShip_type("");
		
		delivery.setProvince_id(1);
		delivery.setProvince("北京");
		delivery.setCity_id(2);
		delivery.setCity("北京");
		delivery.setRegion_id(3);
		delivery.setRegion("昌平区");
		
		delivery.setShip_name("王峰");
		delivery.setShip_addr("昌平区12号");
		delivery.setShip_email("kingapex@163.com");
		delivery.setShip_mobile("13718880644");
		delivery.setShip_zip("100020");
		
		//创建发货明细
		List<DeliveryItem> itemList  = new ArrayList<DeliveryItem>();
		DeliveryItem item1= new DeliveryItem();
		item1.setGoods_id(1);
		item1.setName("秀族09新款韩版淑女七分袖针织雪纺连衣裙");
		item1.setNum(1);
		item1.setProduct_id(2);
		itemList.add(item1);

		DeliveryItem item2= new DeliveryItem();
		item2.setGoods_id(3);
		item2.setName("新韩料雅致灰极致瘦腿提臀百搭裙子");
		item2.setNum(1);
		item2.setProduct_id(11);
		itemList.add(item2);
		
		List<DeliveryItem> giftItemList  = new ArrayList<DeliveryItem>();
		
		DeliveryItem gitem1= new DeliveryItem();
		gitem1.setGoods_id(1);
		gitem1.setProduct_id(1);
		gitem1.setItemtype(2);
		gitem1.setName("小项链");
		gitem1.setNum(1);
		
		giftItemList.add(gitem1);
		
		this.orderFlowManager.shipping(delivery, itemList,giftItemList);
		Order order = this.orderManager.get(1);
		
		// 验证订单状态为已经发货
		Assert.assertEquals(order.getStatus().intValue(),OrderStatus.ORDER_SHIP);

		// 验证订单收货状态为部分发货
		Assert.assertEquals(order.getShip_status().intValue(),OrderStatus.SHIP_PARTIAL_SHIPED);
		
		//验证商品库存为10-1
		int goodsStore = this.jdbcTemplate.queryForInt("select store from  js_goods_2 where goods_id=1");
		Assert.assertEquals(goodsStore,9);
		goodsStore = this.jdbcTemplate.queryForInt("select store from js_goods_2 where goods_id=3");
		Assert.assertEquals(goodsStore,9);
		
		//验证货品库存为10-1
		int productStore= this.jdbcTemplate.queryForInt("select store from js_product_2 where product_id=2");
		Assert.assertEquals(productStore,9);
		productStore= this.jdbcTemplate.queryForInt("select store from js_product_2 where product_id=11");
		Assert.assertEquals(productStore,9);
		
		/************************第二次发货，2个商品发货量为1******************************/
/*		itemList = new ArrayList<DeliveryItem>();
		DeliveryItem item3= new DeliveryItem();
		item3.setGoods_id(3);
		item3.setName("新韩料雅致灰极致瘦腿提臀百搭裙子");
		item3.setNum(1);
		item3.setProduct_id(11);
		itemList.add(item3);
		
		this.orderFlowManager.shipping(delivery, itemList);
		order = this.orderManager.get(1);
		
		// 验证订单状态为已经发货
		Assert.assertEquals(order.getStatus().intValue(),OrderStatus.ORDER_SHIP);

		// 验证订单收货状态为已发货
		Assert.assertEquals(order.getShip_status().intValue(),OrderStatus.SHIP_YES);
		
		//验证商品库存为10-1
		 goodsStore = this.jdbcTemplate.queryForInt("select store from  js_goods_2 where goods_id=1");
		Assert.assertEquals(goodsStore,9);
	
		
		//验证商品库存为10-1-1
		goodsStore = this.jdbcTemplate.queryForInt("select store from js_goods_2 where goods_id=3");
		Assert.assertEquals(goodsStore,8);
		
		//验证货品库存为10-1
		 productStore= this.jdbcTemplate.queryForInt("select store from js_product_2 where product_id=2");
		Assert.assertEquals(productStore,9);
		
		//验证货品库存为10-1-1
		productStore= this.jdbcTemplate.queryForInt("select store from js_product_2 where product_id=11");
		Assert.assertEquals(productStore,8);
*/
		/************************第三次发货，2个商品发货量为1******************************/
	/*	itemList = new ArrayList<DeliveryItem>();
		DeliveryItem item4= new DeliveryItem();
		item4.setGoods_id(3);
		item4.setName("新韩料雅致灰极致瘦腿提臀百搭裙子");
		item4.setNum(1);
		item4.setProduct_id(11);
		itemList.add(item3);
		
		try{
			this.orderFlowManager.shipping(delivery, itemList);
			Assert.fail("订单发货状态已经为已发货，不能对其进行发货");
		}catch(IllegalStateException e){
			
		}*/
			
	}

	
	/**
	 * 验证一次性将货发完的情况
	 */
	@Test
	public void shippingTest2()
	{
		
		this.jdbcTemplate.execute("update js_goods_2 set store=10");
		this.jdbcTemplate.execute("update js_product_2 set store=10");
		//创建订单
		this.testAdd();
		orderFlowManager = this.getBean("orderFlowManager");
		cleanFlow();
		
		/************************第一次发货，第一个商品发货量为1，第二个为2，全部发完******************************/
		//创建发货单
		Delivery delivery = new Delivery();
		delivery.setOrder_id(1);
		delivery.setMoney(10D);
		delivery.setIs_protect(0);
		delivery.setProtect_price(0D);
		//delivery.setMember_id(1);
		delivery.setLogi_id(1);
		delivery.setLogi_name("EMS");
		delivery.setLogi_no("10000001");
		delivery.setShip_type("");
		
		delivery.setProvince_id(1);
		delivery.setProvince("北京");
		delivery.setCity_id(2);
		delivery.setCity("北京");
		delivery.setRegion_id(3);
		delivery.setRegion("昌平区");
		
		delivery.setShip_name("王峰");
		delivery.setShip_addr("昌平区12号");
		delivery.setShip_email("kingapex@163.com");
		delivery.setShip_mobile("13718880644");
		delivery.setShip_zip("100020");
		
		//创建发货明细
		List<DeliveryItem> itemList  = new ArrayList<DeliveryItem>();
		DeliveryItem item1= new DeliveryItem();
		item1.setGoods_id(3);
		item1.setName("秀族09新款韩版淑女七分袖针织雪纺连衣裙");
		item1.setNum(1);
		item1.setProduct_id(22);
		item1.setItemtype(0);
		itemList.add(item1);

		DeliveryItem item2= new DeliveryItem();
		item2.setGoods_id(4);
		item2.setName("新韩料雅致灰极致瘦腿提臀百搭裙子");
		item2.setNum(2);
		item2.setProduct_id(28);
		item2.setItemtype(0);
		itemList.add(item2);
		
		List<DeliveryItem> giftItemList  = new ArrayList<DeliveryItem>();
		
		DeliveryItem gitem1= new DeliveryItem();
		gitem1.setGoods_id(1);
		gitem1.setProduct_id(1);
		gitem1.setItemtype(2);
		gitem1.setName("小项链");
		gitem1.setNum(1);
		giftItemList.add(gitem1);
		
		this.orderFlowManager.shipping(delivery, itemList,giftItemList);
		Order order = this.orderManager.get(1);
		
		// 验证订单状态为已经发货
		Assert.assertEquals(order.getStatus().intValue(),OrderStatus.ORDER_SHIP);

		// 验证订单收货状态为已发货
		Assert.assertEquals(order.getShip_status().intValue(),OrderStatus.SHIP_YES);
		
		//验证商品库存为10-1
		int goodsStore = this.jdbcTemplate.queryForInt("select store from  js_goods_2 where goods_id=3");
		Assert.assertEquals(goodsStore,9);
		
		//验证商品库存为10-2
		goodsStore = this.jdbcTemplate.queryForInt("select store from js_goods_2 where goods_id=4");
		Assert.assertEquals(goodsStore,8);
		
		//验证货品库存为10-1
		int productStore= this.jdbcTemplate.queryForInt("select store from js_product_2 where product_id=22");
		Assert.assertEquals(productStore,9);
		
		//验证商品库存为10-2
		productStore= this.jdbcTemplate.queryForInt("select store from js_product_2 where product_id=28");
		Assert.assertEquals(productStore,8);
		
	}
	

	
	
	
	/**
	 * 测试发货量大于购买量的情况
	 */
	@Test
	public void shippingTest3()
	{
		
		this.jdbcTemplate.execute("update js_goods_2 set store=10");
		this.jdbcTemplate.execute("update js_product_2 set store=10");
		//创建订单
		this.testAdd();
		orderFlowManager = this.getBean("orderFlowManager");
		cleanFlow();
		
		/************************第一次发货，第一个商品发货量为2，第二个为2，发货量大于购买量******************************/
		//创建发货单
		Delivery delivery = new Delivery();
		delivery.setOrder_id(1);
		delivery.setMoney(10D);
		delivery.setIs_protect(0);
		delivery.setProtect_price(0D);
		//delivery.setMember_id(1);
		delivery.setLogi_id(1);
		delivery.setLogi_name("EMS");
		delivery.setLogi_no("10000001");
		delivery.setShip_type("");
		
		delivery.setProvince_id(1);
		delivery.setProvince("北京");
		delivery.setCity_id(2);
		delivery.setCity("北京");
		delivery.setRegion_id(3);
		delivery.setRegion("昌平区");
		
		delivery.setShip_name("王峰");
		delivery.setShip_addr("昌平区12号");
		delivery.setShip_email("kingapex@163.com");
		delivery.setShip_mobile("13718880644");
		delivery.setShip_zip("100020");
		
		//创建发货明细
		List<DeliveryItem> itemList  = new ArrayList<DeliveryItem>();
		DeliveryItem item1= new DeliveryItem();
		item1.setGoods_id(1);
		item1.setName("秀族09新款韩版淑女七分袖针织雪纺连衣裙");
		item1.setNum(1);
		item1.setProduct_id(2);
		itemList.add(item1);

		DeliveryItem item2= new DeliveryItem();
		item2.setGoods_id(3);
		item2.setName("新韩料雅致灰极致瘦腿提臀百搭裙子");
		item2.setNum(3);
		item2.setProduct_id(11);
		itemList.add(item2);
		
		try{
			this.orderFlowManager.shipping(delivery, itemList,null);
			Assert.fail("发货量大于购买量");
		}catch(RuntimeException e){
			
		}
		
	}
	
	
	

	/**
	 * 测试发货订单不存在的情况
	 */
	@Test
	public void shippingTest4()
	{
		
		this.jdbcTemplate.execute("update js_goods_2 set store=10");
		this.jdbcTemplate.execute("update js_product_2 set store=10");
		//创建订单
		this.testAdd();
		orderFlowManager = this.getBean("orderFlowManager");
		cleanFlow();
		
		/************************第一次发货，第一个商品发货量为2，第二个为2，发货量大于购买量******************************/
		//创建发货单
		Delivery delivery = new Delivery();
		delivery.setOrder_id(14);
		delivery.setMoney(10D);
		delivery.setIs_protect(0);
		delivery.setProtect_price(0D);
		//delivery.setMember_id(1);
		delivery.setLogi_id(1);
		delivery.setLogi_name("EMS");
		delivery.setLogi_no("10000001");
		delivery.setShip_type("");
		
		delivery.setProvince_id(1);
		delivery.setProvince("北京");
		delivery.setCity_id(2);
		delivery.setCity("北京");
		delivery.setRegion_id(3);
		delivery.setRegion("昌平区");
		
		delivery.setShip_name("王峰");
		delivery.setShip_addr("昌平区12号");
		delivery.setShip_email("kingapex@163.com");
		delivery.setShip_mobile("13718880644");
		delivery.setShip_zip("100020");
		
		//创建发货明细
		List<DeliveryItem> itemList  = new ArrayList<DeliveryItem>();
		DeliveryItem item1= new DeliveryItem();
		item1.setGoods_id(1);
		item1.setName("秀族09新款韩版淑女七分袖针织雪纺连衣裙");
		item1.setNum(1);
		item1.setProduct_id(2);
		itemList.add(item1);

		DeliveryItem item2= new DeliveryItem();
		item2.setGoods_id(3);
		item2.setName("新韩料雅致灰极致瘦腿提臀百搭裙子");
		item2.setNum(3);
		item2.setProduct_id(11);
		itemList.add(item2);
		
		try{
			this.orderFlowManager.shipping(delivery, itemList,null);
			Assert.fail("发货的订单不存在，不能发货");
		}catch(ObjectNotFoundException e){
			
		}
		
	}	
	
	private Member getMember(){
		Member member = new Member();
		member.setMember_id(1);
		member.setName("bbb");
		member.setProvince_id(104);
		member.setCity_id(105);
		member.setRegion_id(106);
		member.setRegtime(1267572800000L);
		member.setBirthday(1267572800000L);
		member.setSex(1);
		member.setUname("测试");
		member.setEmail("1@2.com");
		member.setAddress("aaa");
		member.setZip("202020");
		member.setMobile("1391111234");
		member.setTel("119");
		member.setPw_question("Why?");
		member.setPw_answer("Oh!");
		member.setPassword("e4789cf2281e1d05a5685438d2cfad");//StringUtil.md5(752513)
		member.setPoint(1440);
		member.setLv_id(1);
		member.setAdvance(new Double(250));
		return member;
	}
	
	/**
	 * 测试退货
	 */
	@Test
	public void testReturned1(){
 
		//一次性将货发完
		shippingTest2();

		/************************退货，一个商品退货量为1，一个商号退货量为2******************************/
		//创建发货单
		Delivery delivery = new Delivery();
		delivery.setOrder_id(1);
		delivery.setMoney(10D);
		delivery.setIs_protect(0);
		delivery.setProtect_price(0D);
		delivery.setLogi_id(1);
		delivery.setLogi_name("EMS");
		delivery.setLogi_no("10000001");
		delivery.setShip_type("");
		
		delivery.setProvince_id(1);
		delivery.setProvince("北京");
		delivery.setCity_id(2);
		delivery.setCity("北京");
		delivery.setRegion_id(3);
		delivery.setRegion("昌平区");
		
		delivery.setShip_name("王峰");
		delivery.setShip_addr("昌平区12号");
		delivery.setShip_email("kingapex@163.com");
		delivery.setShip_mobile("13718880644");
		delivery.setShip_zip("100020");
		
		//创建退货明细
		List<DeliveryItem> itemList  = new ArrayList<DeliveryItem>();
		DeliveryItem item1= new DeliveryItem();
		item1.setGoods_id(3);
		item1.setName("秀族09新款韩版淑女七分袖针织雪纺连衣裙");
		item1.setNum(1);
		item1.setProduct_id(22);
		item1.setItemtype(0);
		itemList.add(item1);

		DeliveryItem item2= new DeliveryItem();
		item2.setGoods_id(4);
		item2.setName("新韩料雅致灰极致瘦腿提臀百搭裙子");
		item2.setNum(2);
		item2.setProduct_id(28);
		item2.setItemtype(0);
		itemList.add(item2);
		
		
		List<DeliveryItem> giftItemList  = new ArrayList<DeliveryItem>();
		
		DeliveryItem gitem1= new DeliveryItem();
		gitem1.setGoods_id(1);
		gitem1.setProduct_id(1);
		gitem1.setItemtype(2);
		gitem1.setName("小项链");
		gitem1.setNum(1);
		giftItemList.add(gitem1);
		
		this.orderFlowManager.returned(delivery, itemList,giftItemList);
		Order order = this.orderManager.get(1);
		
		// 验证订单状态为已经退货
		Assert.assertEquals(order.getStatus().intValue(),OrderStatus.ORDER_CANCEL_SHIP);

		// 验证订单收货状态为部分退货
		Assert.assertEquals(order.getShip_status().intValue(),OrderStatus.SHIP_CANCEL);
		
		//验证商品库存为10
		int goodsStore = this.jdbcTemplate.queryForInt("select store from  js_goods_2 where goods_id=3");
		Assert.assertEquals(goodsStore,10);
		goodsStore = this.jdbcTemplate.queryForInt("select store from js_goods_2 where goods_id=4");
		Assert.assertEquals(goodsStore,10);
		
		//验证货品库存为10
		int productStore= this.jdbcTemplate.queryForInt("select store from js_product_2 where product_id=22");
		Assert.assertEquals(productStore,10);
		productStore= this.jdbcTemplate.queryForInt("select store from js_product_2 where product_id=28");
		Assert.assertEquals(productStore,10);
		
	}

	
	/**
	 * 测试退货，测试退货量大于已发货量的情况
	 */
	@Test
	public void testReturned2(){
		//一次性将货发完
		shippingTest2();

		/************************退货，一个商品退货量为2，一个商号退货量为2******************************/
		//创建发货单
		Delivery delivery = new Delivery();
		delivery.setOrder_id(1);
		delivery.setMoney(10D);
		delivery.setIs_protect(0);
		delivery.setProtect_price(0D);
		delivery.setLogi_id(1);
		delivery.setLogi_name("EMS");
		delivery.setLogi_no("10000001");
		delivery.setShip_type("");
		
		delivery.setProvince_id(1);
		delivery.setProvince("北京");
		delivery.setCity_id(2);
		delivery.setCity("北京");
		delivery.setRegion_id(3);
		delivery.setRegion("昌平区");
		
		delivery.setShip_name("王峰");
		delivery.setShip_addr("昌平区12号");
		delivery.setShip_email("kingapex@163.com");
		delivery.setShip_mobile("13718880644");
		delivery.setShip_zip("100020");
		
		//创建发货明细
		List<DeliveryItem> itemList  = new ArrayList<DeliveryItem>();
		DeliveryItem item1= new DeliveryItem();
		item1.setGoods_id(1);
		item1.setName("秀族09新款韩版淑女七分袖针织雪纺连衣裙");
		item1.setNum(2); //退货量为2，大于已经发货量
		item1.setProduct_id(2);
		itemList.add(item1);

		DeliveryItem item2= new DeliveryItem();
		item2.setGoods_id(3);
		item2.setName("新韩料雅致灰极致瘦腿提臀百搭裙子");
		item2.setNum(2);
		item2.setProduct_id(11);
		itemList.add(item2);
		
		try{
			this.orderFlowManager.returned(delivery, itemList,null);
			Assert.fail("退货量大于已发货量，不能完成退货操作");
		}catch(RuntimeException e){
			
		}
		
		
		Order order = this.orderManager.get(1);
		

		// 验证订单状态为已经发货
		Assert.assertEquals(order.getStatus().intValue(),OrderStatus.ORDER_SHIP);

		// 验证订单收货状态为部分发货
		Assert.assertEquals(order.getShip_status().intValue(),OrderStatus.SHIP_YES);
		
		//验证商品库存为10-1
		int goodsStore = this.jdbcTemplate.queryForInt("select store from  js_goods_2 where goods_id=1");
		Assert.assertEquals(goodsStore,9);
		
		//验证商品库存为10-2
		goodsStore = this.jdbcTemplate.queryForInt("select store from js_goods_2 where goods_id=3");
		Assert.assertEquals(goodsStore,8);
		
		//验证货品库存为10-1
		int productStore= this.jdbcTemplate.queryForInt("select store from js_product_2 where product_id=2");
		Assert.assertEquals(productStore,9);
		
		//验证商品库存为10-2
		productStore= this.jdbcTemplate.queryForInt("select store from js_product_2 where product_id=11");
		Assert.assertEquals(productStore,8);
		
	}

	/**
	 * 测试完成
	 */
	@Test
	public void testComplete(){
		this.testAdd();
		orderFlowManager = this.getBean("orderFlowManager");
		this.orderFlowManager.complete(1);
		Order order = this.orderManager.get(1);
		Assert.assertEquals(order.getStatus().intValue(),OrderStatus.ORDER_COMPLETE);
	}

	/**
	 * 测试作废
	 */
	@Test
	public void testCancel(){
		this.testAdd();
		orderFlowManager = this.getBean("orderFlowManager");
		this.orderFlowManager.cancel(1);
		Order order = this.orderManager.get(1);
		Assert.assertEquals(order.getStatus().intValue(),OrderStatus.ORDER_CANCELLATION);
	}
	
	/**
	 * 测试读取某订单未发货明细
	 */
	@Test
	public void testListNotShipItem(){
		orderFlowManager = this.getBean("orderFlowManager");
		List<OrderItem> list  = this.orderFlowManager.listNotShipGoodsItem(2);
		System.out.println(list.get(0).getName());
	}

	
	
	/**
	 * 测试读取某订单发货明细
	 */
	@Test
	public void testListShipItem(){
		orderFlowManager = this.getBean("orderFlowManager");
		List<OrderItem> list  = this.orderFlowManager.listShipGoodsItem(1);
		System.out.println(list.get(0).getName());
	}
	
	
	
}
