var isLogged = false;

function Navbar_login()
{
    var token = localStorage.getItem("token");
    var userid = localStorage.getItem("userid");
    if(token != null && userid != null){
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var data = JSON.parse(this.responseText);
            if(data.token_info[0].status == "successful")
            {
                window.isLogged = true;
                var name = data.token_info[0].name;
                var ele = document.getElementById("user-tag");
                ele.setAttribute("class","nav-item dropdown");
                ele.innerHTML = "";
                var a_tag = document.createElement("a");
                a_tag.setAttribute("class","nav-link dropdown-toggle");
                a_tag.setAttribute("href","#");
                a_tag.setAttribute("role","button");
                a_tag.setAttribute("data-toggle","dropdown");
                a_tag.setAttribute("aria-haspopup","true");
                a_tag.setAttribute("aria-expanded","false");
                a_tag.setAttribute("id","user_dropdown");
                a_tag.innerHTML = name;
                
                var div = document.createElement("div");
                div.setAttribute("class","dropdown-menu");
                div.setAttribute("aria-labelledby","user_dropdown");
                
                var prof = document.createElement("a");
                prof.setAttribute("class","dropdown-item");
                prof.setAttribute("href","#");
                prof.innerHTML = "Profile";
                var order = document.createElement("a");
                order.setAttribute("class","dropdown-item");
                order.setAttribute("href","MyOrders.html");
                order.innerHTML = "Order";
                
                var logout = document.createElement("a");
                logout.setAttribute("class","dropdown-item");
                logout.setAttribute("href","#");
                logout.setAttribute("onclick","logout()");
                logout.innerHTML = "Logout";
                
                div.appendChild(prof);
                div.appendChild(order);
                div.appendChild(logout);
                
                a_tag.appendChild(div);
                
                ele.appendChild(a_tag);
                
                document.getElementById("cartItemCount").innerHTML = data.token_info[0].cart_count;
            }
            else
            {
                window.isLogged = false;
                var ele = document.getElementById("user-tag");
                ele.setAttribute("class","nav-item");
                ele.innerHTML = "";
                var login = document.createElement("a");
                login.setAttribute("class","nav-link");
                login.setAttribute("href","login.html");
                login.innerHTML = "Login & Register"
                ele.appendChild(login);
            }
        }
        };
        xhttp.open("POST", "/Lapkart/TokenVerify", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("userid="+userid+"&token="+token);
    }
}

function logout()
{
    var token = localStorage.getItem("token");
    var userid = localStorage.getItem("userid");
    if(token != "" && userid != ""){
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var data = this.responseText;
            if(data == "successful")
            {
                window.isLogged = false;
                localStorage.removeItem("token");
                localStorage.removeItem("userid");
                window.open("Home.html","_self");
            }
            else
            {
                window.isLogged = true;
                alert("Logout Fail");
            }
        }
        };
        xhttp.open("POST", "/Lapkart/Logout", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("userid="+userid+"&token="+token);
    }
}

function logincheck()
{
    var token = localStorage.getItem("token");
    var userid = localStorage.getItem("userid");
    if(token != null && userid != null){
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var data = JSON.parse(this.responseText);
            if(data.token_info[0].status == "successful")
            {
                window.isLogged = true;
                window.open("Home.html","_self");
            }
            else
            {
                window.isLogged = false;
                localStorage.removeItem("token");
                localStorage.removeItem("userid");
            }
        }
        };
        xhttp.open("POST", "/Lapkart/TokenVerify", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("userid="+userid+"&token="+token);
    }
}

function myOrder()
{
    var token = localStorage.getItem("token");
    var userid = localStorage.getItem("userid");
    if(token != null && userid != null)
    {
        window.open("/Lapkart/MyOrder?token="+token+"&userid="+userid);
    }
}