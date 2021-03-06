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

package net.ontopia.topicmaps.core;


/**
 * PUBLIC: Thrown when an object is attempted removed, but cannot.</p>
 *
 * Extends ConstraintViolationException but does not provide additional
 * methods; this is because the purpose is just to provide a different
 * exception, to allow API users to handle them differently.</p>
 */

public class NotRemovableException extends ConstraintViolationException {

  public NotRemovableException(Throwable e) {
    super(e);
  }

  public NotRemovableException(String message) {
    super(message);
  }

  public NotRemovableException(String message, Throwable cause) {
    super(message, cause);
  }
  
}





