/*
 * #%L
 * GarethHealy :: Elastic Postman :: Scraper
 * %%
 * Copyright (C) 2013 - 2016 Gareth Healy
 * %%
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
 * #L%
 */
package com.garethahealy.elasticpostman.scraper.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.garethahealy.elasticpostman.scraper.entities.EmailContent;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegExSplitProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(RegExSplitProcessor.class);
    private static final Pattern PATTERN = Pattern.compile("(^From\\s\\w*@redhat.com\\s.*)", Pattern.MULTILINE);

    public void process(Exchange exchange) throws Exception {
        String text = exchange.getIn().getBody(String.class);

        List<EmailContent> answer = new ArrayList<EmailContent>();

        //NOTE: Probably a better way to do this via regex, but i aint a regex guru
        Matcher matches = PATTERN.matcher(text);

        //1. Find what i think is the start of an email (i.e.: From foo@redhat.com)
        //2. Store the start index
        //3. Move to the next start of an email
        //4. Substring last index to new index
        //5. Parse content

        int lastIndex = 0;
        while (matches.find()) {
            int index = matches.start();
            if (index > 0) {
                LOG.debug("start / end === {} / {}", lastIndex, index);
                EmailContent email = new EmailContent(text.substring(lastIndex, index));
                email.parse();

                answer.add(email);
            }

            lastIndex = index;
        }

        EmailContent email = new EmailContent(text.substring(lastIndex, text.length()));
        email.parse();

        answer.add(email);

        exchange.getIn().setBody(answer);
    }
}
