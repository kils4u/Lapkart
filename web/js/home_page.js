var token;
var userid;

function load_data()
{
    Navbar_login();
    getBigOffers();
    getSmallOffers();
    getDod();
    getNewItem();
    getBestSelling();
    getTopBrands();
}


// for first bigg offers

function getBigOffers()
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if ((this.readyState === 4 && this.status === 200) && this.responseText !== "null") {
            var big_off = JSON.parse(this.responseText);
            var big_off_inner = document.getElementById("big_off_inner");
            var big_off_slide = document.getElementById("big_off_slide");
            var j = 0;
            for (var i in big_off.big_offer)
            {
                var li_obj = document.createElement("li");
                li_obj.setAttribute("data-target", "#bigOfferCarousel");
                ;
                li_obj.setAttribute("data-slide-to", j);
                if (j === 0)
                {
                    li_obj.class = "active";
                }
                big_off_slide.appendChild(li_obj);
                big_off_inner.appendChild(create_big_off(big_off.big_offer[i], j));
                j++;
            }
        }
    };
    xhttp.open("GET", "/Lapkart/big_offers", true);
    xhttp.send();
}

function create_big_off(obj, j)
{
    var img_obj = document.createElement("img");
    img_obj.setAttribute("src", "Images/BigOffer/" + obj.off_img_link);
    img_obj.setAttribute("class", "d-block w-100");
    img_obj.setAttribute("style", "height:400px; width:100%");
    img_obj.setAttribute("alt", "Responsive image");
    var div_obj = document.createElement("div");
    if (j === 0)
        div_obj.setAttribute("class", "carousel-item active");
    else
        div_obj.setAttribute("class", "carousel-item");
    div_obj.appendChild(img_obj);
    return div_obj;
}

// for small offers

function getSmallOffers()
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if ((this.readyState === 4 && this.status === 200) && this.responseText !== "null") {
            var small_off = JSON.parse(this.responseText);
            var j = 0;
            for (var i in small_off.small_offer)
            {
                j++;
                var ele = document.getElementById("small_off_" + j);
                var img = document.createElement("img");
                img.setAttribute("src", "Images/BigOffer/" + small_off.small_offer[i].off_img_link);
                img.setAttribute("class", "img-thumbnail");
                img.setAttribute("style", "height:100%;width:100%;");
                img.setAttribute("alt","Responsive image");
                ele.appendChild(img);
            }
        }
    };
    xhttp.open("GET", "/Lapkart/small_offers", true);
    xhttp.send();
}

// for deals fothe day

function getDod()
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if ((this.readyState === 4 && this.status === 200) && this.responseText !== "null") {
            var product = JSON.parse(this.responseText);
            var dod_block = document.getElementById("DOD_block");
            for (var i=0; i<product.DOD.length; i++)
            {
                if (i === 3) {
                    break;
                }
                dod_block.appendChild(create_Product_card(product.DOD[i]));
            }
        }
    };
    xhttp.open("GET", "/Lapkart/deals_of_the_day", true);
    xhttp.send();
}

function getNewItem()
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if ((this.readyState === 4 && this.status === 200) && this.responseText !== "null") {
            var product = JSON.parse(this.responseText);
            var new_item_block = document.getElementById("new_item_block");
            for (var i=0; i<product.new_item.length; i++)
            {
                if (i === 3)
                    break;
                new_item_block.appendChild(create_Product_card(product.new_item[i]));
            }
        }
    };
    xhttp.open("GET", "/Lapkart/new_items", true);
    xhttp.send();
}

function getBestSelling()
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if ((this.readyState === 4 && this.status === 200) && this.responseText !== "null") {
            var product = JSON.parse(this.responseText);
            var best_selling_block = document.getElementById("best_selling_block");
            for (var i=0; i<product.best_selling.length; i++)
            {
                if (i === 3)
                    break;
                best_selling_block.appendChild(create_Product_card(product.best_selling[i]));
            }
        }
    };
    xhttp.open("GET", "/Lapkart/best_selling", true);
    xhttp.send();
}

function create_Product_card(obj)
{
    var img = document.createElement("img");
    img.setAttribute("src", "Images/product/" + obj.pimg_link);
    img.setAttribute("class", "card-img-top img-fluid");
    img.setAttribute("style", "max-height:15rem;width:100%");
    img.setAttribute("alt", "Responsive image");
    var title = document.createElement("h5");
    title.setAttribute("class", "card-title");
    title.innerHTML = obj.pbrand + " " + obj.pmodel;
    var disc = document.createElement("p");
    disc.setAttribute("class", "card-text");
    disc.innerHTML = "( " + obj.pprocessor + " / " + obj.pram_size + " / " + obj.phdd_size + " / " + obj.pgraphics_size + " ) ";
    var price = document.createElement("p");
    price.setAttribute("class", "card-text");
    price.innerHTML = "Price : " + obj.pprice;
    var card_body = document.createElement("div");
    card_body.setAttribute("class", "card-body");
    card_body.appendChild(title);
    card_body.appendChild(disc);
    card_body.appendChild(price);
    var card = document.createElement("div");
    card.setAttribute("class", "card p-2");
    card.setAttribute("style", "width:18rem;");
    card.appendChild(img);
    card.appendChild(card_body);
    var card_block = document.createElement("div");
    card_block.setAttribute("class", "col-md-3 mx-2");
    card_block.setAttribute("data-value", obj.pid);
    card_block.setAttribute("onclick", "open_prod(this.getAttribute('data-value'))");
    card_block.appendChild(card);
    return card_block;
}


function getTopBrands()
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if ((this.readyState === 4 && this.status === 200) && this.responseText !== "null") {
            var brand = JSON.parse(this.responseText);
            var topbrands_block = document.getElementById("top_brands_block");
            for (var i=0; i<brand.top_brands.length; i++)
            {
                if (i === 3)
                    break;
                topbrands_block.appendChild(create_brand(brand.top_brands[i]));
            }
        }
    };
    xhttp.open("GET", "/Lapkart/top_brands", true);
    xhttp.send();
}

function create_brand(obj)
{
    var img = document.createElement("img");
    img.setAttribute("src", "Images/brand_logo/" + obj.blogo);
    img.setAttribute("class", "card-img-top");
    img.setAttribute("style", "height:90%;width:90%");
    img.setAttribute("alt", "Responsive image");
    var title = document.createElement("h5");
    title.setAttribute("class", "card-title");
    title.setAttribute("align", "center");
    title.innerHTML = obj.bname;
    var card_body = document.createElement("div");
    card_body.setAttribute("class", "card-body");
    card_body.appendChild(title);
    var card = document.createElement("div");
    card.setAttribute("class", "card p-2");
    card.setAttribute("style", "width:18rem; max-height:20rem");
    card.appendChild(img);
    card.appendChild(card_body);
    var card_block = document.createElement("div");
    card_block.setAttribute("class", "col-md-3 mx-2");
    card_block.appendChild(card);
    return card_block;
}

function open_prod(pid)
{
    window.open("/Lapkart/Product.html?pid=" + pid);
}