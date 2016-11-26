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
                li.setAttribute("data-search-term", value.name.toLowerCase());
                li.setAttribute("data-remodal-target", "ShowGameDetailsModal");
                li.setAttribute("onclick", "infoGame(this)");

                var img = document.createElement("img");
                img.setAttribute("src", "img/games/" + value.imageName);
                img.setAttribute("alt", value.id);
                img.setAttribute("title", value.name);
                img.className += "addGamesImg";

                var p = document.createElement("p");
                p.innerHTML = value.name;

                var secondP = document.createElement("p");
                secondP.setAttribute("style", "display: none");
                secondP.innerHTML = value.description;

                li.appendChild(img);
                li.appendChild(p);
                li.appendChild(secondP);
                ul.appendChild(li);
            }
        }
    });

    /* ===========================================
     live search filter games
     =========================================== */
    $('.live-search-box').on('keyup', function () {
        var searchTerm = $(this).val().toLowerCase();

        $('.live-search-list li').each(function () {
            if ($(this).filter('[data-search-term *= ' + searchTerm + ']').length > 0 || searchTerm.length < 1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });


});




var allGames = [];
var infoGame = function (e) {

    $(e).children().each(function (i, v) {
        if (i >= 0 && i < 5) {
            allGames[i] = $(this)[0].innerHTML;
        }
    });
    console.log(allGames);
    $('#detailsGames p').text(allGames[2]);
}
