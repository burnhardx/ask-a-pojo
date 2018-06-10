package de.burnhardx.askapojo.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Value;


/**
 * Model that represents one query of the given query string.
 * 
 * @author burnhardx
 */
@Value
public class Question
{

  private String target;

  private List<Condition> conditions;

  public static Question fromString(String query)
  {
    if (!query.contains("["))
    {
      return new Question(query, new ArrayList<>());
    }
    else if (query.contains("]["))
    {
      Pattern r = Pattern.compile("\\[[a-zA-Z=*|$0-9]*\\]");
      Matcher matcher = r.matcher(query);
      List<Condition> conditions = new ArrayList<>();
      while (matcher.find())
      {
        conditions.add(Condition.fromString(matcher.group().substring(1)));
      }
      return new Question(query.split("\\[")[0], conditions);
    }
    else
    {
      String split[] = query.split("\\[");
      return new Question(split[0], Arrays.asList(Condition.fromString(split[1])));
    }
  }



}
