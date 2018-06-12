package de.burnhardx.askapojo.testdata;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.burnhardx.askapojo.annotations.AskMe;
import de.burnhardx.askapojo.annotations.AskMeAs;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


/**
 * Only for testing purposes.
 * 
 * @author burnhardx
 */
@Data
@Builder
@AskMeAs("dude")
public class SimplePojo
{

  private final int noSetter = 1;

  private String name;

  private Integer age;

  private Map<String, SimplePojo> complexMap;

  @Setter(AccessLevel.NONE)
  private int valueWithoutSetter = 0;

  @Getter(AccessLevel.NONE)
  private final int noNormalGetter = 666;

  @AskMeAs("list")
  private List<SimplePojo> complexList;

  @AskMe
  public void simpleVoidCallWithoutParameters()
  {
    valueWithoutSetter++;
  }

  @AskMe
  public Integer readValueWithoutGetter()
  {
    return noNormalGetter;
  }

  @AskMe
  public List<SimplePojo> anotherListGetter()
  {
    return complexList;
  }

  @AskMe
  public Map<String, SimplePojo> anotherMapGetter()
  {
    return complexMap;
  }

  /**
   * @return uuid
   */
  @AskMe
  public String uuid()
  {
    return "XYZ" + UUID.randomUUID().toString();
  }

  public static SimplePojo testSet()
  {
    return SimplePojo.builder()
                     .name("heino")
                     .age(666)
                     .complexList(Arrays.asList(SimplePojo.builder().name("martin").age(88).build(),
                                                SimplePojo.builder().name("stefan").age(4711).build()))
                     .complexMap(Collections.singletonMap("stallion",
                                                          SimplePojo.builder().name("julio").age(78).build()))
                     .build();
  }

}
