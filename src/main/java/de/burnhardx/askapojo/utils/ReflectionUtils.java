package de.burnhardx.askapojo.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import lombok.experimental.UtilityClass;


/**
 * Utility class for reflective operations.
 * 
 * @author burnhardx
 */
@UtilityClass
public class ReflectionUtils
{


  /**
   * Returns the generic type of the underlying field.
   *
   * @param clazz class of the field
   * @param type generic type of field
   */
  public Class<?> getGenericType(Class<?> clazz, Type type)
  {
    if (Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz))
    {
      ParameterizedType parameterizedType = (ParameterizedType)type;
      int row = Map.class.isAssignableFrom(clazz) ? 1 : 0;
      Type genericType = parameterizedType.getActualTypeArguments()[row];
      if (genericType instanceof ParameterizedType)
      {
        return (Class<?>)((ParameterizedType)type).getRawType();
      }
      return (Class<?>)genericType;
    }
    return null;
  }
}
