package com.dexmohq.imadex.auth.stereotype;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Details {
    String value();
}
