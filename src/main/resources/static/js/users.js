/**
 * Created by tijs on 5/12/2016.
 */
/* ===========================================
 show all users
 =========================================== */
var count = 0;

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
                if(value[prop] != null && value[prop] != "" && prop != 'username' && prop != 'password'){
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

/******************************************** redirect *************************************************/
var redirectUser = function (username) {
    var urlPath = username
    return History.pushState({ urlPath: urlPath }, urlPath, urlPath);
}

/* ================================================================================================================
   User Modal
 =============================================================================================================== */

var infoUser = function(e) {

}