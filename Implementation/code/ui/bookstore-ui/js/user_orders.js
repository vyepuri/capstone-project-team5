$(document).ready(function () {
    var hosts;
    $.ajax({
        'url': "../properties/app.json",
        'dataType': "json",
        'success': function (data) {
            hosts = data;
            $.ajax({
                url: "http://" + hosts.orders_host + "/orders/myorders",
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
                                            
                                            <td>` + data[i].bookname + `</td>
                                            <td>` + data[i].quantity + `</td>
                                            <td>` + data[i].price + `</td>
                                            <td>` + data[i].paymentdate + `</td>
                                            <td>` + data[i].paymenttype + `</td>
                                            
                                            <td>` + data[i].deliverystatus + `</td>
                                            
                                        </tr>`);
                    }
                }
            });
        }
    });

});