/*
 * #!
 * Ontopia Webed
 * #-
 * Copyright (C) 2001 - 2013 The Ontopia Project
 * #-
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * !#
 */

package net.ontopia.topicmaps.webed.impl.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;
import net.ontopia.topicmaps.webed.core.ActionIF;
import net.ontopia.topicmaps.webed.core.ActionParametersIF;
import net.ontopia.topicmaps.webed.core.ActionResponseIF;
import net.ontopia.topicmaps.webed.core.ActionRuntimeException;
import net.ontopia.topicmaps.webed.impl.basic.ActionParameters;
import net.ontopia.topicmaps.webed.impl.basic.ActionResponse;
import net.ontopia.utils.ontojsp.FakeServletRequest;
import net.ontopia.utils.ontojsp.FakeServletResponse;

public class TestSetForwardAction extends TestCase {

  public TestSetForwardAction(String name) {
    super(name);
  }

  // tests

  public void testNoParameters() throws ActionRuntimeException {
    ActionIF forward = new SetForwardAction();
    ActionParametersIF params = makeParameters(Collections.EMPTY_LIST);
    ActionResponseIF response = makeResponse();
    forward.perform(params, response);
    assertNull("forward must be null", response.getForward());
  }

  public void testOnlyRequestParam() throws ActionRuntimeException {
    ActionIF forward = new SetForwardAction();
    ActionParametersIF params = makeParameters(Collections.EMPTY_LIST, "foo.jsp");
    ActionResponseIF response = makeResponse();
    forward.perform(params, response);
    assertTrue("forward was wrong", response.getForward().equals("foo.jsp"));
  }

  public void testOneParam() throws ActionRuntimeException {
    ActionIF forward = new SetForwardAction();
    ActionParametersIF params = makeParameters("foo.jsp", "bar.jsp");
    ActionResponseIF response = makeResponse();
    forward.perform(params, response);
    assertTrue("forward was wrong", response.getForward().equals("foo.jsp"));
  }

  public void testEchoingRequestParams() throws ActionRuntimeException {
    ActionIF forward = new SetForwardAction();
    ActionParametersIF params = makeParameters(makeList("foo.jsp", "gurr"),
                                               "bar.jsp");
    ActionResponseIF response = makeResponse();
    forward.perform(params, response);
    
    assertTrue("forward was wrong", response.getForward().equals("foo.jsp"));
    assertTrue("wrong number of parameters set",
               response.getParameters().size() == 1);
    assertTrue("parameter gurr not set",
               response.getParameters().containsKey("gurr"));
    assertTrue("parameter gurr not null",
               response.getParameters().get("gurr") == null);
  }

  public void testEchoingRequestParams2() throws ActionRuntimeException {
    ActionIF forward = new SetForwardAction();
    ActionParametersIF params = makeParameters(makeList("foo.jsp", "gurr", "bah"),
                                               "bar.jsp");
    ActionResponseIF response = makeResponse();
    forward.perform(params, response);
    
    assertTrue("forward was wrong", response.getForward().equals("foo.jsp"));
    assertTrue("wrong number of parameters set",
               response.getParameters().size() == 2);
    assertTrue("parameter gurr not set",
               response.getParameters().containsKey("gurr"));
    assertTrue("parameter gurr not null",
               response.getParameters().get("gurr") == null);
    assertTrue("parameter bah not set",
               response.getParameters().containsKey("bah"));
    assertTrue("parameter bah not null",
               response.getParameters().get("bah") == null);
  }
  
  public void testBadParamType() throws ActionRuntimeException {
    ActionIF forward = new SetForwardAction();
    ActionParametersIF params = makeParameters(makeList(this), "bar.jsp");
    ActionResponseIF response = makeResponse();

    try {
      forward.perform(params, response);
      fail("action accepted parameter of bad type");
    } catch (ActionRuntimeException e) {
    }
  }

  public void testBadParamType2() throws ActionRuntimeException {
    ActionIF forward = new SetForwardAction();
    ActionParametersIF params = makeParameters(makeList(this, this), "bar.jsp");
    ActionResponseIF response = makeResponse();

    try {
      forward.perform(params, response);
      fail("action accepted parameter of bad type");
    } catch (ActionRuntimeException e) {
    }
  }
  
  // helper methods

  private List makeList(Object param1) {
    param1 = Collections.singleton(param1); // params are lists of collections...
    return Collections.singletonList(param1);
  }

  private List makeList(Object param1, Object param2) {
    List list = new ArrayList(2);
    list.add(Collections.singleton(param1));
    list.add(Collections.singleton(param2));
    return list;
  }

  private List makeList(Object param1, Object param2, Object param3) {
    List list = new ArrayList(3);
    list.add(Collections.singleton(param1));
    list.add(Collections.singleton(param2));
    list.add(Collections.singleton(param3));
    return list;
  }
  
  private ActionParametersIF makeParameters(Object param1, String value) {
    if (param1 instanceof List)
      return makeParameters((List) param1, value);

    return makeParameters(makeList(param1), value);
  }
  
  private ActionParametersIF makeParameters(List params) {
    return makeParameters(params, null);
  }

  private ActionParametersIF makeParameters(List params, String value) {
    String[] values = {value};
    return new ActionParameters("boo1", values, null, params, null, null);
  }
  
  private ActionResponseIF makeResponse() {
    javax.servlet.http.HttpServletRequest request = new FakeServletRequest();
    javax.servlet.http.HttpServletResponse response = new FakeServletResponse();
    return new ActionResponse(request, response);
  }
  
}
