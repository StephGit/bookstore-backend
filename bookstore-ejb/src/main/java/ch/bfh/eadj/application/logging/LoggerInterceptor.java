package ch.bfh.eadj.application.logging;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

@Logged
@Interceptor
public class LoggerInterceptor {

    private static final Logger logger = Logger.getLogger(LoggerInterceptor.class.getName());

    @AroundInvoke
    public Object logMethodCall(InvocationContext invocationContext) throws Exception {
        Object ret;
        logger.info("Entering method: "
                + invocationContext.getMethod().getName() + " in class "
                + invocationContext.getMethod().getDeclaringClass().getName());

        try {
            ret = invocationContext.proceed();
        } catch(Exception e) {
            throw e;
        } finally  {
            logger.info("Left method: "
                    + invocationContext.getMethod().getName() + " in class "
                    + invocationContext.getMethod().getDeclaringClass().getName());

        }
        return ret;
    }
}