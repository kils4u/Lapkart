var data;
var email_verified = false;
var p;

function load()
{
    logincheck();
}

function verify(check)
{
	var fname = document.getElementById("fname").value;
	var lname = document.getElementById("lname").value;
	var age = document.getElementById("age").value;
	var gender = document.getElementById("gender").value;
	var email = document.getElementById("email").value;
	var pass = document.getElementById("pass1").value;
        var pass2 = document.getElementById("pass2").value;
	var mob = "+" + document.getElementById("mob").value;
	var addr1 = document.getElementById("addr1").value;
	var addr2 = document.getElementById("addr2").value;
	var country = document.getElementById("country").value;
	var state = document.getElementById("state").value;
	var zip = document.getElementById("zip").value;
        
        if(!(validate_name(fname,document.getElementById("fname-feedback"))))
        {
            alert("enter fname");
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else if(!(validate_name(lname,document.getElementById("lname-feedback"))))
        {
            alert("enter lname");
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else if(!(validate_age(age,document.getElementById("age-feedback"))))
        {
            alert("enter age");
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else if(!pass_check(pass,document.getElementById("pass1-feedback")))
        {
            alert("enter pass");
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else if(!pass_match(pass2,document.getElementById("pass2-feedback")))
        {
            alert("password do not match");
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else if(!check_mob(mob,document.getElementById("mob-feedback")))
        {
            alert("enter mobile no");
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else if(!check_addr())
        {
            alert("enter address : " + addr1);
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else if(country == "")
        {
            alert("select country");
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else if(!check_state())
        {
            alert("select State");
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else if(!check_zip())
        {
            alert("Enter Zip code");
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else if(!email_verified)
        {
            alert("verify Email")
            var ele = document.getElementById("submit");
            ele.disabled = true;
            check.checked = false;
        }
        else
        {
            window.data = "fname=" + fname + "&";
            window.data += "lname=" + lname + "&";
            window.data += "age=" + age + "&";
            window.data += "gender=" + gender + "&";
            window.data += "email=" + email + "&";
            window.data += "pass=" + pass + "&";
            window.data += "mob=" + mob + "&";
            window.data += "addr1=" + addr1 + "&";
            window.data += "addr2=" + addr2 + "&";
            window.data += "country=" + window.country.countries[country].country + "&";
            window.data += "state=" + window.country.countries[country].states[state] + "&";
            window.data += "zip=" + zip;
            var ele = document.getElementById("submit");
            ele.disabled = false;
        }
}

function submit_form()
{
        var xhttp = new XMLHttpRequest();
  	xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var msg = this.responseText;
            if(msg == "successful")
            {
                window.open("/Lapkart/login.html","_self");
            }
    	}
  	};
  	xhttp.open("POST", "/Lapkart/Register", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
  	xhttp.send(window.data);
}

function validate_name(name,ele)
{
    len = name.length;
    if(len <= 2)
    {
        ele.setAttribute("class","invalid-feedback d-block");
        ele.innerHTML =  "Enter valid name";
        return false;
    }
    for(i=0;i<len;i++)
    {
        if (!((name.charCodeAt(i) >= 65 && name.charCodeAt(i) <= 90) ||(name.charCodeAt(i) >= 97 && name.charCodeAt(i) <= 122) || name.charCodeAt(i) == 46))
        {
            ele.setAttribute("class","invalid-feedback d-block");
            ele.innerHTML =  "Enter valid name";
            return false;
        }
    }
    ele.setAttribute("class","invalid-feedback");
    ele.innerHTML =  "";
    return true;
}

function validate_age(age,ele)
{
    len = age.length;
    if(len == 0)
    {
        ele.setAttribute("class","invalid-feedback d-block");
        ele.innerHTML =  "Enter valid age";
        return false;
    }
    for(i=0;i<len;i++)
    {
        if(age.charCodeAt(i)<48 || age.charCodeAt(i)>57)
        {
            ele.setAttribute("class","invalid-feedback d-block");
            ele.innerHTML =  "Enter valid Age";
            return false;
        }
    }
    if(!(age >= 10 && age <= 120))
    {
        ele.setAttribute("class","invalid-feedback d-block");
        ele.innerHTML =  "Enter Age between 10 - 120";
        return false
    }
    else
    {
        ele.setAttribute("class","invalid-feedback");
        ele.innerHTML =  ""
        return true;
    }
}

function check_mob(mob,ele)
{
	var len = mob.length;
	if(len > 13 || len < 13)
        {
            ele.setAttribute("class","invalid-feedback d-block");
            ele.innerHTML =  "mobile no must have 12 characters including country code";
            return false
        }
        else
        {
            var i = 1;
            for(i=1;i<len;i++)
            {
                if(!(mob.charCodeAt(i) >= 48 && mob.charCodeAt(i) <= 57))
                {
                    ele.setAttribute("class","invalid-feedback d-block");
                    ele.innerHTML = "mobile no must have only digits";
                    return false;
                }
            }
            if(i == len)
            {
                ele.setAttribute("class","invalid-feedback");
                ele.innerHTML = "";
                return true;
            }
            
        }
}

function pass_check(pass,ele)
{
	var len = pass.length;
	var cap = 0;
	var small = 0;
	var digit = 0;
	var schar = 0;
	if(len < 8)
        {
            ele.setAttribute("class","invalid-feedback d-block");
            ele.innerHTML = "Password must have minimum 8 characters";
            return false;
        }
	for(i=0;i<len;i++)
	{
		if(pass.charCodeAt(i) >= 48 && pass.charCodeAt(i) <= 57)
			digit++;
		else if(pass.charCodeAt(i) >= 65 && pass.charCodeAt(i) <= 90)
			cap++;
		else if(pass.charCodeAt(i) >= 97 && pass.charCodeAt(i) <= 122)
			small++;
		else
			schar++;
	}

	if(cap == 0 || small == 0 || digit == 0 || schar == 0)
        {
		ele.setAttribute("class","invalid-feedback d-block");
                ele.innerHTML = "Password must have atlst : 1 small letter, 1 capital letter, 1 special char, and 1 digit";
                return false
        }
	else
        {
            ele.setAttribute("class","invalid-feedback");
            ele.innerHTML ="";
            return true;
        }

}

function add_country()
{
    var ele = document.getElementById("country");
    var len = window.country.countries.length;
    for(i=0;i<len;i++)
    {
        var opt = document.createElement("option");
        opt.setAttribute("value",i);
        opt.innerHTML = window.country.countries[i].country;
        ele.appendChild(opt);
    }
}
function add_state()
{
    var c = document.getElementById("country").value;
    if(c == "")
    {
        var feed = document.getElementById("country-feedback");
        feed.setAttribute("class","invalid-feedback d-block");
        feed.innerHTML = "Select valid Country";
        return false
    }
    else
    {
        var len = window.country.countries[c].states.length;
        var ele = document.getElementById("state");
        ele.innerHTML = "";
        for(i=0;i<len;i++)
        {
            var opt = document.createElement("option");
            opt.setAttribute("value",i);
            opt.innerHTML = window.country.countries[c].states[i];
            ele.appendChild(opt);
        }
        var feed = document.getElementById("country-feedback");
        feed.setAttribute("class","invalid-feedback");
        feed.innerHTML = "";
        return true;
    }
}
function check_state()
{
    var val = document.getElementById("state").value;
    if(val == "")
    {
        var feed = document.getElementById("state-feedback");
        feed.setAttribute("class","invalid-feedback d-block");
        feed.innerHTML = "Select valid Seate";
        return false
    }
    else
    {
        var feed = document.getElementById("state-feedback");
        feed.setAttribute("class","invalid-feedback");
        feed.innerHTML = "";
        return true;
    }
}

function pass_match(pass2,ele)
{
        var pass1 = document.getElementById("pass1").value;
	var len = pass1.length;

	if(len != pass2.length)
        {
            ele.setAttribute("class","invalid-feedback d-block");
            ele.innerHTML = "Password Do not match";
            return false;
        }

	for(i=0;i<len;i++)
	{
		if(pass1.charAt(i) != pass2.charAt(i))
                {
                    ele.setAttribute("class","invalid-feedback d-block");
                    ele.innerHTML = "password do not match";
                    return false;
                }
	}
        ele.setAttribute("class","invalid-feedback");
        ele.innerHTML = "";
        return true;
}

function check_addr()
{
    var addr = document.getElementById("addr1").value;
    if(addr.length <= 2)
    {
        var ele = document.getElementById("addr1-feedback");
        ele.setAttribute("class","invalid-feedback d-block");
        ele.innerHTML = "address line-1 is must";
        return false;
    }
    else
    {
        var ele = document.getElementById("addr1-feedback");
        ele.setAttribute("class","invalid-feedback");
        ele.innerHTML = "";
        return true;
    }
}

function check_zip()
{
    var addr = document.getElementById("zip").value;
    if(addr.length <= 2)
    {
        var ele = document.getElementById("zip-feedback");
        ele.setAttribute("class","invalid-feedback d-block");
        ele.innerHTML = "zip is must";
        return false;
    }
    else
    {
        var ele = document.getElementById("zip-feedback");
        ele.setAttribute("class","invalid-feedback");
        ele.innerHTML = "";
        return true;
    }
}

function check_email(mail,ele)
{
        window.email_verified = false;
	if(mail.indexOf(".com") != (mail.length - 4) || mail.indexOf(".com") <= 0)
        {
            ele.setAttribute("class","invalid-feedback d-block");
            ele.innerHTML ="email must have \".com\" at the end";
            return false;
        }
	else if(mail.indexOf("@") <= 0)
        {
            ele.setAttribute("class","invalid-feedback d-block");
            ele.innerHTML ="email must have '@' symbol";
            return false;
        }
        else if(mail.indexOf("@") == mail.indexOf(".com") - 1)
        {
            ele.setAttribute("class","invalid-feedback d-block");
            ele.innerHTML ="Envalid Domain";
            return false;
        }
        else
        {
            window.p = new Promise(function(resolve,reject){
                var isExist;
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    isExist = this.responseText;
                     if(isExist == "notExist")
                    {
                        ele.setAttribute("class","invalid-feedback");
                        ele.innerHTML = "";
                        window.email_verified = true;
                        resolve(true);
                    }
                    else if(isExist == "Exist")
                    {
                        ele.setAttribute("class","invalid-feedback d-block");
                        ele.innerHTML = "Email already taken";
                        window.email_verified = false;
                        reject(new Error(false));
                    }
                }
                };
                xhttp.open("POST", "/Lapkart/check_exist_mail", true);
                xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
                xhttp.send("mail="+mail);
            });
        }
}