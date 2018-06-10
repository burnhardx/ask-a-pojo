package de.burnhardx.askapojo.search;

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

  public static void main(String[] args)
  {
    String sd = "hu*=ssj";
    System.err.println(sd.substring(sd.indexOf('=') - 1, sd.indexOf('=') + 1));
  }

}
