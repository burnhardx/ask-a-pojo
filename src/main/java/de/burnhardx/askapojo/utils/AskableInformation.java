package de.burnhardx.askapojo.utils;

import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import de.burnhardx.askapojo.annotations.AskMe;
import de.burnhardx.askapojo.annotations.AskMeAs;
import lombok.Value;


/**
 * Collects informations about fields and methods that are annotated with {@link AskMe} or {@link AskMeAs}.
 * 
 * @author burnhardx
 */
@Value
public class AskableInformation
{

  private Map<String, PropertyDescriptor> fields;

  private Map<String, MethodDescriptor> methods;

  private boolean askable;

  private String name;

  private BeanInformation beanInfo;

  private static final Predicate<AccessibleObject> IS_QUERYABLE = elem -> elem.isAnnotationPresent(AskMe.class)
                                                                          || elem.isAnnotationPresent(AskMeAs.class);

  /**
   * Returns a initialzed AskableInformation by the given class.
   * 
   * @param clazz
   */
  public static AskableInformation of(Class<?> clazz)
  {
    Map<String, PropertyDescriptor> fields = new HashMap<>();
    Map<String, MethodDescriptor> methods = new HashMap<>();
    BeanInformation beanInformation = BeanInformation.of(clazz);
    boolean isAskable = clazz.isAnnotationPresent(AskMe.class) || clazz.isAnnotationPresent(AskMeAs.class);
    String name = "";
    if (isAskable)
    {
      name = clazz.isAnnotationPresent(AskMeAs.class) ? clazz.getAnnotation(AskMeAs.class).value()
        : clazz.getName();
    }
    Arrays.asList(clazz.getDeclaredFields()).stream().filter(IS_QUERYABLE).forEach(field -> {
      fields.put(getAskableName(field), beanInformation.getPropertyDescriptor(field.getName()));
    });
    Arrays.asList(clazz.getDeclaredMethods()).stream().filter(IS_QUERYABLE).forEach(method -> {
      methods.put(getAskableName(method), beanInformation.getMethodDescriptor(method.getName()));
    });
    return new AskableInformation(fields, methods, isAskable, name, beanInformation);
  }

  private static String getAskableName(Object element)
  {
    AccessibleObject accessible = (AccessibleObject)element;
    return accessible.isAnnotationPresent(AskMeAs.class) ? accessible.getAnnotation(AskMeAs.class).value()
      : ((Member)element).getName();
  }

}
