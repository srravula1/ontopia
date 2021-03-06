/*
 * #!
 * Ontopia Navigator
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

package net.ontopia.topicmaps.nav.utils.comparators;

import java.util.Comparator;
import net.ontopia.topicmaps.entry.TopicMapReferenceIF;
import org.apache.commons.lang3.StringUtils;

/**
 * INTERNAL: A Comparator for ordering TopicMapReferences lexically. It
 * compares the titles of references case-insensitively.
 */
public class TopicMapReferenceComparator implements Comparator<TopicMapReferenceIF> {

  /**
   * INTERNAL: Compares two TopicMapReferenceIFs.
   */
  @Override
  public int compare(TopicMapReferenceIF tmr1, TopicMapReferenceIF tmr2) {
    String title1 = tmr1.getTitle();
    String title2 = tmr2.getTitle();
    return StringUtils.compareIgnoreCase(title1, title2);
  }
  
}





