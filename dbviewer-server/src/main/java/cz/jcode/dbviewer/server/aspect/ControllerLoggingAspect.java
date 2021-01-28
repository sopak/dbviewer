package cz.jcode.dbviewer.server.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @NotNull}))
@ConditionalOnProperty(name = "cz.jcode.dbviewer.server.controller-logging-aspect.enabled", havingValue = "true")
public class ControllerLoggingAspect {

    private final ObjectMapper objectMapper;

    @SuppressWarnings({"EmptyMethod", "unused"})
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void annotationPointCutDefinition() {
    }

    @SuppressWarnings("EmptyMethod")
    @Pointcut("within(cz.jcode.dbviewer..*)")
    public void atExecution() {
    }

    @Before("annotationPointCutDefinition() && atExecution()")
    public void endpointBefore(JoinPoint p) {
        try {
            log.info(prepareMessage(p, p.getArgs(), "Request"));
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    @AfterReturning(pointcut = "annotationPointCutDefinition() && atExecution()", returning = "returnValue")
    public void endpointAfterReturning(JoinPoint p, Object returnValue) {
        try {
            log.info(prepareMessage(p, returnValue, "Response"));
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    @AfterThrowing(pointcut = "annotationPointCutDefinition() && atExecution()", throwing = "e")
    public void identifyEndpointAfterThrowing(JoinPoint p, Throwable e) {
        String error = e.getMessage();
        try {
            error = prepareMessage(p, error, "Error");
        } catch (Throwable ie) {
            log.error(e.getMessage(), ie);
        }
        log.error(error);
    }

    private String prepareMessage(JoinPoint p, Object value, String prefix) throws JsonProcessingException {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String uri = "unknown";
        String method = "unknown";
        if (requestAttributes != null) {
            uri = requestAttributes.getRequest().getRequestURI();
            method = requestAttributes.getRequest().getMethod();
        }
        return String.format(
                "%s %s, method: %s, uri: %s ,args: %s",
                prefix,
                p.getSignature().toShortString(),
                method,
                uri,
                objectMapper.writeValueAsString(value)
        );
    }
}