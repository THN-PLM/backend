package server.thn.Aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import server.thn.Config.guard.AuthHelper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AssignModifierIdAspect {

    private final AuthHelper authHelper;

    @Before("@annotation(server.thn.Aop.AssignModifierId)")

    //부가 기능 수행 지점
    //메소드 호출 전 => @Before
    //@AssignMemberId 어노테이션이 적용된 메소드들은,
    // 본래의 메소드 수행 직전에 assignMemberId 메소드 호출

    public void assignMemberId(JoinPoint joinPoint) {
        //메소드 호출 전 메소드 수행
        //파라미터로 전달받은 JoinPoint를
        // 이용하여 호출되어야할 본래의 메소드에 대한 정보를 가져올

        Arrays.stream(joinPoint.getArgs())
                // JoinPoint.getArgs()를 이용하여 전달되는 인자들을 확인하고,
                // setMemberId 메소드가 정의된 타입이 있다면, memberId를 주입
                .forEach(arg -> getMethod(arg.getClass(), "setModifierId")
                        .ifPresent(setMemberId -> invokeMethod(arg, setMemberId, authHelper.extractMemberId())));
    }

    private Optional<Method> getMethod(Class<?> clazz, String methodName) {
        try {
            return Optional.of(clazz.getMethod(methodName, Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private void invokeMethod(Object obj, Method method, Object... args) {
        try {
            method.invoke(obj, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
