/**
 * Created by tijs on 5/12/2016.
 */
/* ===========================================
 show all users
 =========================================== */
var userProperties = [];

axios.get("/users").then(function (response) {
    var data = response.data;

    // create html tags with users
    var ul = document.getElementsByClassName('users-list')[0];

    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            var value = data[key];
            console.log(value);
            var li = document.createElement("li");
            li.className = "users-list__item";
            li.setAttribute("data-search-term", value.username.toLowerCase());
            li.setAttribute("data-remodal-target", "ShowUserDetailsModal");
            li.setAttribute("onclick", "infoUser(this)");

            var img = document.createElement("img");
            img.className = 'users-list__item--image-small';
            img.setAttribute("src", value.avatar);
            img.setAttribute("alt", value.id);
            img.setAttribute("title", value.username);
            li.appendChild(img);

            var p = document.createElement("p");
            p.className = 'users-list__item--text';
            p.innerHTML = value.username;
            li.appendChild(p);

            for(var prop in value){
                if(prop != 'username' && prop != 'password' && prop != "id"){
                    var p = document.createElement("p");
                    p.setAttribute("style", "display: none");
                    p.innerHTML = value[prop];
                    li.appendChild(p);
                }
            }
            ul.appendChild(li);
        }
    }
});

/******************************************** live search filter *************************************************/
$('#search_box').on('keyup', function () {
    var searchTerm = $(this).val().toLowerCase();

    $('.users-list li').each(function () {
        if ($(this).filter('[data-search-term *= ' + searchTerm + ']').length > 0 || searchTerm.length < 1) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
});

/******************************************** redirect on view profile *******************************************/
$('#viewProfileBtn').click(function () {
    var urlPath = userProperties[1];
    return History.pushState({ urlPath: urlPath }, urlPath, urlPath);
});

/* ================================================================================================================
   User Modal
 =============================================================================================================== */

var infoUser = function(e) {
    userProperties =[] ;

    //clear user modal
    $("#detailsUser").html("");

    //select user div in modal to populate
    var element = document.getElementById('detailsUser');

    //build array with all user data
    $(e).children().each(function (i, v) {
        userProperties[i] = $(this)[0].innerHTML;
    });

     var H3 = document.createElement("h3");
     H3.className = "page-title page-title--medium";
     H3.innerHTML = "User properties";
     element.appendChild(H3);

    var img = document.createElement("img");
    img.className = "users-list__item--image-medium";
    img.src = userProperties[9];
    element.appendChild(img);

    if(userProperties[3] != "" || userProperties[4] != "") {
        var fullName = document.createElement("h4");
        fullName.innerHTML = "Full name";
        element.appendChild(fullName);

        var p = document.createElement("p");
        p.innerHTML = userProperties[3] + " " + userProperties[4];
        element.appendChild(p);
    }
    if(userProperties[2] != "") {
        var email = document.createElement("h4");
        email.innerHTML = "Email";
        element.appendChild(email);

        var p1 = document.createElement("p");
        p1.innerHTML = userProperties[2];
        element.appendChild(p1);
    }

    if(userProperties[5] != "") {
        var description = document.createElement("h4");
        description.innerHTML = "Description";
        element.appendChild(description);

        var p2 = document.createElement("p");
        p2.innerHTML = userProperties[5];
        element.appendChild(p2);
    }

    if(userProperties[7] != "") {
        var discord = document.createElement("h4");
        discord.innerHTML = "Discord-address";
        element.appendChild(discord);

        var p3 = document.createElement("p");
        p3.innerHTML = userProperties[7];
        element.appendChild(p3);

    }
    if(userProperties[8] != ""){
        var teamspeak = document.createElement("h4");
        teamspeak.innerHTML = "Teamspeak-address";
        element.appendChild(teamspeak);

        var p4= document.createElement("p");
        p4.innerHTML = userProperties[8];
        element.appendChild(p4);
    }


}