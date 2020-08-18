

var name;
var email;
var gmail;
var gname;


function onSignIn(googleUser) {
  var profile = googleUser.getBasicProfile();
  
  window.gmail = profile.getEmail();
  window.gname = profile.getName();
  signUp();
   /*console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
   console.log('Name: ' + profile.getName());
   console.log('Image URL: ' + profile.getImageUrl());
   console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
   */
  var id_token = googleUser.getAuthResponse().id_token;
  localStorage.setItem("googleToken",id_token);
}



function signUp()
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            get_login_response(this);
        }
    };
    xhttp.open("POST", "/Lapkart/RegisterGoogleUSer", true);
    xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xhttp.send("email="+gmail+"&name="+gname);
    
}

function login()
{
    email = document.getElementById("email").value;
    var pass = document.getElementById("pass").value;
    
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
    if (this.readyState === 4 && this.status === 200) {
        get_login_response(this);
    }
    };
    xhttp.open("POST", "/Lapkart/Login", true);
    xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xhttp.send("email="+email+"&pass="+pass);
}

function get_login_response(obj)
{
    var data = JSON.parse(obj.responseText);
    if(data.tokens[0].status === "successfull")
    {
        var token = data.tokens[0].token;
        var userid = data.tokens[0].userid;
        localStorage.setItem("token",token);
        localStorage.setItem("userid",userid);
        localStorage.setItem("email",email);
        window.open(document.referrer,"_self");
    }
    else
    {
        var ele = document.getElementById("login-feedback");
        ele.setAttribute("class","invalid-feedback d-block");
        ele.innerHTML = "Invalid Email or Password";
    }
}

