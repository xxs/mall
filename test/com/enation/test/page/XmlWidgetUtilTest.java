package com.enation.test.page;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.enation.eop.processor.widget.WidgetXmlUtil;

/**
 * widget xml 工具类测试
 * @author kingapex
 * 2010-2-10下午03:17:25
 */
public class XmlWidgetUtilTest {
	
	/**
	 * 
	 * 给定一个固定的widget xml地址
	 * 测试是否可以正常读取挂件参数
	 */
	@Test
	public void parseTest(){
		
		String path = "D:\\workspace\\eop\\eop\\src\\test\\java\\com\\enation\\page\\widgets.xml";
		Map<String,Map<String,Map<String,String>>> widgets = WidgetXmlUtil.parse(path);
	    Map<String,Map<String,String>> page = widgets.get("/index.html");
	    Map<String,String> param = page.get("1");
	    String type = param.get("type");
		Assert.assertEquals(type, "goods_list");
		
		
	}

	/**
	 * 将一个页面解析为参数Map测试
	 */
	@Test
	public void jsonParseTest(){
		String json="["
			+"{'id':'1','type':'goods_list','tag_id':'4','border':'border1'},"
			+"{'id':'2','type':'goods_detail','border':'border2'}"
			+"]";
		List<Map<String,String>> mapList= WidgetXmlUtil.jsonToMapList(json);
		for(Map<String,String> paramMap: mapList){
			System.out.println("type->"+paramMap.get("type"));
		}
		
	}	
	
	
	/**
	 * 将一个页面Map参数格式转换为Json格式的数组
	 */
	@Test
	public void mapToJsonTest(){
		String path = "D:\\workspace\\eop\\eop\\src\\test\\java\\com\\enation\\page\\widgets.xml";
		Map<String,Map<String,Map<String,String>>> widgets = WidgetXmlUtil.parse(path);
		Map<String,Map<String,String>> params =widgets.get("/index.html");
		String json = WidgetXmlUtil.mapToJson(params);
		System.out.println(json);
	}
	
	
	
	/**
	 * 挂件参数保存测试
	 */
	@Test
	public void saveTest(){
		
		String path = "D:\\workspace\\eop\\eop\\src\\test\\java\\com\\enation\\page\\widgets.xml";
		String json="["
			+"{'id':'1','type':'goods_list','tag_id':'4','border':'border1'},"
			+"{'id':'2','type':'goods_detail','border':'border2'}"
			+"]";
		List<Map<String,String>> mapList= WidgetXmlUtil.jsonToMapList(json);	
		WidgetXmlUtil.save(path, "/index.html", mapList);

		//断言
		Map<String,Map<String,Map<String,String>>> widgets = WidgetXmlUtil.parse(path);
	    Map<String,Map<String,String>> page = widgets.get("/index.html");
	    Map<String,String> param = page.get("1");
	    String tag_id = param.get("tag_id");
		Assert.assertEquals(tag_id, "4");
		
	}
}
