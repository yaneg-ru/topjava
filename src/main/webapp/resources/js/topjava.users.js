// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                        "asc"
                    ]
                ]
            })
        }
    );

    $('.activeUser').on('change', function () { // on change of state
        var id = $(this).parent().parent().attr("id");
        var enabled = false;

        if (this.checked) // if changed state is "CHECKED"
        {
            enabled = true;
        }

        $.ajax({
            url: context.ajaxUrl + "?id=" + id + "&enabled=" + enabled,
            type: "PUT"
        }).done(function () {
            updateTable();
            successNoty("Update enabled status");
        });
    })

});