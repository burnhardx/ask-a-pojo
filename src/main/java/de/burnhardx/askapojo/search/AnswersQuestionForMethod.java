package de.burnhardx.askapojo.search;

import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.burnhardx.askapojo.search.model.Answer;
import de.burnhardx.askapojo.search.model.Answer.AnswerMaking;
import de.burnhardx.askapojo.search.model.ConditionDescriptor;
import de.burnhardx.askapojo.search.model.Question;
import de.burnhardx.askapojo.utils.AskableInformation;


/**
 * Answersearch related to methods.
 * 
 * @author burnhardx
 */
public class AnswersQuestionForMethod implements HasAnswers
{

  @SuppressWarnings("unchecked")
  @Override
  public Answer ask(Question question, AskableInformation information, Object source)
    throws ReflectiveOperationException
  {
    MethodDescriptor methodDescriptor = information.getMethods().get(question.getTarget());
    if (methodDescriptor == null || methodDescriptor.getMethod() == null)
    {
      return new Answer(null);
    }
    Method method = methodDescriptor.getMethod();
    if (method.getReturnType().equals(Void.TYPE))
    {
      if (method.getParameterCount() == 0)
      {
        method.invoke(source);
      }
      return new Answer(methodDescriptor.getMethod());
    }

    Object resultOfMethod = method.invoke(source);

    PropertyDescriptor propertyDescriptorByMethodName = information.getBeanInfo()
                                                                   .getPropertyDescriptorByMethod(method.getName());
    Class<?> genericTypeOfMethodResult = getGenericType(method.getReturnType(),
                                                        method.getGenericReturnType());

    if (genericTypeOfMethodResult == null)
    {
      return new Answer(resultOfMethod,
                        AnswerMaking.builder().descriptor(propertyDescriptorByMethodName).build());
    }

    List<ConditionDescriptor> conditionDescriptors = question.createConditionDescriptors(genericTypeOfMethodResult);
    if (Collection.class.isAssignableFrom(resultOfMethod.getClass()))
    {
      return askAList(conditionDescriptors,
                      information,
                      (List<Object>)resultOfMethod,
                      propertyDescriptorByMethodName);
    }
    else if (Map.class.isAssignableFrom(resultOfMethod.getClass()))
    {
      return askAList(conditionDescriptors,
                      information,
                      new ArrayList<>(((Map<?, Object>)resultOfMethod).values()),
                      propertyDescriptorByMethodName);
    }
    return new Answer(null);

  }

}
