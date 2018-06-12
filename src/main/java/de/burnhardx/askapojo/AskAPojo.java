package de.burnhardx.askapojo;

import java.util.Arrays;
import java.util.List;

import de.burnhardx.askapojo.search.AnswersQuestionForField;
import de.burnhardx.askapojo.search.AnswersQuestionForMethod;
import de.burnhardx.askapojo.search.HasAnswers;
import de.burnhardx.askapojo.search.model.Answer;
import de.burnhardx.askapojo.search.model.Question;
import de.burnhardx.askapojo.utils.AskableInformation;
import lombok.extern.slf4j.Slf4j;


/**
 * Wrapper for Object questions.
 * 
 * @author burnhardx
 */
@Slf4j
public class AskAPojo
{

  private Object source;

  private final AskableInformation information;

  /**
   * Initialized with an object to search in.
   * 
   * @param source
   */
  public AskAPojo(Object source)
  {
    super();
    this.source = source;
    this.information = AskableInformation.of(source.getClass());
  }

  /**
   * Returns an answer
   * 
   * @param question
   * @return
   */
  public Answer about(String question)
  {
    List<String> questions = Arrays.asList(question.split("/"));
    AskAPojo searchIn = this;
    Answer answer = null;
    for ( String query : questions )
    {
      answer = searchIn.ask(Question.fromString(query));
      if (answer.getResult() != null)
      {
        searchIn = new AskAPojo(answer.getResult());
      }
    }
    return answer;
  }

  private Answer ask(Question question)
  {
    HasAnswers hasAnswers = information.getFields().containsKey(question.getTarget())
      ? new AnswersQuestionForField() : new AnswersQuestionForMethod();
    try
    {
      return hasAnswers.ask(question, information, source);
    }
    catch (ReflectiveOperationException e)
    {
      log.error("reflective operation on pojo failed {}", e);
    }
    return new Answer(null);
  }

}
