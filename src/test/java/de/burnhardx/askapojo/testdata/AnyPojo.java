package de.burnhardx.askapojo.testdata;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.burnhardx.askapojo.annotations.AskMe;
import de.burnhardx.askapojo.annotations.AskMeAs;
import lombok.Data;


/**
 * Only for testing purposes.
 * 
 * @author burnhardx
 */
@Data
@AskMeAs("dude")
public class AnyPojo
{

  private final int noSetter = 1;

  private String anyText;

  private Integer anyInteger;

  @AskMe
  private Map<String, AnyPojo> complexMap;

  @AskMeAs("list")
  private List<AnyPojo> complexList;

  /**
   * @return uuid
   */
  @AskMe
  public String uuid()
  {
    return UUID.randomUUID().toString();
  }

}
