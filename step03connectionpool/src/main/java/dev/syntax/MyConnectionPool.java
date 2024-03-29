package dev.syntax;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyConnectionPool {
	// Field Summary
	// 1. DB 서버 URL
	private String url;
	// 2. DB 서버에 접근하는 사용자 계정 이름
	private String user;
	// 3. DB 서버에 접근하는 사용자 계정 비밀번호
	private String password;
	// 4. 미리 생성할 커넥션 객체들을 보관할 리스트(커넥션 풀), 풀장
	private static List<Connection> connectionPool;
	// 5. 사용된 커넥션 객체 리스트
	private static List<Connection> usedConnections = new ArrayList<>();
	// 6. 최대로 생성할 커넥션 개수(여기서는 최대 10개로 지정)
	private static int INITIAL_POOL_SIZE = 10;
	
	// Method Summary
	
	// 0. 생성자
	private MyConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
		super();
		this.url = url;
		this.user = user;
		this.password = password;
		MyConnectionPool.connectionPool = connectionPool; // 왜 this 아니지?
	}
	
	// 1. 커넥션 풀 생성 메서드
	public static MyConnectionPool create(String url, String user, String password) throws SQLException {
		 // 하나의 Pool 내부에는 여러 개의 커넥션 (Connection connection) 인스턴스가 있다.
	     List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
	     // 최대 사이즈 (INITIAL_POOL_SIZE)만큼 커넥션 객체 생성
	     for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
        	// createConnection()으로 하나의 커넥션 인스턴스 생성 후, Pool에 추가
			pool.add(createConnection(url, user, password));
	     }
	     return new MyConnectionPool(url, user, password, pool);
	}
	
	// 2. DB 입출력 과정에서 사용할 커넥션 객체를 반환하는 메서드
	public static Connection getConnection() {
		Connection connection = connectionPool.remove(connectionPool.size() -1);
		usedConnections.add(connection); // 사용 중인 커넥션 리스트에 커넥션 1개 추가
		return connection;
		
	}
	
	// 3. 입출력 처리가 끝났을 때 사용한 커넥션을 반납하는 메서드
	public static boolean releaseConnection(Connection connection) {
		connectionPool.add(connection);
		return usedConnections.remove(connection);
	}
	
	// 4. 하나의 커넥션 객체 생성 메서드
	private static Connection createConnection(String url, String user, String password) throws SQLException {
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	
	// 5. 커넥션 풀 종료 메서드
	public static void shutdown() throws SQLException {
		usedConnections.forEach(MyConnectionPool::releaseConnection); // 함수형 인터페이스, 메서드 참조 문법
		for (Connection connection : connectionPool) {
	         connection.close(); // 자원 반납
	    }
		connectionPool.clear(); // 풀 내에 커넥션 인스턴스 제거
		
		
//		for (Connection connection : connectionPool) {
//	         connection.close();
//	     }
//	     for (Connection connection : usedConnections) {
//	         connection.close();
//	     }
	}
	
}


////1. 커넥션 풀 생성 메서드
//	public static MyConnectionPool create(String url, String user, String password) throws SQLException {
//     List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
//     for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
//         pool.add(createConnection(url, user, password));
//     }
//     return new MyConnectionPool(url, user, password, pool);
// }
//	
//	// 2. DB 입출력 과정에서 사용할 커넥션 객체를 반환하는 메서드
//	public static Connection getConnection() throws SQLException {
//     if (connectionPool.isEmpty()) {
//         if (usedConnections.size() < INITIAL_POOL_SIZE) {
//             connectionPool.add(createConnection(url, user, password));
//         } else {
//             throw new RuntimeException("");
//         }
//     }
//     Connection connection = connectionPool.remove(connectionPool.size() - 1);
//     usedConnections.add(connection);
//     return connection;
// }
//	// 3. 입출력 처리가 끝났을 때 사용한 커넥션을 반납하는 메서드
// public static boolean releaseConnection(Connection connection) {
//     connectionPool.add(connection);
//     return usedConnections.remove(connection);
// }
// // 4. 하나의 커넥션 객체 생성 메서드
// private static Connection createConnection(String url, String user, String password) throws SQLException {
//     return DriverManager.getConnection(url, user, password);
// }
// // 5. 커넥션 풀 종료 메서드
// public static void shutdown() throws SQLException {
//     for (Connection connection : connectionPool) {
//         connection.close();
//     }
//     for (Connection connection : usedConnections) {
//         connection.close();
//     }
// }
