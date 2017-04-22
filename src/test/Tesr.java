package test;

import org.junit.Test;

import utils.UrlValidate;

public class Tesr {

	@Test
	public void test(){
		String a = "http://192.168.1.195:8080/TestPage/test.jsp";
		System.out.println(UrlValidate.IsUrl(a));
	}
}
