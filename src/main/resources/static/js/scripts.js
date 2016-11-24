$(document).ready(function () {
    'use strict';

    crossroads.addRoute('/', function () {
        window.location += 'login';
    }, 100);

    crossroads.addRoute('/login', function () {
        $('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
        $('body').load('login.html', function () {
            if (session.authenticated) {
                console.log('already authenticated!, redirecting to profile...')
                navigateTo(session.user.username, session.user.username)
            }

            $('#login-form').on('submit', function (e) {
                e.preventDefault()
                var username = $('#login-form input[type=text]').val()
                var password = $('#login-form input[type=password]').val()
                session.login(username, password)
                    .then(function (user) {
                        console.log('successfully authenticated, redirecting to profile...')
                        navigateTo(username, username)
                    })
                    .catch(function(error) {
                        console.log('bad credentials!')
                        $('p#login-errors').text("BAD CREDENTIALS!")
                    })
            })
        });
    }, 100);

    crossroads.addRoute('/register', function () {
        $('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
        $('body').load('register.html', function () {

        });
    }, 100);

    crossroads.addRoute('/settings/{username}', function (username) {
        axios.get('/users?username=' + username)
            .then(function (data) {
                var user = data.data[0]
                $('head').append($('<link rel="stylesheet" href="css/stylesProfile.css">'));
                $('head').append($('<link rel="stylesheet" href="css/remodal.css">'));
                $('head').append($('<link rel="stylesheet" href="css/remodal-default-theme.css">'));
                $('body').load('settings.html', function () {
                    $('#set')[0].href = username;
                    $('#username')[0].value = user.username;
                    $('#email')[0].value = user.email;
                    $('#firstname')[0].value = user.firstname;
                    $('#lastname')[0].value = user.lastname;
                    $('#teamspeakAddr')[0].value = user.teamspeak;
                    $('#DiscordAddr')[0].value = user.discord;
                    $('#descriptionText')[0].value = user.description;
                    $('#formdata')[0].action += user.id;
                });
            })
    }, 100);

    crossroads.addRoute('/games', function () {
        $('head').append($('<link rel="stylesheet" href="css/stylesProfile.css">'));
        $('head').append($('<link rel="stylesheet" href="css/remodal.css">'));
        $('head').append($('<link rel="stylesheet" href="css/remodal-default-theme.css">'));
        $('body').load('games.html');
    }, 100);

    crossroads.addRoute('/{username}', function (username) {
        axios.get('/users?username=' + username).then(function (data) {
            var user = data.data[0]
            $('head').append($('<link rel="stylesheet" href="css/stylesProfile.css">'))
            $('body').load('profile.html', function () {
                $('#set')[0].href += username;
                $('#usernameVal')[0].innerHTML = user.username;
                $('#emailVal')[0].innerHTML = user.email;
                $('#namelVal')[0].innerHTML = user.firstname + ' ' + user.lastname;
                $('#teamspeakVal')[0].innerHTML = user.teamspeak;
                $('#descriptionVal')[0].innerHTML = user.description;
                $('#discordVal')[0].innerHTML = user.discord;
            });
        })
    }, 1);

    crossroads.parse(document.location.pathname);

    var History, State;

    History = window.History;

    if (History.enabled) {
        State = History.getState();

        History.pushState({ urlPath: window.location.pathname }, $('title').text(), State.urlPath);
    }

    History.Adapter.bind(window, 'statechange', function () {
        return crossroads.parse(document.location.pathname);
    })

    $('body').on('click', 'a', function (e) {
        var urlPath = $(this).attr('href');
        e.preventDefault();
        var title = $(this).text();
        return History.pushState({ urlPath: urlPath }, title, urlPath);
    });

    window.navigateTo = function navigateTo(urlPath, title = 'GameNation') {
        if (title) {
            title = title + ' | GameNation'
        }

        History.pushState({ urlPath: urlPath }, title, urlPath)
    }
});
