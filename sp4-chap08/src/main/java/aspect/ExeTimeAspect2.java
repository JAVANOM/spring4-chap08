package aspect;


import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

//공통 사용, 에노테이션을 적용한 클래스에 공통 기능과  Pointcut을 설정
// Order 애노테이션으로 적용순서를 지정할수 있음
@Aspect
@Order(1)
public class ExeTimeAspect2 {
    
	// 수식어 패턴 - 생략 가능 pubilc 등이 온다
	// 리턴타입 패턴 - 클래스 이름 및 메서드 이름을 패던으로 명시, 파라미터패턴 부분은 매칭될 파라미터에 대해서 명시
	// 각 패턴은 '*' 을 이용하여 모든 값을 표현, '..' 0개이상이하는 표현
	@Pointcut("execution(public * chap07..*(..))")
	private void publicTarget() {
	}
	
	//ProceedingJoinPoint 타입의 파라미터는 대상 객체의 메서드를 호출 할 때 사용
	//@Around 애노테이션을 사용해서 Around Advice로 사용된다고 설정 
	@Around("publicTarget()")
	public Object measure(ProceedingJoinPoint joinPoint) throws Throwable{
		long start = System.nanoTime();
		
		try {
			//실제 대상 객체의 메서드를 호출할 때는 proceed 메서드를 사용 -> 이 코드의 이전과 이후에 공통 기능을 위한 코드를 위치 시킴
			Object result = joinPoint.proceed();
			return result;
		} finally {
			long finish = System.nanoTime();
			Signature sig = joinPoint.getSignature(); // 대상 객체의 클래스 이름과 메서드 이름을 출력하기 위해 사용
			System.out.printf("%s.%s(%s) 실행시간 :%d ns\n",
					          joinPoint.getTarget().getClass().getSimpleName(),
					          sig.getName(),Arrays.toString(joinPoint.getArgs()),
					          (finish - start));
		}
		
		
	}
}
