package de.burnhardx.askapojo.search;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.burnhardx.askapojo.search.model.Answer;
import de.burnhardx.askapojo.search.model.ConditionDescriptor;
import de.burnhardx.askapojo.search.model.Question;
import de.burnhardx.askapojo.utils.AskableInformation;
import lombok.extern.slf4j.Slf4j;


/**
 * Utility class to search for fields.
 * 
 * @author burnhardx
 */
@Slf4j
public class AnswersQuestionForField implements HasAnswers
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

    Class<?> genericTypeOfField = getGenericType(field.getType(), field.getGenericType());
    List<ConditionDescriptor> conditionDescriptors = question.createConditionDescriptors(genericTypeOfField);

    Object target = fieldDescriptor.getReadMethod().invoke(source);

    if (question.targetCanBeAccessedDirectly(conditionDescriptors))
    {
      return directAccess(question, conditionDescriptors, target, fieldDescriptor);
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
    return new Answer(null);
  }




}
