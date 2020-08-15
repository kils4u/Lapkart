
function load_orders()
{
    var token = localStorage.getItem("token");
    var userid = localStorage.getItem("userid");
    if(token != null && userid != null)
    {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var resp = this.responseText;
            if(resp != null)
            {
                var MyOrder = JSON.parse(resp);
                var len = MyOrder.orders.length;
                var ele = document.getElementById("MyOrders");
                for(let i=0;i<len;i++)
                {
                    ele.appendChild(create_order(MyOrder.orders[i]));
                }
            }
            else
            {
                alert("response : " + resp);
            }
        }
        };
        xhttp.open("POST", "/Lapkart/MyOrder", true);
        xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        xhttp.send("token="+token+"&userid="+userid);
    }
}

function create_order(order)
{
    var card = document.createElement("div");
    card.setAttribute("class","card mb-5");
    card.setAttribute("style","width: 70rem;");
    
    var card_header = document.createElement("div");
    card_header.setAttribute("class","card-header");
    
    var row = document.createElement("div");
    row.setAttribute("class","row");
    
    var col1 = document.createElement("div");
    col1.setAttribute("class","col-md-6");
    
    var ord = document.createElement("span");
    ord.setAttribute("class","font-weight-bold text-left");
    ord.innerHTML = "OrderID : ";
    
    var ordV = document.createElement("span");
    ordV.setAttribute("class","font-weight-normal text-left");
    ordV.innerHTML = order.orderid;
    
    col1.appendChild(ord);
    col1.appendChild(ordV);
    
    var col2 = document.createElement("div");
    col2.setAttribute("class","col-md-6");
    
    var date = document.createElement("span");
    date.setAttribute("class","font-weight-bold text-left");
    date.innerHTML = "Date : ";
    
    var dateV = document.createElement("span");
    dateV.setAttribute("class","font-weight-normal text-left");
    dateV.innerHTML = order.date;
    
    col2.appendChild(date);
    col2.appendChild(dateV);
    
    row.appendChild(col1);
    row.appendChild(col2);
    
    card_header.appendChild(row);
    
    var unOrderList = document.createElement("ul");
    unOrderList.setAttribute("class","list-group list-group-flush");
    
    var len = order.items.length;
    for(let i=0;i<len;i++)
    {
        unOrderList.appendChild(create_item(order.items[i]));
    }
    
    var card_footer = document.createElement("div");
    card_footer.setAttribute("class","card-footer text-muted");
    
    var row_foot = document.createElement("div");
    row_foot.setAttribute("class","row");
    
    var col1_foot = document.createElement("div");
    col1_foot.setAttribute("class","col-md-6");
    
    var ord_foot = document.createElement("span");
    ord_foot.setAttribute("class","font-weight-bold text-left");
    ord_foot.innerHTML = "Price : ";
    
    var ordV_foot = document.createElement("span");
    ordV_foot.setAttribute("class","font-weight-normal text-left");
    ordV_foot.innerHTML = order.amount;
    
    col1_foot.appendChild(ord_foot);
    col1_foot.appendChild(ordV_foot);
    
    var col2_foot = document.createElement("div");
    col2_foot.setAttribute("class","col-md-6");
    
    var date_foot = document.createElement("span");
    date_foot.setAttribute("class","font-weight-bold text-left");
    date_foot.innerHTML = "Status : ";
    
    var dateV_foot = document.createElement("span");
    dateV_foot.setAttribute("class","font-weight-normal text-left");
    dateV_foot.innerHTML = order.status;
    
    col2_foot.appendChild(date_foot);
    col2_foot.appendChild(dateV_foot);
    
    row_foot.appendChild(col1_foot);
    row_foot.appendChild(col2_foot);
    
    card_footer.appendChild(row_foot);
    
    card.appendChild(card_header);
    card.appendChild(unOrderList);
    card.appendChild(card_footer);
    
    return card;
}

/*
<div class="card mb-5" style="width: 60rem;">
    <div class="card-header">
            <div class="row">
                <div class="col-md-6">
                    <span class="font-weight-bold text-left"> OrderiID :  </span> <span clas="font-weight-normal"> 10050 </span>
                </div>
                <div class="col-md-6">
                    <span class="font-weight-bold text-right"> Date :  </span> <span clas="font-weight-normal"> 10-04-2019 </span>
                </div>
            </div>
    </div>

    <ul class="list-group list-group-flush">

    </ul>

    <div class="card-footer text-muted">
    <div class="row">
                    <div class="col-md-6">
                    <span class="font-weight-bold text-left"> Total amount :  </span> <span clas="font-weight-normal"> 250000 </span>
            </div>
            <div class="col-md-6">
                    <span class="font-weight-bold text-right"> Status :  </span> <span clas="font-weight-normal"> Successful </span>
            </div>
    </div>
    </div>
</div>

*/

function create_item(item)
{
    var list = document.createElement("li");
    list.setAttribute("class","list-group-item");
    
    var row = document.createElement("div");
    row.setAttribute("class","row");
    
    var col1 = document.createElement("div");
    col1.setAttribute("class","col-md-4 m-3");
    
    var img = document.createElement("img");
    img.setAttribute("class","img-fluid");
    img.setAttribute("src","Images/product/"+item.img);
    
    col1.appendChild(img);
    
    var col2 = document.createElement("div");
    col2.setAttribute("class", "col-md-6 m-3");
    
    var name = document.createElement("h2");
    name.setAttribute("class","mb-3");
    name.innerHTML = item.name;
    
    var p1 = document.createElement("p");
    p1.setAttribute("class","m-4");
    
    var q = document.createElement("span");
    q.setAttribute("class","font-weight-bold");
    q.innerHTML = "Quantity : ";
    
    var qv = document.createElement("span");
    qv.setAttribute("class","font-weight-light");
    qv.innerHTML = item.quantity;
    
    p1.appendChild(q);
    p1.appendChild(qv);
    
    var p2 = document.createElement("p");
    p2.setAttribute("class","m-4");
    
    var p = document.createElement("span");
    p.setAttribute("class","font-weight-bold");
    p.innerHTML = "Price : ";
    
    var pv = document.createElement("span");
    pv.setAttribute("class","font-weight-light");
    pv.innerHTML = item.price;
    
    p2.appendChild(p);
    p2.appendChild(pv);
    
    col2.appendChild(name);
    col2.appendChild(p1);
    col2.appendChild(p2);
    
    row.appendChild(col1);
    row.appendChild(col2);
    
    list.appendChild(row)
    
    return list;
}

/*
<li class="list-group-item">

    <div class="row">
            <div class="col-md-4 m-3">
                    <img class="img-fluid" src="Images/product/Acer_300.jpeg" alt="Card image cap">
            </div>
            <div class="col-md-6 m-3">
                    <h2 class="mb-3">Accer Preaditor</h2>
                    <p class="m-4"><span class="font-weight-bold">Quantity : </span> <span class="font-weight-light"> 6 </span></p>
                    <p class="m-4"><span class="font-weight-bold">Price : </span> <span class="font-weight-light">80000</span></p>
            </div>
    </div>

</li> 
*/