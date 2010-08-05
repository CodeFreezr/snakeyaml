/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yaml.snakeyaml;

import java.io.StringReader;

import junit.framework.TestCase;

import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;

public class YamlParseTest extends TestCase {

    public void testParse() {
        SnakeYaml yaml = new SnakeYaml();
        Event e = null;
        int counter = 0;
        for (Event event : yaml.parse(new StringReader("abc: 56"))) {
            if (e == null) {
                assertTrue(event instanceof StreamStartEvent);
            }
            e = event;
            counter++;
        }
        assertTrue(e instanceof StreamEndEvent);
        assertEquals(8, counter);
    }

    public void testParseManyDocuments() {
        SnakeYaml yaml = new SnakeYaml();
        Event e = null;
        int counter = 0;
        for (Event event : yaml.parse(new StringReader("abc: 56\n---\n4\n---\nqwe\n"))) {
            if (e == null) {
                assertTrue(event instanceof StreamStartEvent);
            }
            e = event;
            counter++;
        }
        assertTrue(e instanceof StreamEndEvent);
        assertEquals(14, counter);
    }
}
