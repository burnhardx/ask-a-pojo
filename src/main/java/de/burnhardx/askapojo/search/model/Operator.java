package de.burnhardx.askapojo.search.model;

import java.util.Arrays;


/**
 * All supported condition operators.
 * 
 * @author burnhardx
 */
public enum Operator
{

  EQUAL("=="), STARTS_WITH("|="), CONTAINS("*="), ENDS_WITH("$=");

  private final String symbol;

  private Operator(String symbol)
  {
    this.symbol = symbol;
  }

  /**
   * Returns the symbolization of the {@link Operator}.
   */
  public String getSymbol()
  {
    return symbol;
  }

  /**
   * Matches the given value against the given comparative.
   * 
   * @param value
   * @param comparative
   */
  public boolean matches(Object value, String comparative)
  {
    if (value == null)
    {
      return false;
    }
    String stringValue = value.toString();
    if (this == Operator.EQUAL)
    {
      return stringValue.equals(comparative);
    }
    else if (this == Operator.STARTS_WITH)
    {
      return stringValue.startsWith(comparative);
    }
    else if (this == Operator.ENDS_WITH)
    {
      return stringValue.endsWith(comparative);
    }
    else if (this == Operator.CONTAINS)
    {
      return stringValue.contains(comparative);
    }
    return false;
  }

  /**
   * Returns a feasible {@link Operator} or null.
   * 
   * @param query
   */
  public static Operator inString(String query)
  {
    if (!query.contains("="))
    {
      return null;
    }
    int pos = query.lastIndexOf('=');
    String operator = query.substring(pos - 1, pos + 1);
    return Arrays.asList(Operator.values())
                 .stream()
                 .filter(op -> op.getSymbol().equals(operator))
                 .findAny()
                 .orElse(null);
  }

}
