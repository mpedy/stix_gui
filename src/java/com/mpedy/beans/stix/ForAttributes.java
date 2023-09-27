package com.mpedy.beans.stix;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author cuore
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ForAttributes {

    boolean value() default true;
}
