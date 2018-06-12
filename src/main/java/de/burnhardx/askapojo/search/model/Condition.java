package de.burnhardx.askapojo.search.model;

import lombok.Builder;
import lombok.Value;


/**
 * Model that represents one attribute condition.
 * 
 * @author burnhardx
 */
@Value
@Builder
public class Condition
{

  private String attribute;

  private Operator operator;

  private String value;

  /**
   * Creates a
   * 
   * @param substring
   * @return
   */
  public static Condition fromString(String condition)
  {
    if (!condition.contains("="))
    {
      return new Condition(condition.substring(0, condition.indexOf(']')), null, null);
    }
    Operator operator = Operator.inString(condition);
    int operatorPos = condition.indexOf(operator.getSymbol());
    String attribute = condition.substring(0, operatorPos);
    String value = condition.substring(operatorPos + 2, condition.indexOf(']'));
    return new Condition(attribute, operator, value);
  }

  /**
   * @param toMatch
   * @return
   */
  public boolean matches(Object toMatch)
  {
    return operator.matches(toMatch, getValue());
  }

}
