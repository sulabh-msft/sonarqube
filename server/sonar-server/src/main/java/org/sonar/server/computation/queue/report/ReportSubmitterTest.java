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
package org.sonar.server.computation.queue.report;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.core.permission.GlobalPermissions;
import org.sonar.db.ce.CeTaskTypes;
import org.sonar.db.component.ComponentDto;
import org.sonar.server.component.ComponentService;
import org.sonar.server.component.NewComponent;
import org.sonar.server.computation.ReportFiles;
import org.sonar.server.computation.queue.CeQueue;
import org.sonar.server.computation.queue.CeQueueImpl;
import org.sonar.server.computation.queue.TaskSubmission;
import org.sonar.server.computation.queue.TestTaskSubmission;
import org.sonar.server.computation.queue.report.ReportSubmitter;
import org.sonar.server.tester.UserSessionRule;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportSubmitterTest {

  @Rule
  public UserSessionRule userSession = UserSessionRule.standalone();

  CeQueue queue = Mockito.mock(CeQueueImpl.class);
  ReportFiles reportFiles = Mockito.mock(ReportFiles.class);
  ComponentService componentService = Mockito.mock(ComponentService.class);
  ReportSubmitter underTest = new ReportSubmitter(queue, userSession, reportFiles, componentService);

  @Test
  public void submit_a_report_on_existing_project() {
    Mockito.when(queue.prepareSubmit()).thenReturn(new TestTaskSubmission("TASK_1"));
    userSession.setGlobalPermissions(GlobalPermissions.SCAN_EXECUTION);
    Mockito.when(componentService.getNullableByKey("MY_PROJECT")).thenReturn(new ComponentDto().setUuid("P1"));

    underTest.submit("MY_PROJECT", null, "My Project", IOUtils.toInputStream("{binary}"));

    Mockito.verify(queue).submit(Matchers.argThat(new TypeSafeMatcher<TaskSubmission>() {
      @Override
      protected boolean matchesSafely(TaskSubmission submit) {
        return submit.getType().equals(CeTaskTypes.REPORT) && submit.getComponentUuid().equals("P1") &&
            submit.getUuid().equals("TASK_1");
      }

      @Override
      public void describeTo(Description description) {

      }
    }));
  }

  @Test
  public void provision_project_if_does_not_exist() throws Exception {
    Mockito.when(queue.prepareSubmit()).thenReturn(new TestTaskSubmission("TASK_1"));
    userSession.setGlobalPermissions(GlobalPermissions.SCAN_EXECUTION, GlobalPermissions.PROVISIONING);
    Mockito.when(componentService.getNullableByKey("MY_PROJECT")).thenReturn(null);
    Mockito.when(componentService.create(Matchers.any(NewComponent.class))).thenReturn(new ComponentDto().setUuid("P1"));

    underTest.submit("MY_PROJECT", null, "My Project", IOUtils.toInputStream("{binary}"));

    Mockito.verify(queue).submit(Matchers.argThat(new TypeSafeMatcher<TaskSubmission>() {
      @Override
      protected boolean matchesSafely(TaskSubmission submit) {
        return submit.getType().equals(CeTaskTypes.REPORT) && submit.getComponentUuid().equals("P1") &&
            submit.getUuid().equals("TASK_1");
      }

      @Override
      public void describeTo(Description description) {

      }
    }));

  }
}
