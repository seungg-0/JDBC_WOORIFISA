package dev.syntax;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Step01UsingSimpleCF {
	// CP : connection pool
	// 1. 프로그램 시작 과정에서 커넥션 풀을 미리 생성
	
	static { // static 블럭(block), 프로그램 시작 시 먼저 동작하는 코드
		final String URL = "jdbc:mysql://localhost:3306/testdb";
		final String USER_NAME = "root";
		final String PASSWORD = "1234";
		
		try {
			MyConnectionPool.create(URL, USER_NAME, PASSWORD); /// ??? 클래스명으로 접근 복습
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
		// 2. getConnection으로 미리 생성된 커넥션 객체를 취득 
		Connection connection = MyConnectionPool.getConnection();
		
		// 3. DB 입출력 처리
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
			
			// 4. 입출력 처리가 끝나면 별도의 메서드를 호출해서 사용한 커넥션을 커넥션 풀에 다시 반납
			MyConnectionPool.releaseConnection(connection);
			
			// 5. 프로그램이 종료될 떄 별도의 메서드를 호출해서 모든 커넥션 객체 close() -> 커넥션 풀이 담당 
			MyConnectionPool.shutdown();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
