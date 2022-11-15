package com.traccsolution.scaffold.aspect;

import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class XRayInspector extends AbstractXRayInterceptor {
    @Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && bean(*Controller)")
    public void xrayEnabledClasses() {
        /**
         * This function is empty, and should remain so.
         * It serves as the host for a pointcut instructing the interceptor about which methods to wrap.
         * See AWS X-Ray Spring extensions Docs for more info
         * @see https://aws.amazon.com/blogs/devops/aspect-oriented-programming-for-aws-x-ray-using-spring
         */
    }

}
