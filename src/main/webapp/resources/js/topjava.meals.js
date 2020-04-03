// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/profile/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "dsc"
                    ]
                ]
            })
        }
    );
});

function updateTableWithFilter() {
    var startDate = $('input[name="startDate"]').val();
    var endDate = $('input[name="endDate"]').val();
    var startTime = $('input[name="startTime"]').val();
    var endTime = $('input[name="endTime"]').val();
    var filter = "filter/?startDate=" + startDate + "&startTime=" + startTime + "&endDate=" + endDate + "&endTime=" + endTime;
    if (filter == "filter/?startDate=&startTime=&endDate=&endTime=") {
        filter = "";
    }
    $.get(context.ajaxUrl + filter, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
    successNoty("Filtered");
}

function clearFilter() {
    $('input[name="startDate"]').val("");
    $('input[name="endDate"]').val("");
    $('input[name="startTime"]').val("");
    $('input[name="endTime"]').val("");
    $.get(context.ajaxUrl, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
    successNoty("Clear filter");
}

function saveMeal() {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateTableWithFilter();
        successNoty("Saved");
    });
}