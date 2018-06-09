package de.burnhardx.askapojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Same as {@link AskMe} but the underlying field, method or type can be named.
 * 
 * @author burnhardx
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface AskMeAs
{

  String value();
}
