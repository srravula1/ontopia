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

package net.ontopia.utils.ontojsp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.utils.URIUtils;
import net.ontopia.xml.DefaultXMLReaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * INTERNAL: Class that reads a jsp file and builds a JSPTree from it.
 */
public class JSPPageReader {

  // initialization of logging facility
  private static Logger logger =
    LoggerFactory.getLogger(JSPPageReader.class.getName());

  protected URL file;

  /**
   * Constructor that accepts a filename as argument.
   */
  public JSPPageReader(File source) throws java.net.MalformedURLException {
    this.file = URIUtils.toURL(source);
  }

  public JSPPageReader(URL file) {
    this.file = file;
  }

  /**
   * Creates an XMLReader object.
   */
  public XMLReader createXMLReader() {
    try {
      // Set parser features
      XMLReader reader = DefaultXMLReaderFactory.createXMLReader();
      reader.setFeature("http://xml.org/sax/features/namespaces", false);
      // cxrfactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
      reader.setFeature("http://xml.org/sax/features/validation", false);
      // Create new parser object
      return reader;
    } catch (SAXException e) {
      throw new OntopiaRuntimeException(e);
    }
  }

  /**
   * Reads a jsp file and creates a tree of JSPTreeNodeIF objects.
   *
   * @return net.ontopia.utils.ontojsp.JSPTreeNodeIF
   */
  public JSPTreeNodeIF read()
    throws IOException, SAXException {
    return read(TaglibTagFactory.TAGPOOLING_DEFAULT);
  }

  public JSPTreeNodeIF read(boolean useTagPooling)
    throws IOException, SAXException {
    JSPContentHandler handler = new JSPContentHandler(useTagPooling);
    XMLReader parser = createXMLReader();
    handler.register(parser);
    logger.debug("Read in JSP from " + file);
    try (InputStream stream = file.openStream()) {
      parser.parse(new InputSource(stream));
    }
    return handler.getRootNode();
  }
  
}
