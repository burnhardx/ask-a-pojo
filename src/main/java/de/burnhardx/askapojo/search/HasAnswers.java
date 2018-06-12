package de.burnhardx.askapojo.search;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.burnhardx.askapojo.search.model.Answer;
import de.burnhardx.askapojo.search.model.Answer.AnswerMaking;
import de.burnhardx.askapojo.search.model.ConditionDescriptor;
import de.burnhardx.askapojo.search.model.Question;
import de.burnhardx.askapojo.utils.AskableInformation;


/**
 * Base interface for all search handlers.
 * 
 * @author burnhardx
 */
public interface HasAnswers
{

  Logger log = LoggerFactory.getLogger(HasAnswers.class);

  Answer ask(Question question, AskableInformation information, Object source)
    throws ReflectiveOperationException;

  /**
   * Returns the generic type of the underlying field.
   *
   * @param clazz class of the field
   * @param type generic type of field
   */
  default Class<?> getGenericType(Class<?> clazz, Type type)
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

  @SuppressWarnings("unchecked")
  default Answer directAccess(Question question,
                              List<ConditionDescriptor> conditionDescriptors,
                              Object target,
                              PropertyDescriptor descriptor)
  {
    if (question.getConditions().size() == 0)
    {
      return new Answer(target);
    }
    else if (conditionDescriptors.size() == 0)
    {
      String attributeOfCondition = question.getConditions().get(0).getAttribute();
      if (Collection.class.isAssignableFrom(target.getClass()))
      {
        return getByIndex((List<Object>)target, attributeOfCondition, descriptor);
      }
      else if (Map.class.isAssignableFrom(target.getClass()))
      {
        return getByKey((Map<String, Object>)target, attributeOfCondition, descriptor);
      }
    }
    return new Answer(null);
  }

  default Answer getByKey(Map<String, Object> source, String key, PropertyDescriptor descriptor)
  {
    Object value = source.get(key);
    if (value != null)
    {
      return new Answer(value, AnswerMaking.builder().indexInList(-1).descriptor(descriptor).build());
    }
    return new Answer(null);
  }

  default Answer getByIndex(List<?> list, String index, PropertyDescriptor descriptor)
  {
    try
    {
      int indexInt = Integer.parseInt(index);
      return new Answer(list.get(indexInt),
                        AnswerMaking.builder().indexInList(indexInt).descriptor(descriptor).build());
    }
    catch (Exception e)
    {
      return new Answer(null);
    }
  }

  default Answer askAList(List<ConditionDescriptor> conditionDescriptors,
                          AskableInformation information,
                          List<Object> list,
                          PropertyDescriptor descriptor)
  {
    List<Answer> result = new ArrayList<>();
    int index = 0;
    for ( Object object : list )
    {
      if (conditionsFulfilled(object, conditionDescriptors))
      {
        result.add(new Answer(object,
                              AnswerMaking.builder().indexInList(index).descriptor(descriptor).build()));
      }
      index++;
    }
    return result.size() == 1 ? result.get(0) : new Answer(result);
  }

  default boolean conditionsFulfilled(Object target, List<ConditionDescriptor> conditionDescriptors)
  {
    long matchingConditions = conditionDescriptors.stream().filter(conditionDescriptor -> {
      Object value;
      try
      {
        value = conditionDescriptor.getDescriptor().getReadMethod().invoke(target);
        return conditionDescriptor.getCondition().matches(value);
      }
      catch (ReflectiveOperationException e)
      {
        log.error("can not invoke {}", conditionDescriptor.getDescriptor().getName());
        return false;
      }
    }).count();
    return matchingConditions == conditionDescriptors.size();
  }


}
