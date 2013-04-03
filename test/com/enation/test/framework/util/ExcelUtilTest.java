package com.enation.test.framework.util;

import org.junit.Test;

import com.enation.framework.util.ExcelUtil;

public class ExcelUtilTest {
	
	@Test
	public void test(){
		ExcelUtil excelUtil = new ExcelUtil();
		excelUtil.openModal("d:/access.xls");
		excelUtil.writeStringToCell(1, 1, "test");
		excelUtil.writeToFile("d:/a.xls");
	}
}
