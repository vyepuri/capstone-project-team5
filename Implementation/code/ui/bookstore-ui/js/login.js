$(document).ready(function () {
    var hosts;
   $.ajax({
    'url': "../properties/app.json",
    'dataType': "json",
    'success': function (data) {
      hosts=data;
       $(document).on('keypress', function (e) {
        if (e.which == 13) {
            login();
        }
    });
    $("#login_btn").click(function () {
        login();
    });
    function login() {
        if ($("#mail").val() === "" || $("#pass").val() === "") {
            alert("Email or Password should not be empty");
        } else {
            $.ajax({
                url: "http://"+hosts.user_host+"/user/login",
                type: "POST",
                contentType: "application/json",
                datatype: "json",
                data: JSON.stringify({
                    email: $("#mail").val(),
                    password: $("#pass").val()})
                ,
                success: function (data) {

                    sessionStorage.setItem("email", data.email);
                    sessionStorage.setItem("name", data.name);
                    if (data.role === "ADMIN") {
                        window.location.href = "admin-home.html";
                    } else {
                        window.location.href = "home.html";
                    }

                },
                error: function (e) {
                    alert("Wrong Username or Password");
                }
            });
        }
    }

    }
});
   
});


