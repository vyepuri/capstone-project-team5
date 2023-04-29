
$(document).ready(function () {
     var hosts;
   $.ajax({
    'url': "../properties/app.json",
    'dataType': "json",
    'success': function (data) {
      hosts=data;
      loadtable();
//    $("#add").click(function () {
//        $.ajax({
//            url: "http://192.168.10.39:7001/books/add",
//            type: "POST",
//            contentType: "application/json",
//
//            data: JSON.stringify({
//                email: sessionStorage.getItem("email"),
//                bookname: $("#name").val(),
//                author: $("#author").val(),
//                category: $("#category").val(),
//                price: Number($("#price").val()),
//                availability: Number($("#avail").val())
//            }),
//            success: function (data) {
//                $(".form-control").val("");
//                loadtable();
//            }, error: function () {
//                alert("Unauthorized User");
//            }
//        });
//    });
//    $(".can").click(function () {
//        $(".form-control").val("");
//    });
    function loadtable() {
        $.ajax({
            url: "http://"+hosts.orders_host+"/orders/all",
            type: "POST",
            contentType: "application/json",
            datatype: "json",
            data: JSON.stringify({
                emailid: sessionStorage.getItem("email")
            }),
            success: function (data) {
                $("#orders_tbody").html("");
                console.log(data);
                for (var i = 0; i < data.length; i++) {
                    $("#orders_tbody").append(`<tr>
                                            <td style='display:none'>` + data[i].orderid + `</td>
                                            <td>` + data[i].bookname + `</td>
                                            <td>` + data[i].quantity + `</td>
                                            <td>` + data[i].paymenttype + `</td>
                                            <td>` + data[i].name + `</td>
                                            <td>` + data[i].mobile + `</td>
                                            <td>` + data[i].address + `</td>
                                            <td>` + data[i].deliverystatus + `</td>
                                            <td><span  class="glyphicon glyphicon-pencil up"></span></td>
                                        </tr>`);
                }
            }
        });
    }
    }
});
    
});