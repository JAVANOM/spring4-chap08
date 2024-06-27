package config;

import java.beans.PropertyVetoException;

import javax.management.RuntimeErrorException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import spring.ChangePasswordService;
import spring.MemberDao;

@Configuration
@EnableTransactionManagement //등록된 빈 객체 중에서 타입이 PlatformTransactionManager 인 빈을 트랜잭션으로 사용
public class AppConfig {
    
	@Bean(destroyMethod="close")
	public DataSource dataSource() {
		ComboPooledDataSource ds = new ComboPooledDataSource();
		
		try {
			ds.setDriverClass("com.mysql.cj.jdbc.Driver");
		} catch (PropertyVetoException e) {
			throw new RuntimeException(e);
		}
		ds.setJdbcUrl("jdbc:mysql://localhost:3306/spring4fs?characterEncoding=utf8");
		ds.setUser("spring5");
		ds.setPassword("Test1234@!");
		return ds;
	}
	
	@Bean
	public MemberDao memberDao() {
		return new MemberDao(dataSource());
	}
	
	// 트랜잭션 관리자로 사용
	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
	    tm.setDataSource(dataSource());
	    
	    return tm;
	}
	
	@Bean
	public ChangePasswordService changePwdSvc() {
		return new ChangePasswordService(memberDao());
	}
	
}
