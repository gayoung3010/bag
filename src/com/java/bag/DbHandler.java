package com.java.bag;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHandler {

	public final String DB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";

	private String dbUrl; 
	private String dbUserName; 
	private String dbPassword; 

	private Connection conn;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;

	public void connect(String dbUrl, String dbUserName, String dbPassword)
	{
		
		this.dbUrl = dbUrl;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
		
		try {
			Class.forName(DB_DRIVER_CLASS);
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void disconnect()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void selectAll() throws Exception{
		try {
			// 10번부터 번호가 두자리여서 칸이 안맞아서 예쁘게 출력하기 위해 상위 9개만 셀렉트
			String SQL = "SELECT * FROM student";
			stmt = conn.prepareStatement(SQL);
			rs = stmt.executeQuery();

			System.out.println("번호 이름  지역    휴대폰     성별 ");
			while (rs.next()) {

				int snumber = rs.getInt("snumber");
				String sname = rs.getString("sname");
				String sadress = rs.getString("sadress");
				String sphone = rs.getString("sphone");
				String sgender = rs.getString("sgender");

				System.out.println(snumber + " " + sname + " " + sadress + " " + sphone + " " + sgender);

			}
		} 
		catch (Exception e) {
			throw e;
		}
	}

	public void selectOne(String name) throws Exception{
		
		try {
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			String SQL = "SELECT * FROM student where sname = ?";
			stmt = conn.prepareStatement(SQL);
			stmt.setString(1, name);
			rs = stmt.executeQuery();

			System.out.println("번호 이름  지역    휴대폰     성별 ");
			while (rs.next()) {

				int snumber = rs.getInt("snumber");
				String sname = rs.getString("sname");
				String sadress = rs.getString("sadress");
				String sphone = rs.getString("sphone");
				String sgender = rs.getString("sgender");

				System.out.println(snumber + " " + sname + " " + sadress + " " + sphone + " " + sgender);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	
	public void insert(String name, String addr, String phone, String gender) throws Exception{

		try {
		
			String SQL = "INSERT INTO student (sname, sadress, sphone, sgender) VALUES (?,?,?,?)";
			stmt = conn.prepareStatement(SQL);

			stmt.setString(1, name);
			stmt.setString(2, addr);
			stmt.setString(3, phone);
			stmt.setString(4, gender);
		
			int count = stmt.executeUpdate();

			if( count > 0 ){
				System.out.println("데이터 입력 성공");
			}
			else{
				System.out.println("데이터 입력 실패");
			}
		} catch (Exception e) {
			throw e;
		} 
		
	}
	
	public void delete(int num) throws Exception  {
		
		try {
			
			String SQL = "DELETE from student where snumber = ?";
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1 , num);
			int count = stmt.executeUpdate();
			if( count > 0 ){
				System.out.println("데이터 입력 성공");
			}
			else{
				System.out.println("데이터 입력 실패");
			}
		}catch( Exception e) {
			throw e;
		}
	}
	
	public void update(int num, String name) throws Exception  {
		
		try {
			
			String SQL = "update student set sname = ? where snumber = ?";
			stmt = conn.prepareStatement(SQL);
			stmt.setString(1, name);
			stmt.setInt(2 , num);
			int count = stmt.executeUpdate();
			if( count > 0 ){
				System.out.println("데이터 입력 성공");
			}
			else{
				System.out.println("데이터 입력 실패");
			}
		}catch( Exception e) {
			throw e;
		}
	}


}
