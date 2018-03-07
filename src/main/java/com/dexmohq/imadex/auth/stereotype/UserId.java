package com.dexmohq.imadex.auth.stereotype;


import java.lang.annotation.*;

/**
 * Marks injection point for user id as web method parameter
 *
 * @author Henrik Drefs
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserId {
}
