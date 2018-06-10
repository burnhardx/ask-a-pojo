package de.burnhardx.askapojo;

import static com.google.common.truth.Truth.assertThat;

import de.burnhardx.askapojo.testdata.AnyPojo;


/**
 * Test cases for {@link AskAPojo}.
 * 
 * @author burnhardx
 */
public class TestAskAPojo
{

  public void answersWithValues()
  {

    AnyPojo pojo = AnyPojo.testSet();

    AskAPojo askAPojo = new AskAPojo(pojo);
    assertThat((String)askAPojo.about("anyText")).isEqualTo("heissa");
  }

}
