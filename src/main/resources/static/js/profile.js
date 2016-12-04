/* ===========================================
   show all games that user added
   =========================================== */

$('#shareProfile').on('click', function(e){
    e.preventDefault();
    console.info('Facebook share initiated...');
    logInWithFacebook('http://www.localhost:8080/' + window.profileUser.username);
});

$(document).ready(function() {
    // HACK , CHANGE THI
    var currentUser = window.profileUser;

    axios.get("/users/" + currentUser.id + "/games")
        .then(function(response) {
            var data = response.data;

            // create html tage with added games
            var ul = document.getElementsByClassName('games-list')[0];

            for(key in data) {
                if(data.hasOwnProperty(key)) {
                    var value = data[key];

                    var li = document.createElement("li");
                    li.className = 'games-list__item';

                    var img = document.createElement("img");
                    img.className = 'games-list__item--image-small';
                    img.setAttribute("src", "img/games/" + value['game'].imageName);
                    img.setAttribute("alt", value['game'].id);
                    img.setAttribute("title", value['game'].name);

                    // var firstP = document.createElement("p");
                    // firstP.innerHTML = value['game'].id;
                    // firstP.setAttribute("style", "display: none");

                    var firstP = document.createElement("p");
                    firstP.className = 'games-list__item--level';
                    firstP.innerHTML = value['skill_level'];

                    var secondP = document.createElement("p");
                    secondP.className = 'games-list__item--text';
                    secondP.innerHTML = value['game'].name;

                    li.appendChild(img);
                    li.appendChild(firstP);
                    li.appendChild(secondP);
                    ul.appendChild(li);
                }
            }
        })
});