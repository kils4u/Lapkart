var Total_Price = 0;

function Load_Amount()
{
    var token = localStorage.getItem("token");
    if(token != null)
    {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var resp = this.responseText;
            if(resp != "noData" && resp != null)
            {
                get_price(resp);
            }
        }
        };
        xhttp.open("POST", "/Lapkart/Cart", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("token="+token);
    }
}

function Load_Personal()
{
    var token = localStorage.getItem("token");
    var userid = localStorage.getItem("userid");
    if(token != null && userid != null)
    {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var resp = this.responseText;
            console.log(resp)
            if((resp != "fail" && resp != "Invalid_user") && resp != null)
            {
                get_personal(resp);
            }
        }
        };
        xhttp.open("POST", "/Lapkart/GetPersonal", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("token="+token + "&userid="+userid);
    }
}

function get_personal(response)
{
    var users = JSON.parse(response);
    var name = document.getElementById("name");
    name.innerHTML = users.user.personal.name;
    var email = document.getElementById("email");
    email.innerHTML = users.user.personal.email;
    var len = users.user.contact.length;
    if(len>0)
    {
        var ele = document.getElementById("Contact-Detail");
        for(i=0;i<len;i++)
        {
            ele.appendChild(add_contact(users.user.contact[i]));
            ele.appendChild(document.createElement("hr"));
        }
    }
    var btn1 = document.createElement("button");
    btn1.setAttribute("class","btn btn-primary");
    btn1.setAttribute("data-toggle","modal");
    btn1.setAttribute("data-target","#AddNewContact");
    btn1.innerHTML = "Add new";
    document.getElementById("Contact-Detail").appendChild(btn1);
        
    
    len = users.user.address.length;
    if(len>0)
    {
        var ele = document.getElementById("Address-Detail");
        for(i=0;i<len;i++)
        {
            ele.appendChild(add_addr(users.user.address[i]));
            ele.appendChild(document.createElement("hr"));
        }
    }
    
    var btn2 = document.createElement("button");
    btn2.setAttribute("class","btn btn-primary");
    btn2.setAttribute("data-toggle","modal");
    btn2.setAttribute("data-target","#AddNewAddress");
    btn2.innerHTML = "Add new";
    document.getElementById("Address-Detail").appendChild(btn2);
}

function add_contact(con)
{
    var div_fc = document.createElement("div");
    div_fc.setAttribute("class","form-check");

    var radio = document.createElement("input");
    radio.setAttribute("class","form-check-input");
    radio.setAttribute("type","radio");
    radio.setAttribute("name","contact");
    radio.setAttribute("value",con.mobid);
    radio.setAttribute("checked",true);
    
    var mob = document.createElement("h5");
    mob.setAttribute("class","card-title ml-2");
    mob.innerHTML = "Mob no : +" + con.mob;

    div_fc.appendChild(radio);
    div_fc.appendChild(mob);
    
    return div_fc;
}

function add_addr(addr)
{
    var div_fc = document.createElement("div");
    div_fc.setAttribute("class","form-check");
    
    var radio = document.createElement("input");
    radio.setAttribute("class","form-check-input");
    radio.setAttribute("type","radio");
    radio.setAttribute("name","address");
    radio.setAttribute("value",addr.addrid);
    radio.setAttribute("checked",true);
    
    var div = document.createElement("div");
    div.setAttribute("class","ml-2")
    
    var addr1 = document.createElement("h5");
    addr1.setAttribute("class","card-title");
    addr1.innerHTML = addr.addr1;
    
    var addr2 = document.createElement("h6");
    addr2.setAttribute("class","card-title");
    addr2.innerHTML = addr.addr2;
    
    var country = document.createElement("h6");
    country.setAttribute("class","card-title");
    country.innerHTML = addr.state + ", " + addr.country;
    
    var zip = document.createElement("h6");
    zip.setAttribute("class","card-title");
    zip.innerHTML = " zip : " + addr.zip;
    
    div.appendChild(addr1);
    div.appendChild(addr2);
    div.appendChild(country);
    div.appendChild(zip);
    
    div_fc.appendChild(radio);
    div_fc.appendChild(div);
    
    return div_fc;
}

function get_price(response)
{
    var cart = JSON.parse(response);
    var len = cart.items.length;
    if(len>0)
    {
        var price_tag = document.getElementById("price-tag");
        for(i=0;i<len;i++)
        {
            price_tag.appendChild(createPriceTag(cart.items[i], i+1))
        }
        var tot_li = document.createElement("li");
        tot_li.setAttribute("class","list-group-item");
        var tot_p = document.createElement("p");
        var tot_sp1 = document.createElement("span");
        tot_sp1.innerHTML = "Total Price : ";
        var tot_sp2 = document.createElement("span");
        tot_sp2.setAttribute("class","float-right")
        tot_sp2.innerHTML = window.Total_Price;
        tot_p.appendChild(tot_sp1);
        tot_p.appendChild(tot_sp2);
        tot_li.appendChild(tot_p);
        price_tag.appendChild(tot_li);
        document.getElementById("checkout").style.visibility = "visible";
    }
}


function createPriceTag(item, n)
{
    var list = document.createElement("li");
    list.setAttribute("class","list-group-item");
    var item_no = document.createElement("p");
    var item_sp1 = document.createElement("span");
    item_sp1.innerHTML = "Item No";
    var item_sp2 = document.createElement("span");
    item_sp2.setAttribute("class","float-right")
    item_sp2.innerHTML = n;
    item_no.appendChild(item_sp1);
    item_no.appendChild(item_sp2);
    var price = document.createElement("p");
    var price_sp1 = document.createElement("span");
    price_sp1.innerHTML = "Price";
    var price_sp2 = document.createElement("span");
    price_sp2.setAttribute("class","float-right")
    price_sp2.innerHTML = item.price + "&nbsp * &nbsp" + item.quantity ;
    price.appendChild(price_sp1);
    price.appendChild(price_sp2);
    list.appendChild(item_no);
    list.appendChild(price);
    window.Total_Price += item.price * item.quantity;
    return list;
}

function check_mob(mob,ele)
{
	var len = mob.length;
	if(len != 12)
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
    if(addr.length != 6)
    {
        var ele = document.getElementById("zip-feedback");
        ele.setAttribute("class","invalid-feedback d-block");
        ele.innerHTML = "6 - digit code";
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

function addNewContact()
{
    var userid = localStorage.getItem("userid");
    var token = localStorage.getItem("token");
    var mob = document.getElementById("mob").value;
    var mobfeed = document.getElementById("mob-feedback");
    if(!check_mob(mob,mobfeed));
    else
    {
        if(userid != null && token != null)
        {
            
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var resp = this.responseText;
                if(resp == "successful")
                {
                    location.reload();
                }
            }
            };
            xhttp.open("POST", "/Lapkart/AddNewContact", true);
            xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
            xhttp.send("token="+token + "&userid="+userid+"&mob="+mob);      
        }
    }
}

function place_order()
{
    var userid = localStorage.getItem("userid");
    var token = localStorage.getItem("token");
    var mobid = document.querySelector('input[name="contact"]:checked').value;
    var addrid = document.querySelector('input[name="address"]:checked').value;
    
    if(userid != null && token != null)
    {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var resp = this.responseText;
            if(resp != "fail")
            {
                window.open("/Lapkart/Payment?orderid="+resp+"&token="+token+"&userid="+userid,"_self");
            }
        }
        };
        xhttp.open("POST", "/Lapkart/MakeOrder", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("token="+token+"&userid="+userid+"&mobid="+mobid+"&addrid="+addrid);
    }
}


function addNewAddress()
{
    var userid = localStorage.getItem("userid");
    var token = localStorage.getItem("token");
    if(!check_addr());
    else if(!check_state());
    else if(!check_zip());
    else
    {
        if(userid != null && token != null)
        {
            var data = "";
            var addr1 = document.getElementById("addr1").value;
            var addr2 = document.getElementById("addr2").value;
            var country = document.getElementById("country").value;
            var state = document.getElementById("state").value;
            var zip = document.getElementById("zip").value;
            
            data = "addr1=" + addr1 + "&";
            data += "addr2=" + addr2 + "&";
            data += "country=" + window.country.countries[country].country + "&";
            data += "state=" + window.country.countries[country].states[state] + "&";
            data += "zip=" + zip;
            
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var resp = this.responseText;
                if(resp == "successful")
                {
                    location.reload();
                }
            }
            };
            xhttp.open("POST", "/Lapkart/AddNewAddress", true);
            xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
            xhttp.send("token="+token + "&userid="+userid+"&"+data);
        }
    }
}