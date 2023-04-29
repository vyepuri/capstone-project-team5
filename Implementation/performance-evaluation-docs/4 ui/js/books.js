

$(document).ready(function () {
    var hosts;
    $.ajax({
        'url': "../properties/app.json",
        'dataType': "json",
        'success': function (data) {
            hosts = data;
            loadtable();
            $("#add").click(function () {
                $.ajax({
                    url: "http://" + hosts.books_host + "/books/add",
                    type: "POST",
                    contentType: "application/json",

                    data: JSON.stringify({
                        email: sessionStorage.getItem("email"),
                        bookname: $("#name").val(),
                        author: $("#author").val(),
                        category: $("#category").val(),
                        price: Number($("#price").val()),
                        availability: Number($("#avail").val())
                    }),
                    success: function (data) {
                        $(".form-control").val("");
                        loadtable();
                    }, error: function () {
                        alert("Unauthorized User");
                    }
                });
            });
            $(".can").click(function () {
                $(".form-control").val("");
            });
            function loadtable() {
                $.ajax({
                    url: "http://" + hosts.books_host + "/books/list",
                    type: "GET",
                    contentType: "application/json",
                    datatype: "json",
                    success: function (data) {
                        $("#books_tbody").html("");
                        console.log(data);
                        for (var i = 0; i < data.length; i++) {
                            $("#books_tbody").append(`<tr>
                                            <td style='display:none'>` + data[i].bookid + `</td>
                                            <td>` + data[i].bookname + `</td>
                                            <td>` + data[i].author + `</td>
                                            <td>` + data[i].category + `</td>
                                            <td>` + data[i].price + `</td>
                                            <td>` + data[i].availability + `</td>
                                        </tr>`);
                        }
                    }
                });
            }
        }
    });

});



