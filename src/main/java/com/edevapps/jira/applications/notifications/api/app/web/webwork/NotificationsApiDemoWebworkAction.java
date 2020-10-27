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

package com.edevapps.jira.applications.notifications.api.app.web.webwork;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.message.I18nResolver;
import com.edevapps.jira.applications.notifications.api.Notification;
import com.edevapps.jira.applications.notifications.api.NotificationsService;
import com.edevapps.jira.applications.notifications.api.app.web.dto.SelectItemDto;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class NotificationsApiDemoWebworkAction extends JiraWebActionSupport {

  private static final long serialVersionUID = 199458478654094725L;
  private static final Logger log = LoggerFactory.getLogger(NotificationsApiDemoWebworkAction.class);
  private static final String DEFAULT_HOME_PAGE_URL = "/secure/JiraNotificationsApiDemo!default.jspa";
  private static final String NOTIFICATION_GROUPS_PAR = "notificationGroupsValue";
  private static final String NOTIFICATION_NAME_PAR = "notificationName";
  private static final String NOTIFICATION_MESSAGE_PAR = "notificationMessage";
  private static final String DELIMITER = ",";
  public static final String EMPTY_STRING = "";
  private static final String NULL_VAL = "null";
  private static final String MESSAGE_VALUE_NOT_EMPTY_RES = "message-is-not-be-empty.message";
  private String notificationName;
  private String notificationMessage;
  private List<SelectItemDto> notificationGroups;
  private String error;
  private String message;
  private final NotificationsService notificationsService;
  private final I18nResolver i18nResolver;

  @Inject
  public NotificationsApiDemoWebworkAction(
      @ComponentImport NotificationsService notificationsService,
      @ComponentImport I18nResolver i18nResolver) {
    this.notificationsService = notificationsService;
    this.i18nResolver = i18nResolver;
  }

  public String getNotificationName() {
    return this.notificationName;
  }

  public String getNotificationMessage() {
    return this.notificationMessage;
  }

  public List<SelectItemDto> getNotificationGroups() {
    return this.notificationGroups;
  }

  public String getMessage() {
    return this.message;
  }

  public String getError() {
    return this.error;
  }

  public String doDefault() {
    try {
      loadContent();
    } catch (Exception ex) {
      this.error = ex.getMessage();
      log.error(this.error);
    }
    return SUCCESS;
  }

  protected String doExecute() {
    try {
      loadContent();
      if (!isEmpty(this.notificationMessage)) {
        for (String group : getValues(getRequestParameter(NOTIFICATION_GROUPS_PAR))) {
          this.notificationsService.sendToNotificationGroup(group,
              new Notification(requireNonNullOrEmpty(this.notificationName, NOTIFICATION_NAME_PAR),
                  this.notificationMessage));
        }
        return returnCompleteWithInlineRedirect(DEFAULT_HOME_PAGE_URL);
      }

      this.message = this.i18nResolver.getText(MESSAGE_VALUE_NOT_EMPTY_RES);
    } catch (Exception ex) {
      this.error = ex.getMessage();
      log.error(this.error);
    }
    return SUCCESS;
  }

  private void loadContent() {
    Set<String> selectedGroups = new HashSet<>();
    String notificationGroupsValue = tryGetRequestParameter(NOTIFICATION_GROUPS_PAR);
    if (notificationGroupsValue != null) {
      selectedGroups.addAll(getValues(notificationGroupsValue));
    }

    this.notificationGroups = this.notificationsService.getNotificationGroupNames().stream()
        .map(groupName ->
            new SelectItemDto(groupName, groupName, selectedGroups.contains(groupName)))
        .collect(Collectors.toList());
    this.notificationName = tryGetRequestParameter(NOTIFICATION_NAME_PAR);
    this.notificationMessage = tryGetRequestParameter(NOTIFICATION_MESSAGE_PAR);
  }

  private String getRequestParameter(String name) {
    String parameter = this.getHttpRequest().getParameter(name);
    if (parameter == null) {
      throw new IllegalStateException("Unknown request parameter '" + name + "'.");
    } else {
      return parameter;
    }
  }

  private String tryGetRequestParameter(String name) {
    String parameter = this.getHttpRequest().getParameter(name);
    return !isEmpty(name) && !NULL_VAL.equals(name) ? parameter : EMPTY_STRING;
  }

  private List<String> getValues(String value) {
    return isEmpty(value) ? Collections.emptyList()
        : Arrays.stream(value.split(DELIMITER)).collect(Collectors.toList());
  }

  private String requireNonNullOrEmpty(String argument, String name)
      throws IllegalArgumentException {
    if (argument != null && !argument.equals(EMPTY_STRING)) {
      return argument;
    } else {
      throw new IllegalArgumentException(String.format("The %s is not be null or empty.", name));
    }
  }

  private boolean isEmpty(String value) {
    return value == null || value.equals(EMPTY_STRING);
  }
}