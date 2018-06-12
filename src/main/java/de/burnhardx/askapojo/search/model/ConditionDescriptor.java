package de.burnhardx.askapojo.search.model;

import java.beans.PropertyDescriptor;

import lombok.Builder;
import lombok.Value;


/**
 * Wraps a {@link Condition} with the belonging {@link PropertyDescriptor}.
 * 
 * @author burnhardx
 */
@Value
@Builder
public class ConditionDescriptor
{

  private Condition condition;

  private PropertyDescriptor descriptor;

}
