package de.burnhardx.askapojo.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;


/**
 * Convenience wrapper for {@link BeanInfo}.
 * 
 * @author burnhardx
 */
@Value
@Slf4j
public class BeanInformation
{

  private List<PropertyDescriptor> properties;

  private List<MethodDescriptor> methods;

  /**
   * Creates a {@link BeanInfo} from given {@link Class} and initializes a {@link BeanInformation} with it.
   * 
   * @param clazz
   */
  public static BeanInformation of(Class<?> clazz)
  {
    try
    {
      BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
      return new BeanInformation(Arrays.asList(beanInfo.getPropertyDescriptors()),
                                 Arrays.asList(beanInfo.getMethodDescriptors()));
    }
    catch (IntrospectionException e)
    {
      log.error("can not instantiate beaninfo from {}", clazz, e);
      return new BeanInformation(new ArrayList<>(), new ArrayList<>());
    }
  }

  /**
   * Returns a {@link PropertyDescriptor} by matching name.
   * 
   * @param fieldName name of the property.
   */
  public PropertyDescriptor getPropertyDescriptor(String fieldName)
  {
    return properties.stream().filter(prop -> prop.getName().equals(fieldName)).findAny().orElse(null);
  }

  /**
   * Returns a {@link MethodDescriptor} by matching name.
   * 
   * @param methodName name of the method.
   */
  public MethodDescriptor getMethodDescriptor(String methodName)
  {
    return methods.stream().filter(prop -> prop.getName().equals(methodName)).findAny().orElse(null);

  }

}
