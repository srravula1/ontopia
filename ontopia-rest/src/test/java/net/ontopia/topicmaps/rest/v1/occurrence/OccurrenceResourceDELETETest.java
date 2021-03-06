/*
 * #!
 * Ontopia Rest
 * #-
 * Copyright (C) 2001 - 2017 The Ontopia Project
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

package net.ontopia.topicmaps.rest.v1.occurrence;

import net.ontopia.topicmaps.rest.exceptions.OntopiaRestErrors;
import net.ontopia.topicmaps.rest.model.Occurrence;
import net.ontopia.topicmaps.rest.v1.AbstractV1ResourceTest;
import org.junit.Test;

public class OccurrenceResourceDELETETest extends AbstractV1ResourceTest {
	
	public OccurrenceResourceDELETETest() {
		super(OCCURRENCES_LTM, "occurrences");
	}

	@Test
	public void deleteOccurrence() {
		delete("2", Occurrence.class);
		assertGetFails("2", OntopiaRestErrors.MANDATORY_ATTRIBUTE_IS_NULL);
	}

	@Test
	public void deleteInvalidOccurrence() {
		assertDeleteFails("1", OntopiaRestErrors.MANDATORY_ATTRIBUTE_IS_WRONG_TYPE);
	}

	@Test
	public void deleteUnexistingOccurrence() {
		assertDeleteFails("unexisting_occurrence_id", OntopiaRestErrors.MANDATORY_ATTRIBUTE_IS_NULL);
	}
}
