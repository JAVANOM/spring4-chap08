package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import spring.Member;
import spring.MemberDao;

public class MainForMemberDao {
	
	private static MemberDao memberDao;

	public static void main(String[] args) {
		//스프링 컨테이너 사용
		AbstractApplicationContext ctx =
		    new GenericXmlApplicationContext("classpath:appCtx.xml");
        
		// 컨테이너로부터 memberDao 빈을 검색해서 정적필드인 memberDao필드에 할당
		memberDao = ctx.getBean("memberDao", MemberDao.class);
		
		selectAll();
		updateMember();
		insertMember();
		
	    ctx.close(); 
		
	}
	
	private static void selectAll() {
		System.out.println("----- selectAll");
		// 전체 행 개수 구함
		int total = memberDao.count();
		System.out.println("전체 데이터: " +total);
		// 전체 데이터를 구함
		List<Member> members = memberDao.selectAll();
		for (Member m : members) {
			System.out.println(m.getId()+":"+m.getEmail()+":"+m.getName());
		}
	}
	
	private static void updateMember() {
		System.out.println("----- updateMember");
		// EMAIL이 madvirus@madvirus.net 인 member 객체를 구함
		Member member = memberDao.selecetByEmail("madvirus@madvirus.net");
		String oldPw = member.getPassword();
		String newPw = Double.toHexString(Math.random());
		// 비밀번호 변경 메서드를 실행
		member.changePassword(oldPw, newPw);
		
		//변경된 부분을 db에 저장
		memberDao.update(member);
		System.out.println("암호 변경 :" + oldPw + ">" + newPw);
		
	}
	
	private static void insertMember(){
		System.out.println("----- insertMember");
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
		String prefix = dateFormat.format(new Date());
		
		Member member = 
	        new Member(prefix + "@test.com",prefix,prefix, new Date());
		memberDao.insert(member);
		System.out.println(member.getId()+"데이터 추가");
	}

}
