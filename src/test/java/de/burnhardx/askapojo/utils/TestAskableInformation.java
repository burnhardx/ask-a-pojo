package de.burnhardx.askapojo.utils;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

import de.burnhardx.askapojo.annotations.AskMe;
import de.burnhardx.askapojo.annotations.AskMeAs;
import de.burnhardx.askapojo.testdata.SimplePojo;


/**
 * Test cases for {@link AskableInformation}.
 * 
 * @author burnhardx
 */
public class TestAskableInformation
{

  /**
   * Asserts that {@link AskableInformation} maps all fields that are annotated with {@link AskMe} or
   * {@link AskMeAs}.
   */
  @Test
  public void hasInformationAboutFieldsThatAreAnnotatedAsAskMe()
  {

    AskableInformation underTest = AskableInformation.of(SimplePojo.class);
    assertThat(underTest.isAskable()).isTrue();
    assertThat(underTest.getName()).isEqualTo("dude");
    assertThat(underTest.getFields().get("list").getReadMethod().getName()).isEqualTo("getComplexList");
    assertThat(underTest.getFields().get("name").getReadMethod().getName()).isEqualTo("getName");
  }

  /**
   * Asserts that {@link AskableInformation} maps all methods that are annotated with {@link AskMe} or
   * {@link AskMeAs}.
   */
  @Test
  public void hasInformationAboutMethodsThatAreAnnotatedAsAskMe()
  {
    AskableInformation underTest = AskableInformation.of(SimplePojo.class);
    assertThat(underTest.getMethods().containsKey("uuid")).isTrue();
  }

}
