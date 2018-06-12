package de.burnhardx.askapojo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.burnhardx.askapojo.change.model.TestChangeValues;
import de.burnhardx.askapojo.search.TestQuestion;
import de.burnhardx.askapojo.utils.TestAskableInformation;
import de.burnhardx.askapojo.utils.TestBeanInformation;


@RunWith(Suite.class)
@Suite.SuiteClasses({TestAskAPojo.class, TestChangeValues.class, TestAskableInformation.class,
                     TestBeanInformation.class, TestQuestion.class})
public class AskAPojoSuite
{

}
