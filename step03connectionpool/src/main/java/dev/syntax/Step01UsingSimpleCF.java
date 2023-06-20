package dev.syntax;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Step01UsingSimpleCF {
	// CP : connection pool
	// 1. ���α׷� ���� �������� Ŀ�ؼ� Ǯ�� �̸� ����
	
	static { // static ��(block), ���α׷� ���� �� ���� �����ϴ� �ڵ�
		final String URL = "jdbc:mysql://localhost:3306/testdb";
		final String USER_NAME = "root";
		final String PASSWORD = "1234";
		
		try {
			MyConnectionPool.create(URL, USER_NAME, PASSWORD); /// ??? Ŭ���������� ���� ����
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
		// 2. getConnection���� �̸� ������ Ŀ�ؼ� ��ü�� ��� 
		Connection connection = MyConnectionPool.getConnection();
		
		// 3. DB ����� ó��
		String sql = "SELECT * FROM todo";
		try (PreparedStatement statement = connection.prepareStatement(sql);
			 ResultSet rs = statement.executeQuery();
				) {
			rs.next();
			int id = rs.getInt("id");
			String title = rs.getString("title");
			String description = rs.getString("description");
			Date dueDate = rs.getDate("due_date");
			int isCompleted = rs.getInt("is_completed");

			Todo todo = new Todo(id, title, description, dueDate.toLocalDate(), isCompleted == 1);
			System.out.println(todo); 
			
			// 4. ����� ó���� ������ ������ �޼��带 ȣ���ؼ� ����� Ŀ�ؼ��� Ŀ�ؼ� Ǯ�� �ٽ� �ݳ�
			MyConnectionPool.releaseConnection(connection);
			
			// 5. ���α׷��� ����� �� ������ �޼��带 ȣ���ؼ� ��� Ŀ�ؼ� ��ü close() -> Ŀ�ؼ� Ǯ�� ��� 
			MyConnectionPool.shutdown();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
