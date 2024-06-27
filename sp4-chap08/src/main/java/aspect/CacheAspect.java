package aspect;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

//Advice 적용 순서
// 캐시를 구현한 Aspect, 실핼 결과를 Map 보관했다가 다음에 동일한 요청이 오면 Map 보관한 결과를 리턴(동일한 작업시 수행 x, 시간 감소)
public class CacheAspect {
    
	private Map<Long, Object> cache =
			new HashMap<Long, Object>();
	
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Long num = (Long)joinPoint.getArgs()[0];
		if(cache.containsKey(num)) {
			System.out.printf("CacheAspect : Cache에서 구함[%d]\n", num);
			return cache.get(num);
		}
		
		Object result = joinPoint.proceed();
		cache.put(num, result);
		System.out.printf("CacheAspect: Cache에 추가 [%d]\n",num);
		
		return result;
		
	}
}
