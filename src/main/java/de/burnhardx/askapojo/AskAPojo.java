package de.burnhardx.askapojo;

import de.burnhardx.askapojo.utils.AskableInformation;


public class AskAPojo
{

  private Object source;

  private final AskableInformation information;

  public AskAPojo(Object source)
  {
    super();
    this.source = source;
    this.information = AskableInformation.of(source.getClass());
  }

  public Object about(String question)
  {

    return null;
  }

}
