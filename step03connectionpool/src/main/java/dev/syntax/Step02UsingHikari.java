package dev.syntax;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Step02UsingHikari {

	public static void main(String[] args) {
		try {
			Connection connection = HikariCPDataSource.getConnection();
			System.out.println(connection);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		Set<String> hash = new HashSet<>();
	}
}

