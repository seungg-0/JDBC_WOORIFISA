package dev.syntax;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSource {
	private static HikariConfig config = new HikariConfig();
	private static HikariDataSource ds;
	
	static {
		
		// 필수 설정 파라미터 (url, username, password), classname은 선택 속성	
		config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
		config.setUsername("root");
		config.setPassword("1234");
		
		// 상세 프로퍼티 (https://github.com/brettwooldridge/HikariCP#frequently-used)
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepCacheSize", "250");
		config.addDataSourceProperty("preStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config); // 위에서 작성한 설정 코드를 기반으로 HikariCP인스턴스 생성
	}
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	private HikariCPDataSource() {}
	
}
