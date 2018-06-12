package de.burnhardx.askapojo.change;

import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import de.burnhardx.askapojo.change.model.CallMethod;
import de.burnhardx.askapojo.change.model.ChangeField;
import de.burnhardx.askapojo.exceptions.EmptyResultInAnswer;
import de.burnhardx.askapojo.search.model.Answer;
import de.burnhardx.askapojo.utils.AskableInformation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * Model to store informations to update a pojo.
 * 
 * @author burnhardx
 */
@Data
@Slf4j
public class ChangeValues
{

  private final Answer answer;

  private List<ChangeField> changes = new ArrayList<>();

  private List<CallMethod> calls = new ArrayList<>();

  /**
   * Initializes the changes.
   * 
   * @param fieldName name of the field to change.
   */
  public ChangeField change(String fieldName) throws EmptyResultInAnswer
  {
    checkEmptyResultInAnswer();
    return new ChangeField(this, fieldName);
  }

  /**
   * Returns an initialized {@link CallMethod} for later method invocation.
   * 
   * @param methodName name or AskMeAsName of the method to invoke
   * @throws EmptyResultInAnswer
   */
  public CallMethod call(String methodName) throws EmptyResultInAnswer
  {
    checkEmptyResultInAnswer();
    return new CallMethod(this, methodName);
  }

  private void checkEmptyResultInAnswer() throws EmptyResultInAnswer
  {
    if (answer == null || answer.getResult() == null)
    {
      throw new EmptyResultInAnswer("answer is not filled");
    }
  }

  /**
   * Execute the stored changes on the stored pojo.
   * 
   * @throws ReflectiveOperationException
   */
  public void execute() throws ReflectiveOperationException
  {
    AskableInformation askableInformation = AskableInformation.of(answer.getResult().getClass());
    executeChanges(askableInformation);
    executeCalls(askableInformation);
  }

  private void executeCalls(AskableInformation askableInformation) throws ReflectiveOperationException
  {
    for ( CallMethod call : calls )
    {
      String methodName = call.getMethodName();
      MethodDescriptor methodDescriptor = askableInformation.getMethods().get(methodName);
      if (methodDescriptor == null || methodDescriptor.getMethod() == null)
      {
        log.error("can not call method {}", methodName);
      }
      methodDescriptor.getMethod().invoke(answer.getResult(), call.getParameters());
    }
  }

  private void executeChanges(AskableInformation askableInformation)
    throws IllegalAccessException, InvocationTargetException
  {
    for ( ChangeField change : changes )
    {
      String fieldName = change.getFieldName();
      PropertyDescriptor askableField = askableInformation.getFields().get(fieldName);
      PropertyDescriptor propertyDescriptor = askableField == null
        ? askableInformation.getBeanInfo().getPropertyDescriptor(fieldName) : askableField;

      if (propertyDescriptor == null)
      {
        log.error("can not change value of {}, because the propertyDescriptor can not be found.", fieldName);
      }
      if (propertyDescriptor.getWriteMethod() == null)
      {
        log.error("can not change value of {}, because the propertyDescriptor has no write method",
                  fieldName);
      }
      propertyDescriptor.getWriteMethod().invoke(answer.getResult(), change.getValue());
    }
  }

}
