/*
 * #%L
 * GarethHealy :: Elastic Postman Scraper
 * %%
 * Copyright (C) 2013 - 2018 Gareth Healy
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

import java.net.URL;

import com.google.common.base.Strings;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RemoveAllHeadersExceptCamelHttpUriProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String camelHttpUri = exchange.getIn().getHeader(Exchange.HTTP_PATH, String.class);
        String camelFileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);

        exchange.getProperties().clear();
        exchange.getIn().getHeaders().clear();
        exchange.getIn().setHeader(Exchange.FILE_NAME, camelFileName);
        exchange.getIn().setHeader(Exchange.HTTP_PATH, camelHttpUri);
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "GET");
    }
}
