package de.burnhardx.askapojo.exceptions;

import de.burnhardx.askapojo.search.model.Answer;


/**
 * This exception is thrown if a {@link Answer} with no result will be changed.
 * 
 * @author burnhardx
 */
public class EmptyResultInAnswer extends Exception
{

  private static final long serialVersionUID = 1L;


  public EmptyResultInAnswer(String message)
  {
    super(message);
  }

  public EmptyResultInAnswer(String message, Throwable exception)
  {
    super(message, exception);
  }


}
