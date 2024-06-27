package aspect;


import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

// 
//공통 사용
public class ExeTimeAspect {
    
	//ProceedingJoinPoint 타입의 파라미터는 대상 객체의 메서드를 호출 할 때 사용
	public Object measure(ProceedingJoinPoint joinPoint) throws Throwable{
		long start = System.nanoTime();
		
		try {
			//실제 대상 객체의 메서드를 호출할 때는 proceed 메서드를 사용 -> 이 코드의 이전과 이후에 공통 기능을 위한 코드를 위치 시킴
			Object result = joinPoint.proceed();
			return result;
		} finally {
			long finish = System.nanoTime();
			Signature sig = joinPoint.getSignature(); // 호출되는 메서드에 대한 정보를 구한다.
			//getTarget - 대상 객체를 구한다. getArgs - 파라미터 목록을 구한다.
			//getName - 메서드의 이름을 구한다. toLongString - 메서드를 완전하게 표현한 문장을 구한다. toShortString - 메서드를 축약해서 표현한 문장을 구한다. 
			System.out.printf("%s.%s(%s) 실행시간 :%d ns\n",
					          joinPoint.getTarget().getClass().getSimpleName(),
					          sig.getName(),Arrays.toString(joinPoint.getArgs()),
					          (finish - start)); 
		}
		
		
	}
}
