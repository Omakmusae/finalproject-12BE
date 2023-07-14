package com.example.finalproject12be;

import static org.assertj.core.api.Fail.*;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.Test;

public class MySQLConnectionTest {
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConnection() {

		try(Connection con =
				DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/finaldb?createDatabaseIfNotExist=true&characterEncoding=UTF-8&serverTimezone=UTC",
					"root",
					"1234")){
			System.out.println(con);
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}
}