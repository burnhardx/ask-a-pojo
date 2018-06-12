package de.burnhardx.askapojo.change.model;

import static com.google.common.truth.Truth.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import de.burnhardx.askapojo.AskAPojo;
import de.burnhardx.askapojo.annotations.AskMe;
import de.burnhardx.askapojo.annotations.AskMeAs;
import de.burnhardx.askapojo.change.ChangeValues;
import de.burnhardx.askapojo.exceptions.EmptyResultInAnswer;
import de.burnhardx.askapojo.search.model.Answer;
import de.burnhardx.askapojo.testdata.Comment;
import de.burnhardx.askapojo.testdata.SimplePojo;
import de.burnhardx.askapojo.testdata.SimplePojo.SimplePojoBuilder;


/**
 * Test cases for {@link ChangeValues}.
 * 
 * @author burnhardx
 */
public class TestChangeValues
{

  private static final AskAPojo askAPojo = new AskAPojo(SimplePojo.testSet());

  private final SimplePojoBuilder pojo = SimplePojo.builder();

  /**
   * Asserts that {@link ChangeValues} can be build in a very fluent way.
   * 
   * @throws EmptyResultInAnswer
   */
  @Test
  public void canBuildInFluentWay() throws EmptyResultInAnswer
  {
    Answer answer = askAPojo.about("list[name==stefan]");
    ChangeValues underTest = new ChangeValues(answer);

    underTest.change("age").to(9).change("name").to("heino");

    assertThat(((ChangeField)underTest.getChanges().get(0)).getFieldName()).isEqualTo("age");
    assertThat((Integer)((ChangeField)underTest.getChanges().get(0)).getValue()).isEqualTo(9);

    assertThat(((ChangeField)underTest.getChanges().get(1)).getFieldName()).isEqualTo("name");
    assertThat((String)((ChangeField)underTest.getChanges().get(1)).getValue()).isEqualTo("heino");
  }

  /**
   * Asserts that a {@link Answer} with no result can not be changed.
   * 
   * @throws EmptyResultInAnswer
   */
  @Test(expected = EmptyResultInAnswer.class)
  public void canNotChangeValuesOnAnswerWithNoResult() throws EmptyResultInAnswer
  {
    ChangeValues underTest = new ChangeValues(new Answer(null));
    underTest.change("anyField");
  }

  /**
   * Asserts that an empty {@link Answer} can not be changed.
   * 
   * @throws EmptyResultInAnswer
   */
  @Test(expected = EmptyResultInAnswer.class)
  public void canNotChangeValuesOnEmptyAnswer() throws EmptyResultInAnswer
  {
    ChangeValues underTest = new ChangeValues(null);
    underTest.change("anyField");
  }

  /**
   * Asserts that simple single changes on an {@link Answer} are possible.
   * 
   * @throws EmptyResultInAnswer
   * @throws ReflectiveOperationException
   */
  @Test
  public void canChangeValuesInPojos() throws EmptyResultInAnswer, ReflectiveOperationException
  {
    ChangeValues underTest = new ChangeValues(askAPojo.about("list[name==stefan]"));

    Map<String, SimplePojo> theWho = Map.of("roger",
                                            pojo.name("Roger Daltrey").build(),
                                            "peter",
                                            pojo.name("Peter Townshend").build(),
                                            "keith",
                                            pojo.name("Keith Moon").build(),
                                            "john",
                                            pojo.name("John Entwistle").build());

    underTest.change("age").to(78).change("name").to("peterchen").change("complexMap").to(theWho).execute();

    SimplePojo resultAfterUpdate = (SimplePojo)askAPojo.about("list[name==peterchen]").getResult();
    assertThat(resultAfterUpdate).isNotNull();
    assertThat(resultAfterUpdate.getAge()).isEqualTo(78);

    for ( Entry<String, SimplePojo> member : theWho.entrySet() )
    {
      assertThat(resultAfterUpdate.getComplexMap().get(member.getKey())).isEqualTo(member.getValue());
    }
  }

  /**
   * Asserts that simple changes on {@link Answer} are also possible when the {@link AskMe} or {@link AskMeAs}
   * name is used.
   * 
   * @throws EmptyResultInAnswer
   * @throws ReflectiveOperationException
   */
  @Test
  public void canChangeValuesByTheirAskMeName() throws EmptyResultInAnswer, ReflectiveOperationException
  {
    ChangeValues underTest = new ChangeValues(askAPojo.about("list[name==martin]"));

    List<SimplePojo> ramones = List.of(pojo.name("Joey Ramone").build(),
                                       pojo.name("Johnny Ramone").build(),
                                       pojo.name("Dee Dee Ramone").build(),
                                       pojo.name("Tommy Ramone").build());

    underTest.change("list").to(ramones).execute();

    for ( SimplePojo ramone : ramones )
    {
      SimplePojo loadedRamone = askAPojo.about("list[name==martin]/anotherListGetter[name=="
                                               + ramone.getName() + "]")
                                        .getResultAs(SimplePojo.class);
      assertThat(loadedRamone.getName()).isEqualTo(ramone.getName());
    }
  }

  /**
   * Asserts that methods that are annotated with {@link AskMe} or {@link AskMeAs} can be invoked.
   * 
   * @throws EmptyResultInAnswer
   * @throws ReflectiveOperationException
   */
  @Test
  public void canInvokeVoidMethodsWithGivenValues() throws ReflectiveOperationException, EmptyResultInAnswer
  {
    ChangeValues underTest = new ChangeValues(askAPojo.about("list[name==martin]"));

    String title = "This test is a mess!!!!";
    Date publishingDate = new Date();
    underTest.call("addComment").with(title, publishingDate).execute();

    assertThat(underTest.getAnswer().getResultAs(SimplePojo.class).getComments()).isNotEmpty();

    Comment storedComment = askAPojo.about("list[name==martin]/comments[title==" + title + "]")
                                    .getResultAs(Comment.class);
    assertThat(storedComment.getTitle()).isEqualTo(title);
    assertThat(storedComment.getId()).isNotEmpty();
  }
}
