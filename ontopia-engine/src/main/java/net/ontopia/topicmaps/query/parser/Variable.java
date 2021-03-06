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

package net.ontopia.topicmaps.query.parser;

/**
 * INTERNAL: Used to represent variable references in tolog queries.
 */
public class Variable {
  protected String name; // does NOT include the initial '$'
  
  public Variable(String name) {
    this.name = name.substring(1);
  }

  public String getName() {
    return name;
  }

  /// Object

  @Override
  public String toString() {
    return "$" + name;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Variable &&
      name.equals(((Variable) obj).name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
  
}
