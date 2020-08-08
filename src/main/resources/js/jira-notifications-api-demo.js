AJS.toInit(function($) {

  AJS.$('#is-sending').val('');

  AJS.$('#notification-groups').auiSelect2();

  AJS.$('#notification-groups-value').val(
    AJS.$('#notification-groups').val());

  AJS.$('#notification-groups').change(function () {
    AJS.$('#notification-groups-value').val(
      AJS.$('#notification-groups').val());
  });
});