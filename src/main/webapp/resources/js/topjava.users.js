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
        var tr = $(this).parent().parent();
        var enabled = this.checked;
        $.ajax({
            url: context.ajaxUrl + "?id=" + id + "&enabled=" + enabled,
            type: "PUT",
            success: function () {
                tr.attr("data-userEnabled", enabled);
                successNoty("Update enabled status");
            },
            error: function () {
                this.checked = !enabled;
            }
        })
    })
});