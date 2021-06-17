package com.java.bag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CrossBag implements Bag {

	private List<Product> items;
	private Boolean isOpened;
	private int num;

	public final String DB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";
	
	private String dbUrl; 
	private String dbUserName; 
	private String dbPassword;
	
	private Connection conn;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	public CrossBag() {

		this.items = new ArrayList<>();
		this.isOpened = false;
	}

	@Override
	public void open() {

		isOpened = true;
		System.out.println("백팩을 열었습니다.");
	}

	@Override
	public void putIn() throws Exception {
		
		try {
		Scanner scanner = new Scanner(System.in);

		String SQL = "INSERT INTO BAG (bagID, mount) values (?,?,?)";
		
		if (isOpened) {
			System.out.println("물품을 입력해주세요.");
			String item = scanner.nextLine();

			int num;
			while (true) {
				System.out.println("몇개를 넣으시겠습니까?");
				num = scanner.nextInt();
				scanner.nextLine();
				// 0개이하의 물품을 넣었을 경우 검사
				if (num > 0) {
					break;
				} else {
					System.out.println("0개 이상의 물건을 넣어주세요.");
				}
			}
			// 기존에 물품이 있는지 확인
			boolean flag = true;
			for (Product p : items) {
				if (p.getName().equals(item)) {
					flag = false;
					int n = p.getAmount() + num;
					p.setAmount(n);
				}
			}
			if (flag) {
				// 새로운 아이템일 경우
				Product newProduct = new Product();
				newProduct.setName(item);
				newProduct.setAmount(num);
				items.add(newProduct);
			}

			System.out.println("물품이 성공적으로 들어갔습니다.");
		} else {
			System.out.println("경고 ! 백팩이 닫혀있습니다.");
		}
	}catch (Exception e) {
		throw e;
	}
}

	@Override
	public void minus() {

		Scanner scanner = new Scanner(System.in);
		if (isOpened == true) {
			boolean loop = true;
			while (loop) {

				System.out.println("찾으실 물품을 입력해주세요.");
				String item = scanner.nextLine();

				boolean flag = true;
				for (Product p : items) {
					if (p.getName().equals(item)) {
						loop = false;
						flag = false;

						while (true) {
							System.out.println("몇개를 빼시겠습니까?");
							int num = scanner.nextInt();
							if (num <= p.getAmount()) {
								int n = p.getAmount() - num;
								p.setAmount(n);
								System.out.println("정상적으로 물품을 뺏습니다.");
								break;
							} else {
								System.out.println("수량을 다시 입력해주세요 (기존의 양보다 많습니다)");
							}
						}
					}
				}
				if (flag) {
					System.out.println("찾으시는 물품이 없습니다. 다시 입력해주세요.");
				}
			}

		} else {
			System.out.println("경고! 백팩이 닫혀있습니다.");
		}
	}

	@Override
	public void selectAll() {

		if (isOpened) {
			System.out.println("----------------------------------------------------");
			System.out.println("* 백팩 조회 시작");
			for (int i = 0; i < items.size(); i++) {
				System.out.println(items.get(i).getName() + ", " + items.get(i).getAmount() + '개');
			}
			System.out.println("* 백팩 조회 끝");
			System.out.println("----------------------------------------------------");
		} else {
			System.out.println("경고! 백팩이 닫혀있습니다.");
		}
	}

	@Override
	public void close() {
		isOpened = false;
		System.out.println("백팩이 닫혔습니다.");
	}

	@Override
	public void select() {

		Scanner scanner = new Scanner(System.in);
		if (isOpened) {
			System.out.println("찾으실 물품을 적어주세요");
			String item = scanner.nextLine();
			
			if (true) {
				// 물품을 비교한뒤 검색한 물품이 있다
				for(Product p : items) {
					if(p.getName().equals(item)) {
						for(int i=0; i<items.size(); i++) {
						System.out.println(items.get(i).getName()+", "+items.get(i).getAmount());
						}
					} else {
						// 물품을 비교한뒤 검색한 물품이 없다.
						System.out.println("찾으시는 물품이 없습니다.");
						
					}
				}
			}

		} else {
			System.out.println("경고! 백팩이 닫혀있습니다");
		}

	}

	@Override
	public void disconnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void connect(String dbUrl, String dbUserName, String dbPassword){
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



}
