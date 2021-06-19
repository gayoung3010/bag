package com.java.bag;


public interface Bag {

	public void open();
	
	public void putIn() throws Exception;
	
	public void selectAll();
	
	public void close();
	
	public void select();
	
	public void minus();
	
	public void connect(String dbUrl, String dbUserName, String dbPassword);
	
	public void disconnect();
	
	public void getBag();
}
