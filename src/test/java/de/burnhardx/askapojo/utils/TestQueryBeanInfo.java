package de.burnhardx.askapojo.utils;

import static com.google.common.truth.Truth.assertThat;

import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

import org.junit.Test;

import de.burnhardx.askapojo.testdata.AnyPojo;


/**
 * Test cases for {@link BeanInformation}.
 * 
 * @author burnhardx
 */
public class TestQueryBeanInfo
{

  /**
   * Asserts that {@link BeanInformation} returns a {@link PropertyDescriptor} by a given field name.
   */
  @Test
  public void returnsMatchingPropertyDescriptorByFieldName()
  {
    BeanInformation underTest = BeanInformation.of(AnyPojo.class);
    assertThat(underTest.getPropertyDescriptor("noSetter").getWriteMethod()).isNull();
    assertThat(underTest.getPropertyDescriptor("complexList")
                        .getReadMethod()
                        .getName()).isEqualTo("getComplexList");

    assertThat(underTest.getPropertyDescriptor("notExist")).isNull();
  }

  /**
   * Asserts that {@link BeanInformation} returns a {@link MethodDescriptor} by a given field name.
   */
  @Test
  public void returnsMatchingMethodDescriptorByMethodName()
  {
    BeanInformation underTest = BeanInformation.of(AnyPojo.class);
    assertThat(underTest.getMethodDescriptor("uuid").getName()).isEqualTo("uuid");
    assertThat(underTest.getMethodDescriptor("notThere")).isNull();
  }
}
