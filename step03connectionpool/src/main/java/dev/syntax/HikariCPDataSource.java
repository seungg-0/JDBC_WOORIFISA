package dev.syntax;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSource {
	private static HikariConfig config = new HikariConfig();
	private static HikariDataSource ds;
	
	static {
		
		// �ʼ� ���� �Ķ���� (url, username, password), classname�� ���� �Ӽ�	
		config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
		config.setUsername("root");
		config.setPassword("1234");
		
		// �� ������Ƽ (https://github.com/brettwooldridge/HikariCP#frequently-used)
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepCacheSize", "250");
		config.addDataSourceProperty("preStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config); // ������ �ۼ��� ���� �ڵ带 ������� HikariCP�ν��Ͻ� ����
	}
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	private HikariCPDataSource() {}
	
}
