/* ===========================================
   show all games that user added
   =========================================== */
$(document).ready(function() {
    var APICALL = function(){
		var request = new XMLHttpRequest();
		request.addEventListener('load',listen);
		request.open("get", 'http://localhost:8080/api/users/' + (window.location.href).substring((window.location.href).lastIndexOf("/") + 1, (window.location.href).length) + '/games');
		request.send();
	};

	var listen = function(){
		var data = JSON.parse(this.responseText);

        // create html tage with added games
		var ul = document.createElement("ul");
		ul.setAttribute("id", "listAddedGames");
		document.getElementById('games-overview').appendChild(ul);

		for(key in data) {
			if(data.hasOwnProperty(key)) {
				var value = data[key];

				var li = document.createElement("li");

                var a = document.createElement('a');
                a.setAttribute("data-remodal-target", "EditGameModal");
                a.setAttribute("onclick", "editGame(this)");

                var firstP = document.createElement("p");
                firstP.innerHTML = value['game'].id;
                firstP.setAttribute("style", "display: none");

				var img = document.createElement("img");
				img.setAttribute("src", "img/games/" + value['game'].imageName);
				img.setAttribute("alt", value['game'].id);
				img.setAttribute("title", value['game'].name);
				img.className += "game";

				var secondP = document.createElement("p");
				secondP.innerHTML = value['game'].name;

				var thirdP = document.createElement("p");
				thirdP.innerHTML = value.skill_level;
                thirdP.setAttribute("style", "display: none");

                a.appendChild(firstP);
				a.appendChild(img);
				a.appendChild(secondP);
				a.appendChild(thirdP);
				li.appendChild(a);
                ul.appendChild(li);
			}
		}
	};

    APICALL();
});