package spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

//회원가입 데이터 관련
public class MemberDao {
    
	private JdbcTemplate jdbcTemplate;
	
	public MemberDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Member selecetByEmail(String email) {
		
		//query 메서드로 실행할 쿼리를 지정 
		List<Member> results = jdbcTemplate.query("select * from Member where EMAIL = ?", new RowMapper<Member>() {
            // RowMapper는 ResultSet에서 데이터를 읽어와 Member객체로 변환해 주는 기능 제공 
			public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
				Member member = new Member(rs.getString("EMAIL"),
						        rs.getString("PASSWORD"),
						        rs.getString("NAME"),
						        rs.getTimestamp("REGDATE"));
				member.setId(rs.getLong("ID"));
				return member;
			}
		}, email); // 인덱스 파라미터 인 '?'에 들어갈 값, 가변인자가 두개 이상이면 콤마로 넣어준다.
		
		return results.isEmpty() ? null : results.get(0);
		
	}
	
	public void insert(final Member member) {
		//자동생성된 키 값을 구함
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		//update() 메서드는 PreparedStatementCreator 객체와 KeyHolder 객체를 파라미터로 갖는 메서드
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			//String 배열인 "ID"는 자동 생성되는 키 칼럼 목록을 지정할 때 사용 
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(
						"insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE)" + "values(?,?,?,?)", new String[] {"ID"});
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				pstmt.setTimestamp(4, new Timestamp(member.getRegisterDate().getTime()));
				
				return pstmt;
		     }
		},keyHolder);
		Number keyValue = keyHolder.getKey();
		member.setId(keyValue.longValue());
	
		
	}
	
	
	// jdk 1.8 이상일 때는 람다식을 통해 진행 가능
	public void insert2(final Member member) {
		//자동생성된 키 값을 구함
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		//update() 메서드는 PreparedStatementCreator 객체와 KeyHolder 객체를 파라미터로 갖는 메서드
		jdbcTemplate.update((Connection con) -> {
			
				PreparedStatement pstmt = con.prepareStatement(
						"insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE)" + "values(?,?,?,?)", new String[] {"ID"});
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				pstmt.setTimestamp(4, new Timestamp(member.getRegisterDate().getTime()));
				
				return pstmt;
		},keyHolder);
		Number keyValue = keyHolder.getKey();
		member.setId(keyValue.longValue());
	
		
	}
	
	
	
	public void update(Member member) {
		jdbcTemplate.update("update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?",
				            member.getName(), member.getPassword(), member.getEmail());
	}
	
	public List<Member> selectAll(){
		 List<Member> results = jdbcTemplate.query("select * from Member", new RowMapper<Member>() {

			public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
				Member member = new Member(rs.getString("EMAIL"), 
						   rs.getString("PASSWORD"),
						   rs.getString("NAME"),
						   rs.getTimestamp("REGDATE"));
				member.setId(rs.getLong("ID"));
				return member;
			}
			
		});
		return results;
		
	}
	
	public int count() {
		// 리턴 타입이 RowMapper러 변환해주는 타입
		Integer count = jdbcTemplate.queryForObject("select count(*) from MEMBER",
				Integer.class);
		
		return count;
	}
	
	
	
	
}
