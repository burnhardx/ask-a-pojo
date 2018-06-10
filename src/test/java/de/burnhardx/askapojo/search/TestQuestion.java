package de.burnhardx.askapojo.search;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;


/**
 * Test cases for {@link Question}.
 * 
 * @author burnhardx
 */
public class TestQuestion
{

  /**
   * Asserts that {@link Question} can be build by given string like attr*=anyValue.
   */
  @Test
  public void createsQuestionsFromString()
  {
    Question withoutConditions = Question.fromString("anyQuestion");
    assertThat(withoutConditions.getConditions()).isEmpty();

    Question withCondition = Question.fromString("attribute[mustBe==val]");
    assertThat(withCondition.getConditions().size()).isEqualTo(1);
    Condition condition = withCondition.getConditions().get(0);
    assertThat(condition.getAttribute()).isEqualTo("mustBe");
    assertThat(condition.getOperator()).isEqualTo(Operator.EQUAL);
    assertThat(condition.getValue()).isEqualTo("val");

    Question withMultipleConditions = Question.fromString("multi[attr1==val1][attr2|=val2]][attr3*=val3][attr4$=val4]");
    assertThat(withMultipleConditions.getConditions().size()).isEqualTo(4);
    assertThat(withMultipleConditions.getTarget()).isEqualTo("multi");

    condition = withMultipleConditions.getConditions().get(0);
    assertThat(condition.getAttribute()).isEqualTo("attr1");
    assertThat(condition.getOperator()).isEqualTo(Operator.EQUAL);
    assertThat(condition.getValue()).isEqualTo("val1");

    condition = withMultipleConditions.getConditions().get(1);
    assertThat(condition.getAttribute()).isEqualTo("attr2");
    assertThat(condition.getOperator()).isEqualTo(Operator.STARTS_WITH);
    assertThat(condition.getValue()).isEqualTo("val2");

    condition = withMultipleConditions.getConditions().get(2);
    assertThat(condition.getAttribute()).isEqualTo("attr3");
    assertThat(condition.getOperator()).isEqualTo(Operator.CONTAINS);
    assertThat(condition.getValue()).isEqualTo("val3");

    condition = withMultipleConditions.getConditions().get(3);
    assertThat(condition.getAttribute()).isEqualTo("attr4");
    assertThat(condition.getOperator()).isEqualTo(Operator.ENDS_WITH);
    assertThat(condition.getValue()).isEqualTo("val4");
  }

}
