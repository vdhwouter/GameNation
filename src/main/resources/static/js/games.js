/**
 * Created by tijs on 24/11/2016.
 */
$(document).ready(function () {

/* ===========================================
 show all games
 =========================================== */

axios.get("/games").then(function (response) {
    var data = response.data

    // create html tage with games
    var ul = document.createElement("ul");
    ul.className = "live-search-list";
    document.getElementById('games').appendChild(ul);

    for (key in data) {
        if (data.hasOwnProperty(key)) {
            var value = data[key];

            var li = document.createElement("li");
            li.className = "file";
            li.setAttribute("data-search-term", value.name);

            var img = document.createElement("img");
            img.setAttribute("src", "img/games/" + value.imageName);
            img.setAttribute("alt", value.id);
            img.setAttribute("title", value.name);
            img.className += "addGamesImg";

            var p = document.createElement("p");
            p.innerHTML = value.name;

            li.appendChild(img);
            li.appendChild(p);
            ul.appendChild(li);
        }
    }
});




});
