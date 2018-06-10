package de.burnhardx.askapojo.testdata;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.burnhardx.askapojo.annotations.AskMe;
import de.burnhardx.askapojo.annotations.AskMeAs;
import lombok.Builder;
import lombok.Data;


/**
 * Only for testing purposes.
 * 
 * @author burnhardx
 */
@Data
@Builder
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

  public static AnyPojo testSet()
  {
    return AnyPojo.builder()
                  .anyText("heissa")
                  .anyInteger(666)
                  .complexList(Arrays.asList(AnyPojo.builder().anyText("martin").build(),
                                             AnyPojo.builder().anyText("stefan").build()))
                  .complexMap(Collections.singletonMap("stallion",
                                                       AnyPojo.builder()
                                                              .anyText("julio")
                                                              .anyInteger(78)
                                                              .build()))
                  .build();
  }

}
