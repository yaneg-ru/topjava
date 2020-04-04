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
        var enabled = this.checked;
        $.ajax({
            url: context.ajaxUrl + "?id=" + id + "&enabled=" + enabled,
            type: "PUT"
        }).success(function () {
            if (enabled) {
                $(this).parent().parent().css("color", "#212529")
            } else {
                $(this).parent().parent().css("color", "darkgray")
            }
            successNoty("Update enabled status");
        });
    })

});