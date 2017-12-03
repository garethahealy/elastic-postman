/*
 * #%L
 * GarethHealy :: Elastic Postman :: Scraper
 * %%
 * Copyright (C) 2013 - 2017 Gareth Healy
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
package com.garethahealy.elasticpostman.scraper.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Header;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.garethahealy.elasticpostman.scraper.processors.RegExSplitProcessor;
import com.google.common.collect.Iterators;

import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.mail.util.MimeMessageParser;
import org.apache.commons.mail.util.MimeMessageUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailContent {

    private static final Logger LOG = LoggerFactory.getLogger(EmailContent.class);

    private final String raw;
    private String from;
    private String subject;
    private String content;
    private Collection<String> contentIds;
    private DateTime sentDate;
    private Map<String, String> headers;

    public EmailContent(String raw) {
        this.raw = raw;
    }

    public void parse() throws Exception {
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage message = MimeMessageUtils.createMimeMessage(session, raw);
        MimeMessageParser mimeMessageParser = new MimeMessageParser(message);
        MimeMessageParser parsed = mimeMessageParser.parse();

        this.from = parsed.getFrom();
        this.subject = parsed.getSubject();
        this.content = parsed.getPlainContent();
        this.contentIds = parsed.getContentIds();
        this.sentDate = new DateTime(parsed.getMimeMessage().getSentDate());
        this.headers = new HashMap<String, String>();

        @SuppressWarnings("unchecked")
        EnumerationIterator it = new EnumerationIterator(parsed.getMimeMessage().getAllHeaders());
        while (it.hasNext()) {
            Object current = it.next();
            if (current instanceof Header) {
                Header header = (Header)current;
                if (includeHeader(header.getName())) {
                    headers.put(header.getName(), sanatizeValue(header.getName(), header.getValue()));
                }
            }
        }

    }

    private Boolean includeHeader(String header) {
        return !header.startsWith("From ");
    }

    private String sanatizeValue(String header, String value) {
        if (header.equalsIgnoreCase("X-List-Received-Date") || header.equalsIgnoreCase("Date")) {
            //DateFormat examples:
            //X-List-Received-Date:     Wed, 11 May 2016 10:49:36 -0000
            //Date:     Wed, 11 May 2016 10:49:36 -0000
            //Date:     Wed, 11 May 2016 10:49:36 -0000 (EDT)

            //Some dates have trailing spaces, so trim all to be safe
            value = value.trim();

            DateTime parsed = tryParseDate("EEE, dd MMM YYYY HH:mm:ss Z", value, false);
            if (parsed == null) {
                parsed = tryParseDate("EEE, dd MMM YYYY HH:mm:ss Z' ('zzz')'", value, true);
                
                if (parsed == null) {
                    //Special case - Wed, 11 May 2016 10:49:36 -0000 (SGT)
                    //Joda does not like the SGT bit, so remove and try again
                    parsed = tryParseDate("EEE, dd MMM YYYY HH:mm:ss Z", value.substring(0, value.length() - 6), true);
                }
            }

            if (parsed != null) {
                value = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").print(parsed);
            }
        }

        return value;
    }

    private DateTime tryParseDate(String pattern, String value, Boolean isLogException) {
        DateTime parsed = null;

        try {
            //http://www.joda.org/joda-time/apidocs/org/joda/time/format/DateTimeFormat.html
            parsed = DateTimeFormat.forPattern(pattern).parseDateTime(value);
        } catch (UnsupportedOperationException ex) {
            if (isLogException) {
                LOG.error(ex.toString());
            }
        } catch (IllegalArgumentException ex) {
            if (isLogException) {
                LOG.error(ex.toString());
            }
        }

        return parsed;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("from", from);
        map.put("subject", subject);
        map.put("content", content);
        map.put("contentIds", contentIds);
        map.put("sentDate", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").print(sentDate));
        map.put("headers", headers);

        return map;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public Collection<String> getContentIds() {
        return contentIds;
    }

    public DateTime getSentDate() {
        return sentDate;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int gethashCode() {
        //NOTE: method only exists because camel didnt like calling hashCode directly
        return hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("raw", raw)
            .append("from", from)
            .append("subject", subject)
            .append("content", content)
            .append("contentIds", contentIds)
            .append("sentDate", sentDate)
            .append("headers", headers)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmailContent that = (EmailContent)o;

        return new EqualsBuilder()
            .append(raw, that.raw)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(raw)
            .toHashCode();
    }
}
