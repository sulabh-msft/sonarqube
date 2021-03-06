/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.search;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import org.junit.AfterClass;
import org.junit.Test;
import org.sonar.process.LogbackHelper;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchLoggingTest {

  SearchLogging underTest = new SearchLogging();

  @AfterClass
  public static void resetLogback() throws Exception {
    new LogbackHelper().resetFromXml("/logback-test.xml");
  }

  @Test
  public void log_to_console() {
    LoggerContext ctx = underTest.configure();

    Logger root = ctx.getLogger(Logger.ROOT_LOGGER_NAME);
    Appender appender = root.getAppender("CONSOLE");
    assertThat(appender).isInstanceOf(ConsoleAppender.class);
  }
}
