package com.java.bag;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Bag bag = null;

		try {
			String dbUrl = "jdbc:mariadb://127.0.0.1:3306/kgy"; // ???
			String dbUserName = "kgy"; // id
			String dbPassword = "kgy"; // pass
			Scanner scanner = new Scanner(System.in);

			System.out.println("가방 종류를 선택하세요 (1:백팩  2:크로스백)");
			int selectNo = scanner.nextInt();

			if (selectNo == 1) {
				bag = new BackPack();
			} else if (selectNo == 2) {
				bag = new CrossBag();
			}

			bag.connect(dbUrl, dbUserName, dbPassword);

			while (true) {
				System.out.println("---------------------------------------------------------------------------------");
				System.out
						.println("|1.가방 열기  | 2.물품 넣기  | 3.물품 전체 조회  | 4.닫다  | 5. 물품 선택 조회  | 6. 물품 빼기  | 0.프로그램 종료 |");
				System.out.println("--------------------------------------------------------------------------------");
				System.out.println(">선택");

				selectNo = scanner.nextInt();

				if (selectNo == 1) {
					bag.open();
				} else if (selectNo == 2) {
					bag.putIn();
				} else if (selectNo == 3) {
					bag.selectAll();
				} else if (selectNo == 4) {
					bag.close();
				} else if (selectNo == 5) {
					bag.select();
				} else if (selectNo == 6) {
					bag.minus();
				} else if (selectNo == 0) {
					break;
				}

			}
			System.out.println("프로그램을 종료합니다.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bag.disconnect();
		}
	}
}
