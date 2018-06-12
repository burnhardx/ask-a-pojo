package de.burnhardx.askapojo;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import de.burnhardx.askapojo.testdata.SimplePojo;


/**
 * Test cases for {@link AskAPojo}.
 * 
 * @author burnhardx
 */
public class TestAskAPojo
{

  private static final AskAPojo askAPojo = new AskAPojo(SimplePojo.testSet());

  @Test
  public void directAccess()
  {
    assertThat((String)askAPojo.about("name").getResult()).isEqualTo("heino");
  }

  @Test
  public void accessListElementByQuery()
  {
    assertThat(((SimplePojo)askAPojo.about("list[name==stefan]").getResult()).getAge()).isEqualTo(4711);

    assertThat(((SimplePojo)askAPojo.about("list[name==martin]").getResult()).getAge()).isEqualTo(88);

    assertThat(askAPojo.about("list[name==martin]/age").getResult()).isEqualTo(88);

  }

  @Test
  public void accessMapElementByQuery()
  {
    assertThat(((SimplePojo)askAPojo.about("complexMap[name==julio]").getResult()).getAge()).isEqualTo(78);
  }

  @Test
  public void accessByIndex()
  {
    assertThat(((SimplePojo)askAPojo.about("list[1]").getResult()).getAge()).isEqualTo(4711);
  }

  @Test
  public void accessByKey()
  {
    assertThat(((SimplePojo)askAPojo.about("complexMap[stallion]").getResult()).getAge()).isEqualTo(78);
  }

  @Test
  public void accessWithNonsense()
  {
    assertThat(askAPojo.about("nonsense").getResult()).isNull();
    assertThat(askAPojo.about("nonsense").getMaking()).isNull();
    assertThat(askAPojo.about("list[1]").getResult()).isNotNull();
    assertThat(askAPojo.about("list[1]/nonsense").getResult()).isNull();
  }

  @Test
  @Ignore
  public void accessMethods()
  {
    String uuid = (String)askAPojo.about("uuid").getResult();
    assertThat(uuid).isNotNull();
    assertThat(uuid).startsWith("XYZ");
  }

}
