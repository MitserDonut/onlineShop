package cn.work.test;

import java.util.UUID;

import org.junit.Test;

public class TestUUID {
	@Test
	public void m1(){
		System.out.println(UUID.randomUUID().toString());
	}
	
	
	@Test
	public void m2(){
		int a = 9;
		int b = 3;
		System.out.println(Math.ceil(a*1.0/b) );
	}
	
	public static void main(String[] args) {
		
	}
}
