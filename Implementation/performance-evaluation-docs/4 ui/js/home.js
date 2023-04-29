$(document).ready(function () {
     var hosts;
   $.ajax({
    'url': "../properties/app.json",
    'dataType': "json",
    'success': function (data) {
      hosts=data;
       var book_id = [];
    loadAll();
    function loadAll() {
        $.ajax({
            url: "http://"+hosts.orders_host+"/orders/cart/list",
            type: "POST",
            contentType: "application/json",
            datatype: "json",
            data: JSON.stringify({
                emailid: sessionStorage.getItem("email")
            }),
            success: function (data) {
                book_id = [];
                for (var i = 0; i < data.length; i++) {
                    book_id.push(data[i].bookid);
                }
            }
        }).done(function () {
            $.ajax({
                url: "http://"+hosts.books_host+"/books/list",
                type: "GET",
                contentType: "application/json",
                datatype: "json",
                success: function (data) {
                    $("#contents").html("");

                    for (var i = 0; i < data.length; i++) {
                        var btn = "";

                        if (book_id.includes(data[i].bookid)) {
                            btn = '<button  class="btn btn-success" type="button"><span class="glyphicon glyphicon-ok"></span></button>';
                        } else {
                            btn = '<button  class="btn btn-primary add_cart" type="button">Add to Cart</button>';
                        }
                        var option = "";
                        for (var j = 1; j <= data[i].availability; j++) {
                            option += "<option>" + j + "</option>";
                        }
                        $("#contents").append(`<div class="col-md-3" style="margin-bottom:15px"><div class="col-md-12" style="text-align:center;border:1px solid;border-radius:20px 20px;padding-top:15px;padding-bottom:15px;">
                     <p class="bid" style="display:none;">` + data[i].bookid + `</p>
<div class="col-md-12" style="background-color:gray;padding:15px;color:white;text-align:center;"><h3>` + data[i].bookname + `</h3></div>
                    <p style="padding-top:96px">by <span style="font-weight:bold">` + data[i].author + `</span></p>
                   
                    <p style="color:red;font-weight:bold">Price: $ <span class="price">` + data[i].price + `</span> </p>
                    <p>` + data[i].availability + ` available</p>
                    <p> Qty: <select class="qty">` + option + `</select></p>
                    ` + btn + `
                    </div></div>`);
                    }

                }
            });
        }).done(function(){
            
        });
    }
    $(document).on("click", ".add_cart", function () {
        $.ajax({
            url: "http://"+hosts.orders_host+"/orders/cart/add",
            type: "POST",
            contentType: "application/json",
            datatype: "json",
            data: JSON.stringify({
                emailid: sessionStorage.getItem("email"),
                bookid: $(this).parent().find(".bid").text(),
                quantity: Number($(this).parent().find(".qty").val()),
                price: Number($(this).parent().find(".price").text())
            }),
            success: function (data) {
//                alert("akk");
                loadAll();
            }
        });
//      alert($(this).parent().find(".bid").text()); 
//      alert($(this).parent().find(".qty").val()); 
//      alert($(this).parent().find(".price").text()); 
    });
    }
});
   
});

