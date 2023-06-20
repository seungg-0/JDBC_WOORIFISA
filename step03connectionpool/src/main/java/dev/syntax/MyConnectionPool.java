package dev.syntax;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyConnectionPool {
	// Field Summary
	// 1. DB ���� URL
	private String url;
	// 2. DB ������ �����ϴ� ����� ���� �̸�
	private String user;
	// 3. DB ������ �����ϴ� ����� ���� ��й�ȣ
	private String password;
	// 4. �̸� ������ Ŀ�ؼ� ��ü���� ������ ����Ʈ(Ŀ�ؼ� Ǯ), Ǯ��
	private static List<Connection> connectionPool;
	// 5. ���� Ŀ�ؼ� ��ü ����Ʈ
	private static List<Connection> usedConnections = new ArrayList<>();
	// 6. �ִ�� ������ Ŀ�ؼ� ����(���⼭�� �ִ� 10���� ����)
	private static int INITIAL_POOL_SIZE = 10;
	
	// Method Summary
	
	// 0. ������
	private MyConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
		super();
		this.url = url;
		this.user = user;
		this.password = password;
		MyConnectionPool.connectionPool = connectionPool; // �� this �ƴ���?
	}
	
	// 1. Ŀ�ؼ� Ǯ ���� �޼���
	public static MyConnectionPool create(String url, String user, String password) throws SQLException {
		 // �ϳ��� Pool ���ο��� ���� ���� Ŀ�ؼ� (Connection connection) �ν��Ͻ��� �ִ�.
	     List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
	     // �ִ� ������ (INITIAL_POOL_SIZE)��ŭ Ŀ�ؼ� ��ü ����
	     for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
        	// createConnection()���� �ϳ��� Ŀ�ؼ� �ν��Ͻ� ���� ��, Pool�� �߰�
			pool.add(createConnection(url, user, password));
	     }
	     return new MyConnectionPool(url, user, password, pool);
	}
	
	// 2. DB ����� �������� ����� Ŀ�ؼ� ��ü�� ��ȯ�ϴ� �޼���
	public static Connection getConnection() {
		Connection connection = connectionPool.remove(connectionPool.size() -1);
		usedConnections.add(connection); // ��� ���� Ŀ�ؼ� ����Ʈ�� Ŀ�ؼ� 1�� �߰�
		return connection;
		
	}
	
	// 3. ����� ó���� ������ �� ����� Ŀ�ؼ��� �ݳ��ϴ� �޼���
	public static boolean releaseConnection(Connection connection) {
		connectionPool.add(connection);
		return usedConnections.remove(connection);
	}
	
	// 4. �ϳ��� Ŀ�ؼ� ��ü ���� �޼���
	private static Connection createConnection(String url, String user, String password) throws SQLException {
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	
	// 5. Ŀ�ؼ� Ǯ ���� �޼���
	public static void shutdown() throws SQLException {
		usedConnections.forEach(MyConnectionPool::releaseConnection); // �Լ��� �������̽�, �޼��� ���� ����
		for (Connection connection : connectionPool) {
	         connection.close(); // �ڿ� �ݳ�
	    }
		connectionPool.clear(); // Ǯ ���� Ŀ�ؼ� �ν��Ͻ� ����
		
		
//		for (Connection connection : connectionPool) {
//	         connection.close();
//	     }
//	     for (Connection connection : usedConnections) {
//	         connection.close();
//	     }
	}
	
}


////1. Ŀ�ؼ� Ǯ ���� �޼���
//	public static MyConnectionPool create(String url, String user, String password) throws SQLException {
//     List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
//     for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
//         pool.add(createConnection(url, user, password));
//     }
//     return new MyConnectionPool(url, user, password, pool);
// }
//	
//	// 2. DB ����� �������� ����� Ŀ�ؼ� ��ü�� ��ȯ�ϴ� �޼���
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
//	// 3. ����� ó���� ������ �� ����� Ŀ�ؼ��� �ݳ��ϴ� �޼���
// public static boolean releaseConnection(Connection connection) {
//     connectionPool.add(connection);
//     return usedConnections.remove(connection);
// }
// // 4. �ϳ��� Ŀ�ؼ� ��ü ���� �޼���
// private static Connection createConnection(String url, String user, String password) throws SQLException {
//     return DriverManager.getConnection(url, user, password);
// }
// // 5. Ŀ�ؼ� Ǯ ���� �޼���
// public static void shutdown() throws SQLException {
//     for (Connection connection : connectionPool) {
//         connection.close();
//     }
//     for (Connection connection : usedConnections) {
//         connection.close();
//     }
// }
