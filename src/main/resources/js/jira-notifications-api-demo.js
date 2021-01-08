AJS.toInit(function($) {

  AJS.$('#is-sending').val('');

  AJS.$('#notification-group').auiSelect2();
  AJS.$('#notification-user-group').auiSelect2();

  AJS.$('#notification-group').change(function () {
    AJS.$('#notification-group-value').val(
      AJS.$('#notification-group').val());
  });

  AJS.$('#notification-user').change(function () {
    AJS.$('#notification-user-value').val(
      AJS.$('#notification-user').val());
  });

    AJS.$('#notification-user-group').change(function () {
      AJS.$('#notification-user-group-value').val(
        AJS.$('#notification-user-group').val());
    });

  AJS.$('#notification-groups-send-button').click(function (event) {
    window.onbeforeunload = null;
    AJS.$('#action').val('NOTIFICATION_GROUP_SEND');
  });

  AJS.$('#notification-user-send-button').click(function (event) {
    window.onbeforeunload = null;
    AJS.$('#action').val('NOTIFICATION_USER_SEND');
  });

  AJS.$('#notification-user-group-send-button').click(function (event) {
    window.onbeforeunload = null;
    AJS.$('#action').val('NOTIFICATION_USER_GROUP_SEND');
  });

  autocomplete('#notification-user', '/plugins/servlet/jiranotifications.action?action=GET_SEARCH_SELECT_USERS');
});

function autocomplete(selector, url) {
  AJS.$(selector)
    .on('keydown', function(event) {
      if (event.keyCode === AJS.$.ui.keyCode.TAB && AJS.$(this).autocomplete('instance').menu.active ) {
        event.preventDefault();
      }
    })
    .autocomplete({
      source: function(request, response) {
        AJS.$.getJSON(url, { selector: extractLast(request.term) }, response);
      },
      search: function() {
        var term = extractLast(this.value);
        if (term.length < 2) {
          return false;
        }
      },
      focus: function() {
        return false;
      },
      select: function(event, ui) {
        this.value = ui.item.value;
        return false;
      }
    });
}