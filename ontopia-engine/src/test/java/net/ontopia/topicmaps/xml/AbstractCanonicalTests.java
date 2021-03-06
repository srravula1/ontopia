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

package net.ontopia.topicmaps.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import net.ontopia.topicmaps.core.TopicMapStoreFactoryIF;
import net.ontopia.topicmaps.impl.basic.InMemoryStoreFactory;
import net.ontopia.utils.TestFileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public abstract class AbstractCanonicalTests {

  // --- Canonicalization type methods

  /**
   * INTERNAL: Returns directory to search for files in.
   */
  protected String getFileDirectory() {
    return "in";
  }

  /**
   * INTERNAL: Makes the name of the outfile (without path) given the
   * name of the infile.
   */
  protected String getOutFilename(String infile) {
    return infile;
  }

  /**
   * INTERNAL: Performs the actual canonicalization.
   */
  protected abstract void canonicalize(URL infile, File outfile)
    throws IOException;

  /**
   * INTERNAL: Returns the store factory to be used.
   */
  protected TopicMapStoreFactoryIF getStoreFactory() {
    return new InMemoryStoreFactory();
  }
  
  // --- Test case class

    protected String base;
    protected URL inputFile;
    protected String filename;
    protected String _testdataDirectory;

    @Test
    public void testFile() throws IOException {
      TestFileUtils.verifyDirectory(base, "out");
      
      // setup canonicalization filenames
      File out = new File(base + File.separator + "out" + File.separator + getOutFilename(filename));

      // produce canonical output
      canonicalize(inputFile, out);
      
      // compare results
      URL baselineURL = new URL(inputFile, "../baseline/" + getOutFilename(filename));
      try (InputStream baselineIn = baselineURL.openStream(); FileInputStream in = new FileInputStream(out)) {
        Assert.assertTrue("test file " + filename + " canonicalized wrongly",
                IOUtils.contentEquals(in, baselineIn));
      }
    }
}
