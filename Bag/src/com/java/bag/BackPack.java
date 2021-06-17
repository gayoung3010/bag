package com.java.bag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BackPack implements Bag {

	public final String DB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";

	private List<Product> items;
	private Boolean isOpened;

	private String dbUrl;
	private String dbUserName;
	private String dbPassword;

	private Connection conn;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;

	public void connect(String dbUrl, String dbUserName, String dbPassword) {
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

	public void disconnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public BackPack() {
		this.items = new ArrayList<>();
		this.isOpened = false;
	}

	// DB에서 백팩안에 아이템을 전부 조회해서 넣어줌 (매번 select 하기 힘들어서)
	public void getBackPackData() throws Exception {
		// 1
		// db에서 bag_kind가 맞는 데이터를 조회한 후 items에 넣는다 (select)
		String SQL = "select * from bag where bag_kind = 'backpack'";
		stmt = conn.prepareStatement(SQL);
		rs = stmt.executeQuery();

		ArrayList<Product> itemList = new ArrayList<>();
		while (rs.next()) {
			Product p = new Product();
			p.setName(rs.getString("item_name"));
			p.setAmount(rs.getInt("item_amount"));
			itemList.add(p);
		}

		items = itemList;

	}

	@Override
	public void open() {

		isOpened = true;
		System.out.println("백팩을 열었습니다.");
	}

	@Override
	public void putIn() throws Exception {

		Scanner scanner = new Scanner(System.in);

		if (isOpened) {
			getBackPackData();
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
					// 3
					// db에 bag table에 bag_kind와 item_name이 같은 곳에 물건 갯수 추가 (update)
					String SQL = "update bag set item_amount = ? where item_name = ? and bag_kind = 'backpack' ";
					stmt = conn.prepareStatement(SQL);
					stmt.setInt(1, n);
					stmt.setString(2, item);
					int count = stmt.executeUpdate();
					if (count > 0) {
						System.out.println("물품이 성공적으로 들어갔습니다.");
					} else {
						System.out.println("물품 추가에 실패했습니다. 다시시도해주세요.");
					}
//					p.setAmount(n);
				}
			}
			if (flag) {
				// 2
				// db에 bag테이블에 가방 종류와 함께 아이템이름, 아이템 수 추가 (insert)
				String SQL = "insert into bag (bag_kind, item_name, item_amount) VALUES ('backpack',?,?)";
				stmt = conn.prepareStatement(SQL);

				stmt.setString(1, item);
				stmt.setInt(2, num);

				int count = stmt.executeUpdate();

				if (count > 0) {

					System.out.println("물품이 성공적으로 들어갔습니다.");
				} else {
					System.out.println("물품 추가에 실패했습니다. 다시시도해주세요.");

				}
//				// 새로운 아이템일 경우  이건 이제 필요없음
//				Product newProduct = new Product();
//				newProduct.setName(item);
//				newProduct.setAmount(num);
//				
//				items.add(newProduct); 
			} else {

				System.out.println("물품이 성공적으로 들어갔습니다.");
			}

		} else {
			System.out.println("경고 ! 백팩이 닫혀있습니다.");
		}
	}

	@Override
	public void minus() {
		Scanner scanner = new Scanner(System.in);
		if (isOpened == true) {
			try {
				getBackPackData();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
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
							if (num < p.getAmount()) {
								int n = p.getAmount() - num;
								// 4
								// bag table에서 bag_kind와 item_name으로 물건을 뺌 (update)-> 갯수가 남았을때 
								String SQL = "update bag set item_amount = ? where item_name = ? and bag_kind = 'backpack'";
								try {
									stmt = conn.prepareStatement(SQL);
									stmt.setInt(1, n);
									stmt.setString(2, item);

									int count = stmt.executeUpdate();
									if (count > 0) {
										System.out.println("정상적으로 물품을 뺏습니다.");
									} else {
										System.out.println("물품 빼기 실패했습니다.");
									}

								} catch (SQLException e) {

									e.printStackTrace();
								}
//								p.setAmount(n);

								break;

							} else if (num == p.getAmount()) {
								// 5
								// bag table에서 bag_kind와 item_name으로 물건을 지움 (delete) -> 물품갯수가 0이 됐을때 
								String SQL = "delete from bag where item_name = ? and bag_kind ='backpack'";

								try {
									stmt = conn.prepareStatement(SQL);
									stmt.setString(1, item);

									int count = stmt.executeUpdate();
									if (count > 0) {
										System.out.println("정상적으로 물품을 뺏습니다.");
									} else {
										System.out.println("물품 빼기에 실패했습니다");
									}
								} catch (SQLException e) {

									e.printStackTrace();
								}

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
			try {
				getBackPackData();
			} catch (Exception e) {

				e.printStackTrace();
			}
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
//		햄버거
//		select * from bag where bag_kind = 'backpack' and item_name = '햄버거';
		Scanner scanner = new Scanner(System.in);
		if (isOpened) {
			try {
				getBackPackData();
			} catch (Exception e) {

				e.printStackTrace();
			}
			System.out.println("찾으실 물품을 적어주세요");
			String item = scanner.nextLine();

			if (true) {
				// 물품을 비교한뒤 검색한 물품이 있다
				for (Product p : items) {
					if (p.getName().equals(item)) {
						for (int i = 0; i < items.size(); i++) {
							System.out.println(items.get(i).getName() + ", " + items.get(i).getAmount());
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

}
