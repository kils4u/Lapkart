var search;

function getSearch()
{
    var url = window.location.href;
    var start = url.indexOf("search=") + 7;
    var end = url.length;
    if(start !== 7)
    {
        window.search = url.substring(start,end);
        window.search = window.search.replace("%20"," ");
    }
}

function Load_search()
{
    getSearch();
    if(window.search !== null)
    {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            var resp = this.responseText;
            if(resp !== "No_Result" && resp !== null)
            {
                var serachAlert = document.getElementById("serachAlert");
                serachAlert.setAttribute("class","col-md-12 mt-5 alert alert-primary");
                var searchValue = document.getElementById("searchValue");
                searchValue.innerHTML = "Result for : "+window.search;
                add_item(resp);
            }
            else
            {
                var serachAlert = document.getElementById("serachAlert");
                serachAlert.setAttribute("class","col-md-12 mt-5 alert alert-danger");
                var searchValue = document.getElementById("searchValue");
                searchValue.innerHTML = "No Result found : "+window.search;
            }
        }
        };
        xhttp.open("POST", "/Lapkart/Search", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("search="+window.search);
    }
}

function add_item(resp)
{
    var result = JSON.parse(resp);
    var ele = document.getElementById("items_collection");
    var len = result.Items.length;
    var row;
    for(let i=0;i<len;i++)
    {
        if(i%3 === 0)
        {
            row = document.createElement("div");
            row.setAttribute("class","row m-3");
            ele.appendChild(row);
        }
        row.appendChild(create_Product_card(result.Items[i]));
    }
}

function create_Product_card(obj)
{
    var img = document.createElement("img");
    img.setAttribute("src","Images/product/" + obj.pimg_link);
    img.setAttribute("class","card-img-top img-fluid");
    img.setAttribute("style","height:100%;width:100%");
    img.setAttribute("alt","");
    var title = document.createElement("h5");
    title.setAttribute("class","card-title");
    title.innerHTML = obj.pbrand + " " + obj.pmodel;
    var disc = document.createElement("p");
    disc.setAttribute("class","card-text");
    disc.innerHTML = "( " + obj.pprocessor + " / " + obj.pram_size + " / " + obj.phdd_size + " / " + obj.pgraphics_size + " ) ";
    var price = document.createElement("p");
    price.setAttribute("class","card-text");
    price.innerHTML = "Price : " + obj.pprice;
    var card_body = document.createElement("div");
    card_body.setAttribute("class","card-body");
    card_body.appendChild(title);
    card_body.appendChild(disc);
    card_body.appendChild(price);
    var card = document.createElement("div");
    card.setAttribute("class", "card p-2");
    card.setAttribute("style","width: 18rem;");
    card.appendChild(img);
    card.appendChild(card_body);
    var card_block = document.createElement("div");
    card_block.setAttribute("class","col-md-3 m-4");
    card_block.setAttribute("data-value",obj.pid);
    card_block.setAttribute("onclick","open_prod(this.getAttribute('data-value'))");
    card_block.appendChild(card);
    return card_block;
}

function open_prod(pid)
{
    window.open("/Lapkart/Product.html?pid="+pid);
}