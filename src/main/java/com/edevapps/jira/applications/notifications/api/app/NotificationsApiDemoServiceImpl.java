/*
 *     Copyright (c) 2020, The Eduard Burenkov. All rights reserved. http://edevapps.com
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.edevapps.jira.applications.notifications.api.app;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.sal.api.lifecycle.LifecycleAware;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

@ExportAsService({LifecycleAware.class})
@Named(NotificationsApiDemoServiceImpl.SERVICE_NAME)
public class NotificationsApiDemoServiceImpl implements LifecycleAware, DisposableBean {

  private static final Logger log = LoggerFactory.getLogger(NotificationsApiDemoServiceImpl.class);

  public static final String SERVICE_NAME = "notificationsApiDemoService";

  public NotificationsApiDemoServiceImpl() {
  }

  public void onStart() {
  }

  private void stop() {
  }

  public void onStop() {
    this.stop();
  }

  public void destroy() {
    this.stop();
  }
}