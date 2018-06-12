package de.burnhardx.askapojo;

import static com.google.common.truth.Truth.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.burnhardx.askapojo.exceptions.EmptyResultInAnswer;
import de.burnhardx.askapojo.search.model.Answer;
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
  public void willReturnNullIfQuestionsAreNonsense()
  {
    assertThat(askAPojo.about("nonsense").getResult()).isNull();
    assertThat(askAPojo.about("nonsense").getMaking()).isNull();
    assertThat(askAPojo.about("list[1]").getResult()).isNotNull();
    assertThat(askAPojo.about("list[1]/nonsense").getResult()).isNull();
  }

  @Test
  public void canInvokeVoidMethodsWithoutParameters()
  {
    askAPojo.about("list[1]/simpleVoidCallWithoutParameters");
    assertThat(((SimplePojo)askAPojo.about("list[1]").getResult()).getValueWithoutSetter()).isEqualTo(1);
  }

  @Test
  public void canInvokeGettersWithoutPropertyDescriptors()
  {
    assertThat(askAPojo.about("list[0]/readValueWithoutGetter").getResult()).isEqualTo(666);
  }

  @Test
  public void accessMethods()
  {
    String uuid = (String)askAPojo.about("uuid").getResult();
    assertThat(uuid).isNotNull();
    assertThat(uuid).startsWith("XYZ");
  }

  @Test
  public void findsEntriesInListsByGetters()
  {
    assertThat(((SimplePojo)askAPojo.about("anotherListGetter[name==stefan]")
                                    .getResult()).getAge()).isEqualTo(4711);
  }

  @Test
  public void findsEntriesInMapsByGetters()
  {
    assertThat(((SimplePojo)askAPojo.about("anotherMapGetter[name==julio]")
                                    .getResult()).getAge()).isEqualTo(78);
  }

  @Test
  public void findAndChangeValuesInFluentWay() throws EmptyResultInAnswer, ReflectiveOperationException
  {
    Answer about = askAPojo.about("list[name==stefan]");
    int ageBeforeUpdate = ((SimplePojo)about.getResult()).getAge();
    about.updatePojo().change("age").to(4).execute();
    assertThat(((SimplePojo)about.getResult()).getAge()).isEqualTo(4);
    about.updatePojo().change("age").to(ageBeforeUpdate).execute();
    assertThat(((SimplePojo)about.getResult()).getAge()).isEqualTo(ageBeforeUpdate);

    List<SimplePojo> childsOfStefan = Collections.singletonList(SimplePojo.builder()
                                                                          .name("maxi")
                                                                          .age(999)
                                                                          .build());

    about.updatePojo().change("complexList").to(childsOfStefan).execute();

    Answer answerMaxi = askAPojo.about("list[name==stefan]/list[name==maxi]");
    assertThat(((SimplePojo)answerMaxi.getResult()).getAge()).isEqualTo(999);
  }

}
