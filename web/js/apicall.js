
var APICALL = function(){
    var request = new XMLHttpRequest();
    request.addEventListener('load',listen)
    request.open("get", 'http://localhost:8080/api/games');
    request.send();
};

var listen = function(){
    var data = JSON.parse(this.responseText)

    var ul = document.createElement("ul");
    document.getElementById('addGames').appendChild(ul);

    for(key in data) {
        if(data.hasOwnProperty(key)) {
            var value = data[key];

            var li = document.createElement("li");
                li.className = "file";

                var img = document.createElement("img");
                img.setAttribute("src", "img/" + value.imageName + ".jpg");
                img.setAttribute("alt", value.name);
                img.setAttribute("title", value.name);

                var p = document.createElement("p");
                p.innerHTML = value.name;

                li.appendChild(img);
                li.appendChild(p);
                ul.appendChild(li);
        }
    }

};



// start script
window.addEventListener('load', function() {
    APICALL();
});