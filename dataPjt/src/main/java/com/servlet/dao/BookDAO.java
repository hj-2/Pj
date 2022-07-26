package com.servlet.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.servlet.dto.BookDTO;

public class BookDAO {
	
	DataSource dataSource; //connection pool 사용위해 선언
	
	//주석한것 connection pool 생성으로 필요없어짐
	//connection pool 위해 새로 작성한것 표시
//	String driver = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:xe";
//	String id = "scott";
//	String pw = "tiger";
	
	public BookDAO() {
		try {
//			Class.forName(driver);
			Context context = new InitialContext(); //connection pool
			dataSource = (DataSource)context.lookup("java:comp/env/jdbc/Oracle11g"); //connection pool
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<BookDTO> select() {
		
		ArrayList<BookDTO> list = new ArrayList<BookDTO>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		
		try {
//			con = DriverManager.getConnection(url, id, pw);
			con = dataSource.getConnection(); //connection pool
			String sql = "SELECT * FROM book";
			pstmt = con.prepareStatement(sql);
			res = pstmt.executeQuery();
			
			while (res.next()) {
				int bookId = res.getInt("book_id");
				String bookName = res.getString("book_name");
				String bookLoc = res.getString("book_loc");
				
				BookDTO bookDTO = new BookDTO(bookId, bookName, bookLoc);
				list.add(bookDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(res != null) res.close();
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return list; //ArrayList<BookDTO> list = new ArrayList<BookDTO>();	
	}
}
