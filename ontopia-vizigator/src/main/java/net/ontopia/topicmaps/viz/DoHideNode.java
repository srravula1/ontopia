/*
 * #!
 * Ontopia Vizigator
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
package net.ontopia.topicmaps.viz;

public class DoHideNode implements RecoveryObjectIF {
  private VizController controller;
  private NodeRecoveryObjectIF recreator;

  public DoHideNode(VizController controller, NodeRecoveryObjectIF recreator) {
    this.controller = controller;
    this.recreator = recreator;
  }

  @Override
  public void execute(TopicMapView view) {
    TMAbstractNode node = recreator.recoverNode(view);
    controller.hideNode(node);
  }
}
