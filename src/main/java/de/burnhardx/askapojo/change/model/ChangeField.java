package de.burnhardx.askapojo.change.model;

import de.burnhardx.askapojo.change.ChangeValues;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;


/**
 * Stores the changes to one field.
 * 
 * @author burnhardx
 */
@Data
public class ChangeField
{

  private final ChangeValues parent;

  private final String fieldName;

  @Setter(AccessLevel.NONE)
  private Object value;

  /**
   * Returns the parent {@link ChangeValues} after the current instance of {@link ChangeField} is added to
   * changes.
   * 
   * @param givenValues
   */
  public ChangeValues to(Object value)
  {
    this.value = value;
    parent.getChanges().add(this);
    return parent;
  }

}
