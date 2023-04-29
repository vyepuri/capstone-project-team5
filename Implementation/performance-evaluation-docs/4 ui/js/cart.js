$(document).ready(function () {
     var hosts;
   $.ajax({
    'url': "../properties/app.json",
    'dataType': "json",
    'success': function (data) {
      hosts=data;
        loadtable();
function loadtable() {
        $.ajax({
            url: "http://"+hosts.orders_host+"/orders/cart/list",
            type: "POST",
            contentType: "application/json",
            datatype: "json",
            data: JSON.stringify({
                emailid: sessionStorage.getItem("email")
            }),
            success: function (data) {
                $("#cart_tbody").html("");
                console.log(data);
                if(data!=="No carts found"){
                for (var i = 0; i < data.length; i++) {
                    $("#cart_tbody").append(`<tr>
                                            <td style='display:none' class="orderid">` + data[i].orderid + `</td>
                                            <td style='display:none' class="bookid">` + data[i].bookid + `</td>
                                            <td>` + data[i].bookname + `</td>
                                            <td>` + data[i].quantity + `</td>
                                            <td class="pri">` + data[i].price + `</td>
                                            <td><span class="glyphicon glyphicon-minus-sign remove" style="color:red"></span></td>
                                        </tr>`);
                }
            }
        }
        }).done(function(){
            var total=0;
            $(".pri").each(function(i,obj){
                total+=Number($(this).text());
            });
            $(".total").text(total);
        });
    }
    $(document).on("click", ".remove", function () {
        $.ajax({
            url: "http://"+hosts.orders_host+"/orders/cart/delete",
            type: "POST",
            contentType: "application/json",
            datatype: "json",
            data: JSON.stringify({
                emailid: sessionStorage.getItem("email"),
                orderid: $(this).parent().parent().find(".orderid").text(),
                
            }),
            success: function (data) {
//                alert("akk");
                loadtable();
            }
        });
        
//      alert($(this).parent().parent().find(".orderid").text()); 
//      alert($(this).parent().find(".qty").val()); 
//      alert($(this).parent().find(".price").text()); 
    });
    $(document).on("click", "#p_order", function () {
            alert("dsd");
            var date=new Date();
            var d=date.getDate()+"/"+date.getMonth()+1+"/"+date.getFullYear()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
            var orders='[';
            $("#cart_tbody tr").each(function(i){
                if(i!==0){
                    orders+=",";
                }
                    orders+='{"bookId":"'+$(this).find("td:eq(1)").text()+'",';
                    orders+='"quantity":"'+$(this).find("td:eq(3)").text()+'"}';
            });
            orders+="]";
            var d1={emailid: sessionStorage.getItem("email"),
                paymenttype: $("#type").val(),
                paymentdate:d,
                books:JSON.parse(orders)};
            console.log(d1);
            $.ajax({
            url: "http://"+hosts.orders_host+"/orders/cart/pay",
            type: "POST",
            contentType: "application/json",
            datatype: "json",
            data: JSON.stringify({
                emailid: sessionStorage.getItem("email"),
                paymenttype: $("#type").val(),
                paymentdate:d,
                books:JSON.parse(orders)
            }),
            success: function (data) {
                alert("order placed successfully");
                loadtable();
            },
            error:function (data) {
                alert("order placed successfully");
                loadtable();
            }
        });
        });
    }
});
  
});