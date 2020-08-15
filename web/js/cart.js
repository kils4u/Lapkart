var RemoveCart;

var Total_Price = 0;

function Load_cart()
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
                get_items(resp);
            }
            else
            {
                alert("response : " + resp);
            }
        }
        };
        xhttp.open("POST", "/Lapkart/Cart", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("token="+token);
    }
}

function get_items(response)
{
    var cart = JSON.parse(response);
    var len = cart.items.length;
    if(len>0)
    {
        document.getElementById("Error").innerHTML = "";
        document.getElementById("totalItems").innerHTML = len;
        var ele = document.getElementById("cart-list");
        var price_tag = document.getElementById("price-tag");
        for(i=0;i<len;i++)
        {
            ele.appendChild(create_item(cart.items[i]));
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
        document.getElementById("shopping").style.visibility = "visible";
        document.getElementById("checkout").style.visibility = "visible";
        
    }
}

function create_item(item)
{
    var li = document.createElement("li");
    li.setAttribute("class","list-group-item p-4");
    
    var div_row = document.createElement("div");
    div_row.setAttribute("class","row");
    
    var div_col_1 = document.createElement("div");
    div_col_1.setAttribute("class","col-md-4");
    div_col_1.setAttribute("data-value",item.p_id);
    div_col_1.setAttribute("onclick","open_item(this.getAttribute('data-value'))");
    
    var img = document.createElement("img");
    img.setAttribute("src","Images/product/" + item.img);
    img.setAttribute("class","img-fluid");
    
    div_col_1.appendChild(img);
    
    var div_col_2 = document.createElement("div");
    div_col_2.setAttribute("class","col-sm-8 pl-4");
    
    var h4 = document.createElement("h4");
    h4.setAttribute("id",item.name+"-name");
    h4.setAttribute("class","mb-4");
    h4.setAttribute("data-value",item.p_id);
    h4.setAttribute("onclick","open_item(this.getAttribute('data-value'))");
    h4.innerHTML = item.name;
    
    var h6 = document.createElement("h6");
    h6.setAttribute("class","mb-4 ml-2");
    h6.innerHTML = "Quentity : &nbsp&nbsp";
    
    var sub = document.createElement("button");
    sub.setAttribute("type","button");
    sub.setAttribute("data-value",item.cartid)
    sub.setAttribute("id",item.cartid + "-sub")
    sub.setAttribute("class","btn btn-primary btn-sm ml-1");
    sub.setAttribute("onclick","sub_item(this.getAttribute('data-value'))");
    sub.innerHTML="-";
    if(item.quantity <= 1)
        sub.disabled = true;
    else
        sub.disabled = false;
    
    var input = document.createElement("input");
    input.setAttribute("id",item.cartid + "-input")
    input.setAttribute("data-value",item.cartid)
    input.setAttribute("size","1");
    input.setAttribute("class","m-1");
    input.setAttribute("type","text");
    input.setAttribute("onchange","input_change(this)");
    input.value = item.quantity;
    
    var add = document.createElement("button");
    add.setAttribute("id",item.cartid + "-add")
    add.setAttribute("type","button");
    add.setAttribute("data-value",item.cartid)
    add.setAttribute("class","btn btn-primary btn-sm");
    add.setAttribute("onclick","add_item(this.getAttribute('data-value'))");
    add.innerHTML="+";
    
    h6.appendChild(sub);
    h6.appendChild(input)
    h6.appendChild(add)
    
    var h5 = document.createElement("h5");
    h5.innerHTML = "Price : " + item.price;
    
    var remove = document.createElement("button");
    remove.setAttribute("data-value",item.cartid)
    remove.setAttribute("type","button");
    remove.setAttribute("data-toggle","modal");
    remove.setAttribute("data-target","#RemoveItem");
    remove.setAttribute("class","btn btn-link float-right");
    remove.setAttribute("onmouseover","setValueForCartItem(this.getAttribute('data-value'))");
    remove.innerHTML="Remove Item";
    
    div_col_2.appendChild(h4);
    div_col_2.appendChild(h6);
    div_col_2.appendChild(h5);
    div_col_2.appendChild(remove);
    
    div_row.appendChild(div_col_1);
    div_row.appendChild(div_col_2);
    
    li.appendChild(div_row);
    return li;
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

function setValueForCartItem(cartid)
{
    window.RemoveCart = cartid;
}

function add_item(cartid)
{
    var token = localStorage.getItem("token");
    var userid = localStorage.getItem("userid");
    if(token != null && userid != null)
    {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            location.reload();
        }
        };
        xhttp.open("POST", "/Lapkart/EditCart", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("token="+token+"&userid="+userid+"&cartid="+cartid+"&operation=add&value=1");
    }
    else
    {
        location.reload();
    }
}

function sub_item(cartid)
{
    var token = localStorage.getItem("token");
    var userid = localStorage.getItem("userid");
    var value = document.getElementById(cartid+"-input").value;
    if((token != null && userid != null) && value > 1)
    {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            location.reload();
        }
        };
        xhttp.open("POST", "/Lapkart/EditCart", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("token="+token+"&userid="+userid+"&cartid="+cartid+"&operation=sub&value=1");
    }
    else
    {
        location.reload();
    }
}

function remove_item()
{
    var cartid = window.RemoveCart;
    var token = localStorage.getItem("token");
    var userid = localStorage.getItem("userid");
    if(token != null && userid != null)
    {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            location.reload();
        }
        };
        xhttp.open("POST", "/Lapkart/RemoveFromCart", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("token="+token+"&userid="+userid+"&cartid="+cartid);
    }
    else
    {
        location.reload();
    }
}

function input_change(ele)
{
    var cartid = ele.getAttribute('data-value');
    var token = localStorage.getItem("token");
    var userid = localStorage.getItem("userid");
    var value = document.getElementById(cartid+"-input").value;
    if((token != null && userid != null) && value > 0)
    {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            location.reload();
        }
        };
        xhttp.open("POST", "/Lapkart/EditCart", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("token="+token+"&userid="+userid+"&cartid="+cartid+"&value="+value+"&operation=change");
    }
    else
    {
        location.reload();
    }
}

function open_item(pid)
{
    window.open("Product.html?pid="+pid,"_self");
}

function doShopping()
{
    window.open("Home.html","_self");
}

function checkout()
{
    window.open("Billing_address.html","_self");
}