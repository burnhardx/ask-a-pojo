package de.burnhardx.askapojo.change.model;

import de.burnhardx.askapojo.change.ChangeValues;
import lombok.Data;


/**
 * Stores the Method to invoke and the belonging parameters.
 * 
 * @author burnhardx
 */
@Data
public class CallMethod
{

  private final ChangeValues parent;

  private final String methodName;

  private Object[] parameters;

  /**
   * Returns the parent {@link ChangeValues} after the current instance of {@link ChangeField} is added to
   * changes.
   * 
   * @param givenValues
   */
  public ChangeValues with(Object... parameters)
  {
    this.parameters = parameters;
    parent.getCalls().add(this);
    return parent;
  }
}
