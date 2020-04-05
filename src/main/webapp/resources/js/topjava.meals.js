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

function getFilter() {
    var filter = "filter/?" + $('form[name="filter"]').serialize();

    if (filter == "filter/?startDate=&endDate=&startTime=&endTime=" || filter == "filter/?") {
        filter = "";
    }
    return filter;
}

function clearFilter() {
    //$('#dataFilter')[0].reset(); // не работает
    $("body").find('#dataFilter').find('input').val('');
    updateTableWithFilter();
    successNoty("Clear filter");
}