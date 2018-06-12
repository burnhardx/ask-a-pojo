package de.burnhardx.askapojo.testdata;

import java.util.Date;

import de.burnhardx.askapojo.annotations.AskMe;
import lombok.Builder;
import lombok.Value;


/**
 * Only for testing purposes.
 * 
 * @author burnhardx
 */
@Value
@Builder
@AskMe
public class Comment
{

  private String id;

  private String title;

  private Date date;
}
