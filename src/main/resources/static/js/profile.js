/* ================================================================================================================
 facebook sharing
 =============================================================================================================== */
$('#shareProfile').on('click', function (e) {
    e.preventDefault();
    console.info('Facebook share initiated...');
    logInWithFacebook('http://www.localhost:8080/' + window.profileUser.username);
});


/* ================================================================================================================
 show all games that user added
 =============================================================================================================== */

 $(document).ready(function () { 
    // this is kind of a hack.. :p 
    var currentUser = window.profileUser; 
    axios.get("/users/" + currentUser.id + "/games") 
        .then(function (response) {
            var data = response.data;

            // create html tage with added games
            var ul = document.getElementsByClassName('games-list')[0];

            for (key in data) {
                if (data.hasOwnProperty(key)) {
                    var value = data[key];

                    var li = document.createElement("li");
                    li.className = 'games-list__item';

                    var img = document.createElement("img");
                    img.className = 'games-list__item--image-small';
                    img.setAttribute("src", "img/games/" + value['game'].imageName);
                    img.setAttribute("alt", value['game'].id);
                    img.setAttribute("title", value['game'].name);

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



    /* ===========================================================================================================
     friend button
     ========================================================================================================== */

    var friendButton = $('#friend-button');

    function updateFriendButton(relation) {
        if (relation) currentUser.relation = relation;
        if (session.authenticated) {
            // check if not the profile page of logged in user
            if (currentUser.id != session.id) {
                // if there is no relation, show "add as friend" button
                if (!currentUser.relation) {
                    friendButton.text("Add as friend");
                    friendButton.attr('data-user-id', currentUser.id);
                    friendButton.attr('data-type', 'add');
                    friendButton.show();
                    // if there is a relation, not yet accepted...
                } else if (!currentUser.relation.accepted) {
                    // ..and authenticated user is sending
                    if (currentUser.relation.sender.id == session.id) {
                        friendButton.text("Cancel friend request");
                        friendButton.attr('data-type', 'delete');
                        friendButton.show();
                        // .. and authenticated user is receiving
                    } else if (currentUser.relation.receiver.id == session.id) {
                        friendButton.text("Accept friend request");
                        friendButton.attr('data-type', 'add');
                        friendButton.show();
                    }
                } else {
                    // users are friends
                    friendButton.text("End friendship");
                    friendButton.attr('data-type', 'delete');
                    friendButton.show();
                }

            } else {
                friendButton.hide()
            }
        }
    }

    updateFriendButton();

    friendButton.click(function () {
        var id = $(this).attr('data-user-id');
        var type = $(this).attr('data-type');

        switch (type) {
            case 'add':
                axios.post('/users/' + session.id + '/friends', {user: currentUser.id})
                    .then(function (res) {
                        // reload friendship button with new data
                        currentUser.relation = res.data;
                        updateFriendButton();
                    })
                break;
            case 'delete':
                axios.delete('/users/' + session.id + '/friends/' + currentUser.id)
                    .then(function (res) {
                        // reload friendship button with new data
                        currentUser.relation = res.data;
                        updateFriendButton();
                    })
                break;
        }

    })

    window.updateFriendButton = updateFriendButton;
});