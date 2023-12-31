package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. 어떤 데이터베이스에 연결할지를 String로 작성
        String connectionString = "jdbc:sqlite:db.sqlite";

        // 2. 해당 데이터베이스에 연결
        try (Connection connection = DriverManager.getConnection(connectionString)) {
            System.out.println("Connection Success!");

            // 3. 데이터베이스 연결 객체로부터 Statement 객체를 받는다.
            Statement statement = connection.createStatement();
            // 4. execute를 이용해 간단한 SQL문을 사용한다.
            statement.execute("""
            DROP TABLE IF EXISTS user;
            """);
            statement.execute("""
            CREATE TABLE user(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                password TEXT,
                first_name TEXT,
                last_name TEXT,
                email TEXT
            );
            """);

            // 서로다른 사용자 계정 3개 입력
            statement.execute("""
            INSERT INTO user (username, password, first_name, last_name, email)
            VALUES ('A','1234','Alex','Kim','a@gamil.com');
            """);
            statement.execute("""
            INSERT INTO user (username, password, first_name, last_name, email)
            VALUES ('B','5678','Brad','Park','b@gamil.com');
            """);
            statement.execute("""
            INSERT INTO user (username, password, first_name, last_name, email)
            VALUES ('C','9012','Chad','Lee','c@gamil.com');
            """);

            String insertSql = """
            INSERT INTO user (username, password, first_name, last_name, email)
            VALUES ('%s','%s','Chad','Lee','c@gamil.com');
            """;
//            Scanner scanner = new Scanner(System.in);
//            String username = scanner.nextLine();
//            String password = scanner.nextLine();
//            insertSql = String.format(insertSql, username, password);
//            statement.execute(insertSql);

            // Select 해보자.
            String selectSl = """
            SELECT * FROM user WHERE id = 1;
            """;
            //  조회하는 쿼리는 ResiltSet으로 데이터를 받는다.
            // ResultSet은 결과 테이블을 살펴볼 수 있게 도와주는 인터페이스
            ResultSet resultSet = statement.executeQuery(selectSl);
            if (resultSet.next()){
                System.out.println(resultSet.getString("username"));
                System.out.println(resultSet.getString("first_name"));
                System.out.println(resultSet.getString("email"));
            }else{
                System.out.println("Could not find");
            }
            // 다음 줄이 없으면 next()는 false를 반환한다.
            selectSl = """
            SELECT * FROM user;
            """;
            resultSet = statement.executeQuery(selectSl);
            while (resultSet.next()){
                System.out.println(resultSet.getString("username"));
//                System.out.println(resultSet.getString("first_name"));
//                System.out.println(resultSet.getString("email"));
            }

            String updateSql = """
                    UPDATE user
                    SET first_name = 'Alexander'
                    WHERE id = 1;
            """;
            // executeUpdate는 내가 실행한 sql 문의 결과로 바뀐 줄의 갯수를 반환
            int rows = statement.executeUpdate(updateSql);
            System.out.println("rows affected: " + rows);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}