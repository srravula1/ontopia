/*
 * #!
 * Ontopia Engine
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

package net.ontopia.topicmaps.query.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseLocatorPredicateTest extends AbstractPredicateTest {
  
  public BaseLocatorPredicateTest(String name) {
    super(name);
  }

  @Override
  public void tearDown() {
    closeStore();
  }
  
  /// tests
  
  public void testCompletelyOpen() throws InvalidQueryException, IOException {
    load("family2.ltm");

    List matches = new ArrayList();
    addMatch(matches, "BASE", topicmap.getStore().getBaseAddress().getAddress());
    
    verifyQuery(matches, "base-locator($BASE)?");
  }
  
  public void testCompletelyOpenNoValue() throws InvalidQueryException, IOException {
    makeEmpty();

    // NOTE: have to skip rdbms test, because rdbms topic map store
    // gets base locator implicitly.
    if (topicmap.getStore().getBaseAddress() == null) {
      List matches = new ArrayList();    
      verifyQuery(matches, "base-locator($BASE)?");
    }
  }

  public void testWithSpecificValueFalse() throws InvalidQueryException, IOException {
    load("jill.xtm");

    findNothing(OPT_TYPECHECK_OFF +
                "base-locator(jillstm)?");
  }

  public void testWithSpecificValueFalse2() throws InvalidQueryException, IOException{
    load("jill.xtm");

    List matches = new ArrayList();    
    verifyQuery(matches, "base-locator(\"jillstm\")?");
  }

  public void testWithSpecificValueFalse3() throws InvalidQueryException, IOException{
    makeEmpty();

    List matches = new ArrayList();    
    verifyQuery(matches, "base-locator(\"file://tst.xtm\")?");
  }
  
  public void testWithSpecificValueTrue() throws InvalidQueryException, IOException {
    load("jill.xtm");

    String base = topicmap.getStore().getBaseAddress().getAddress();
    List matches = new ArrayList();
    matches.add(new HashMap());
    
    verifyQuery(matches, "base-locator(\"" + base + "\")?");
  }
  
}
