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
/**
<p>
Provides interfaces for topic map objects;  the topic map API for all Ontopia
topic map implementations. In particular, this package enables topic map applications to be written without any dependencies on the repository-specific backend(s) used for managing the topic map data.</p>
<p> In addition, this package provides interfaces for some objects used to manage topic maps. </p>
<p>
This package contains no implementations of these interfaces; all
implementations are found as separate packages with the
<tt>net.ontopia.topicmaps.impl</tt> prefix. Classes are included for exceptions thrown by violations of the integrity of a topic map.
</p>
<h3>Package Specification</h3>
<p>The interfaces in this package provide an API for interacting with a topic map, covering the core topic map concepts, including: topic map; topic; association; occurrence; scope and type. (These terms are normally used as topic map terms, rather than with any other meaning, throughout Ontopia product documentation.) These interfaces provide facilities for creating and maintaining the detailed structure of a topic map.</p>
<p>This package provides a topic map store interface which enables the repository-specific details of establishing and maintaining a topic map (eg in a database)  to be hidden from applications using the topic map. Similarly, repository-independent and format-independent interfaces are provided for reading and writing topic maps in serialized interchange format (such as XML).</p>
<h3>Related Documentation</h3>
<p>The purpose and scope of this package is explained further in the Ontopia Engine Developers Guide.
</p>
<h3>Class and Interface Summary</h3>
<h4>Topic map object interfaces</h4>
<p> TMObjectIF is the common interface for all topic map objects, that is, for objects representing a topic map, topic, topic name, variant name, association, association role, or occurrence.</p>
<p>The interfaces for topic map objects are: TopicMapIF, TopicIF, TopicNameIF, VariantNameIF, AssociationIF, AssociationRoleIF, or OccurrenceIF. All these extend TMObjectIF.</p>
<h4>Topic map object properties</h4>
<p>TypedIF is implemented by topic map objects that have a single type. TypedIF is extended by  AssociationIF, AssociationRoleIF, and OccurrenceIF.</p>
<p>ScopedIF is implemented by topic map objects that have scope. ScopedIF is extended by AssociationIF, TopicNameIF, VariantNameIF, and OccurrenceIF.</p>
<h4>Topic map store interfaces</h4>
<p>These are interfaces to make utility functions on a topic map store independent of the repository in which it is actually held. TopicMapStoreIF represents the connexion between a topic map and a repository. </p>
<p>TopicMapBuilderIF and TopicMapFactoryIF provide support for creating topic map object structures. TopicMapFactoryIF allows you to
create topic map objects in a way that is independent of the specific
implementation you use. TopicMapBuilderIF is implemented by a helper object that wraps a factory in
order to provide a more convenient way of building topic map structures.(This
simplifies the factory interface, which has to be implemented for each repository backend, at the expense of adding an extra interface, but one that needs
only be implemented once.) </p>
<p>TopicMapImporterIF, TopicMapReaderIF, and TopicMapWriterIF provide support for obtaining and sending topic maps outside the store. </p>
<h4>Exception Classes</h4>
<p> Classes are provided in this package which support a uniform naming convention for exceptions thrown as a result of the violation of generic topic map properties. These are  TopicMapViolationException, UniquenessViolationException and ConstraintViolationException (which both extend TopicMapViolationException), and PolicyViolationException (which extends OntopiaRuntimeException in the net.ontopia.utils package).</p>
*/

package net.ontopia.topicmaps.core;
