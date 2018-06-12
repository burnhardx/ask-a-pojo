package de.burnhardx.askapojo.search;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.burnhardx.askapojo.search.model.Answer;
import de.burnhardx.askapojo.search.model.Answer.AnswerMaking;
import de.burnhardx.askapojo.search.model.ConditionDescriptor;
import de.burnhardx.askapojo.search.model.Question;
import de.burnhardx.askapojo.utils.AskableInformation;
import de.burnhardx.askapojo.utils.ReflectionUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


/**
 * Utility class to search for fields.
 * 
 * @author burnhardx
 */
@UtilityClass
@Slf4j
public class AskForFields
{

  @SuppressWarnings("unchecked")
  public Answer ask(Question question, AskableInformation information, Object source)
    throws ReflectiveOperationException
  {
    PropertyDescriptor fieldDescriptor = information.getFields().get(question.getTarget());
    Field field = source.getClass().getDeclaredField(fieldDescriptor.getName());
    if (field == null)
    {
      return new Answer(null);
    }

    Class<?> genericTypeOfField = ReflectionUtils.getGenericType(field.getType(), field.getGenericType());
    List<ConditionDescriptor> conditionDescriptors = question.createConditionDescriptors(genericTypeOfField);
    Object target = fieldDescriptor.getReadMethod().invoke(source);

    if (isAccessibleDirectly(question, conditionDescriptors))
    {
      if (question.getConditions().size() == 0)
      {
        return new Answer(target);
      }
      else if (conditionDescriptors.size() == 0)
      {
        String attributeOfCondition = question.getConditions().get(0).getAttribute();
        if (Collection.class.isAssignableFrom(field.getType()))
        {
          return getByIndex((List<Object>)target, attributeOfCondition, fieldDescriptor);
        }
        else if (Map.class.isAssignableFrom(field.getType()))
        {
          return getByKey((Map<String, Object>)target, attributeOfCondition, fieldDescriptor);
        }
      }
    }

    if (Collection.class.isAssignableFrom(field.getType()))
    {
      return askAList(conditionDescriptors, information, (List<Object>)target, fieldDescriptor);
    }
    else if (Map.class.isAssignableFrom(field.getType()))
    {
      return askAList(conditionDescriptors,
                      information,
                      new ArrayList<>(((Map<?, Object>)target).values()),
                      fieldDescriptor);
    }
    return null;
  }


  private Answer getByKey(Map<String, Object> source, String key, PropertyDescriptor descriptor)
  {
    Object value = source.get(key);
    if (value != null)
    {
      return new Answer(value, AnswerMaking.builder().indexInList(-1).descriptor(descriptor).build());
    }
    return new Answer(null);
  }

  private Answer getByIndex(List<?> list, String index, PropertyDescriptor descriptor)
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


  private Answer askAList(List<ConditionDescriptor> conditionDescriptors,
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

  private boolean conditionsFulfilled(Object target, List<ConditionDescriptor> conditionDescriptors)
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

  private boolean isAccessibleDirectly(Question question, List<ConditionDescriptor> queryDescriptors)
  {
    if (question.getConditions().isEmpty())
    {
      return true;
    }
    if (question.getConditions().size() != queryDescriptors.size())
    {
      if (!question.getConditions().isEmpty() && queryDescriptors.isEmpty())
      {
        return true;
      }
    }
    return false;
  }

}
