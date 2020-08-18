var pid;

function getPID()
{
    var url = window.location.href;
    var start = url.indexOf("pid=") + 4;
    var end = url.length;
    if(start !== 3)
    {
        var id = url.substring(start,end);
        var len = id.length;
        window.pid = "";
        for(i=0;i<len;i++)
        {
            if(id.charCodeAt(i) < 48 || id.charCodeAt(i) > 57 )
                break;
            window.pid += id.charAt(i);
        }
    }
    else
    {
        window.open("\Lapkart\Home.html","_self");
    }
}

function getProduct()
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
    if (this.readyState === 4 && this.status === 200) {
        var data = JSON.parse(this.responseText);
        document.getElementById("brand_name").innerHTML = data.product[0].bname;
        document.getElementById("Model_name").innerHTML = data.product[0].mname;
        document.getElementById("color").innerHTML = data.product[0].color;
        document.getElementById("weight").innerHTML = data.product[0].weight;
        document.getElementById("procrssor_brand").innerHTML = data.product[0].processor;
        document.getElementById("processor_name").innerHTML = data.product[0].processor_gen;
        document.getElementById("processor_speed").innerHTML = data.product[0].processor_speed;
        document.getElementById("ram").innerHTML = data.product[0].ram;
        document.getElementById("ram_size").innerHTML = data.product[0].ram_size;
        document.getElementById("graphics").innerHTML = data.product[0].graphics;
        document.getElementById("graphics_size").innerHTML = data.product[0].graphics_size;
        document.getElementById("ssd").innerHTML = data.product[0].ssd;
        document.getElementById("hdd").innerHTML = data.product[0].hdd;
        document.getElementById("display").innerHTML = data.product[0].display;
        document.getElementById("display_size").innerHTML = data.product[0].disp_size;
        document.getElementById("scr_res").innerHTML = data.product[0].scr_res;
        document.getElementById("product_name").innerHTML = data.product[0].bname + " " + data.product[0].mname;
        document.getElementById("price").innerHTML = data.product[0].price;
        var ele = document.getElementById("img");
        ele.setAttribute("src","Images/product/"+data.product[0].img);
    }
    };
    xhttp.open("POST", "/Lapkart/Product", true);
    xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xhttp.send("pid="+pid); 
}

function addToCart()
{
    if(window.isLogged)
    {
        var userid = localStorage.getItem("userid");
        var token = localStorage.getItem("token");
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            if(this.responseText === "successful")
            {
                window.open("Cart.html","_self");
            }
        }
        };
        xhttp.open("POST", "/Lapkart/AddToCart", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("pid="+window.pid+"&userid="+userid+"&token="+token);
    }
    else
    {
        window.open("login.html","_self");
    }
}