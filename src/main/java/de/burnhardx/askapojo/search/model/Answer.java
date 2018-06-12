package de.burnhardx.askapojo.search.model;

import java.beans.PropertyDescriptor;

import de.burnhardx.askapojo.AskAPojo;
import lombok.Builder;
import lombok.Data;
import lombok.Value;


/**
 * Represents an answer from {@link AskAPojo}.
 * 
 * @author burnhardx
 */
@Data
public class Answer
{

  @Value
  @Builder
  public static class AnswerMaking
  {

    private int indexInList;

    private PropertyDescriptor descriptor;
  }

  private final Object result;

  private final AnswerMaking making;

  /**
   * Initializes an Answer with the requested object and further information how the requested object was
   * resolved.
   * 
   * @param result requested object
   * @param making information how this object was found.
   */
  public Answer(Object result, AnswerMaking making)
  {
    super();
    this.result = result;
    this.making = making;
  }

  /**
   * Initializes an Answer with the requested object. Informations how this result was achieved are not
   * included.
   * 
   * @param result requested object
   */
  public Answer(Object result)
  {
    super();
    this.result = result;
    this.making = null;
  }


}
