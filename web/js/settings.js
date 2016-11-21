/* ===========================================
   live search filter games
   =========================================== */
$('.live-search-box').on('keyup', function(){
    var searchTerm = $(this).val().toLowerCase();

    $('.live-search-list li').each(function(){
        if ($(this).filter('[data-search-term *= ' + searchTerm + ']').length > 0 || searchTerm.length < 1) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
});


/* ===========================================
   show all games in remodal
   make an array from all selected games
   =========================================== */
// array with selected games
var addedGames = new Array();

$(document).ready(function() {
    var APICALL = function(){
		var request = new XMLHttpRequest();
		request.addEventListener('load',listen);
		request.open("get", 'http://localhost:8080/api/games');
		request.send();
	};

	var listen = function(){
		var data = JSON.parse(this.responseText);

        // create html tage with games
		var ul = document.createElement("ul");
		ul.className = "live-search-list";
		document.getElementById('addGames').appendChild(ul);

		for(key in data) {
			if(data.hasOwnProperty(key)) {
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

		// what if a user click on a game?
		$('.addGamesImg').click(function(){
			$(this).toggleClass('active');

			if (!eleContainsInArray(addedGames, $(this).attr("alt"))) {
			    addedGames.push($(this).attr("alt"));
			}
            else {
                var index = addedGames.indexOf($(this).attr("alt"));
                addedGames.splice(index, 1);
            }

		});
	};

    // check if game is already in array
	function eleContainsInArray(arr,element){
        if(arr != null && arr.length > 0){
            for(var i=0;i < arr.length; i++){
                if(arr[i] == element)
                    return true;
            }
        }
        return false;
    }


    // by click on the add button
    $('#addConfirm').click(function(){
        if (addedGames.length == 0) {
            console.log("no items selected");
        }
        else {
            console.log(addedGames);

            //toevoegen game aan user werkt nog niet

            /*var parameters = { "games": addedGames};
            $.post(
                'localhost:8080/api/users/5830533b9e783a58e84d6a80/games',
                parameters
            ).setRequestHeader("Authorization", "Bearer supertoken");*/

            /*$.ajax({
                type: 'POST',
                url: 'http://localhost:8080/api/users/58331d58d28b93294860ec20/games',
                headers: {
                    "Authorization":"Bearer supertoken"
                },
                data: {
                    "games": addedGames
                }
            });*/
        }
    });

    APICALL();
});



/* ===========================================
   edit skillset game modal
   =========================================== */
function editGame(e) {
    var td_list = [];
    $(e).children().each(function(i, v) {
        if (i >= 0 && i < 5) {
            td_list[i] = $(this)[0].innerHTML;
        }
    });

    $('#editGameTitle').text("Edit " + td_list[2]);
    $('#editGame input[type="number"]').val(td_list[3].replace(/[^0-9]/g, ''));

    // by click on the add button
    $('#modelSkilEditEdit').click(function(){
        console.log("edit");
    });
}


/* ===========================================
   show all games that user added
   =========================================== */
$(document).ready(function() {
    var APICALL = function(){
		var request = new XMLHttpRequest();
		request.addEventListener('load',listen);
		request.open("get", 'http://localhost:8080/api/users/tijs/games');
		request.send();
	};

	var listen = function(){
		var data = JSON.parse(this.responseText);

        // create html tage with added games
		var ul = document.createElement("ul");
		ul.setAttribute("id", "listAddedGames");
		document.getElementById('games').appendChild(ul);

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

        // add li tag for add a new game
        var ul = document.getElementById("listAddedGames");
        var li = document.createElement("li");

        var a = document.createElement("a");
        a.setAttribute("data-remodal-target", "AddNewGameModal");

        var img = document.createElement("img");
        img.setAttribute("src", "img/addGame.png");
        img.setAttribute("alt", "addGame");
        img.setAttribute("title", "Add new Game");
        img.setAttribute("id", "addGame");
        img.className += "game";

        var p = document.createElement("p");
        p.innerHTML = "New game";

        a.appendChild(img);
        a.appendChild(p);
        li.appendChild(a);
        ul.appendChild(li);
	};

    APICALL();
});