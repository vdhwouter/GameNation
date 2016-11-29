$(document).ready(function () {
    'use strict';

    crossroads.addRoute('/', function () {
        navigateTo('/login', 'Login')
    }, 100);

    crossroads.addRoute('/me', function() {
        window.location = session.user.username;
    }, 100);

    crossroads.addRoute('/logout', function() {
        session.logout();
        window.location = '/login';
    }, 100);

    crossroads.addRoute('/login', function (query) {
        console.log(query);
        $('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
        $('head').append($('<link rel="stylesheet" href="css/remodal.css">'));
        $('head').append($('<link rel="stylesheet" href="css/remodal-default-theme.css">'));
        $('.sidebar').load('sidebar/not_logged_in.html');
        $('.content').load('content/login.html', function () {
            if (session.authenticated) {
                console.info('already authenticated!, redirecting to profile...')
                return navigateTo(session.user.username, session.user.username)
            }

            if (window.loginMessage) {
                $('#login-message').text(window.loginMessage);
                window.loginMessage = null
            }

            $('#login-form').on('submit', function (e) {
                e.preventDefault()
                var username = $('#login-form input[type=text]').val()
                var password = $('#login-form input[type=password]').val()
                session.login(username, password)
                    .then(function (user) {
                        console.info('successfully authenticated, redirecting to profile...')
                        navigateTo(username, username)
                    })
                    .catch(function (error) {
                        console.warn('bad credentials!')
                        $('p#login-errors').text("BAD CREDENTIALS!")
                    })
            })
        });
    }, 100);

    crossroads.addRoute('/register', function () {
        $('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))

        $('head').append($('<link rel="stylesheet" href="css/remodal.css">'));
        $('head').append($('<link rel="stylesheet" href="css/remodal-default-theme.css">'));
        $('.sidebar').load('sidebar/not_logged_in.html');
        $('.content').load('content/register.html', function () {
            if (session.authenticated) {
                console.info('already authenticated! no need to register - redirecting to profile...')
                return navigateTo(session.user.username, session.user.username)
            }

            $('#register-form').on('submit', function (e) {
                e.preventDefault()
                console.log("ERAHAKDKASHD")
                var username = $('#register-form input[name=username]').val()
                var email = $('#register-form input[name=email]').val()
                var password = $('#register-form input[name=password]').val()
                var confirmation = $('#register-form input[name=confirmation]').val()

                // Return error array as list items
                var errorArray = CheckFormInput(email, password, confirmation, username);
                $('#register-errors').empty();
                $(errorArray).each(function(index, value){ $('#register-errors').append('<li><img src="img/error.png" /><p>' + value + '</p></li>') });
                $('#register-errors').slideDown();

                if (errorArray.length == 0) {
                    axios.post('/users', { username: username, password: password, email: email })
                        .then((res) => {
                            console.log(res);
                            window.loginMessage = "success, you can now login!"
                            navigateTo('login', 'Login')
                        }).catch((err) => {
                            console.log(err)
                            $('#register-errors')[0].innerHTML = '<li><img src="img/error.png" /><p>' + err.response.data.message + '</p></li>';
                        })
                }

            })
        });
    }, 100);

    crossroads.addRoute('/settings', function () {
        axios.get('/users/' + session.id)
            .then(function (data) {
                var user = data.data
                $('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
                $('head').append($('<link rel="stylesheet" href="css/remodal.css">'));
                $('head').append($('<link rel="stylesheet" href="css/remodal-default-theme.css">'));
                $('.sidebar').load('sidebar/logged_in.html');
                $('.content').load('content/settings.html', function () {
                    $('#set')[0].href = user.username;
                    $('#username')[0].value = user.username;
                    $('#email')[0].value = user.email;
                    $('#firstname')[0].value = user.firstname;
                    $('#lastname')[0].value = user.lastname;
                    $('#teamspeakAddr')[0].value = user.teamspeak;
                    $('#DiscordAddr')[0].value = user.discord;
                    $('#descriptionText')[0].value = user.description;
                    $('#userID')[0].value = user.id;
                    $('#level')[0].value = user.level;
                });
            })
    }, 100);

    crossroads.addRoute('/games', function () {
        $('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
        $('head').append($('<link rel="stylesheet" href="css/remodal.css">'));
        $('head').append($('<link rel="stylesheet" href="css/remodal-default-theme.css">'));
        $('.sidebar').load(session.authenticated ? 'sidebar/logged_in.html' : 'sidebar/not_logged_in.html');
        $('.content').load('content/games.html');
    }, 100);

    crossroads.addRoute('/{username}', function (username) {
        axios.get('/users?username=' + username).then(function (data) {
            var user = data.data[0]
            $('head').append($('<link rel="stylesheet" href="css/mainLoginRegister.css">'))
            $('.sidebar').load(session.authenticated ? 'sidebar/logged_in.html' : 'sidebar/not_logged_in.html');
            $('.content').load('content/profile.html', function () {
                $('#usernameVal')[0].innerHTML = user.username;
                $('#emailVal')[0].innerHTML = user.email;
                $('#namelVal')[0].innerHTML = user.firstname + ' ' + user.lastname;
                $('#teamspeakVal')[0].innerHTML = user.teamspeak;
                $('#descriptionVal')[0].innerHTML = user.description;
                $('#discordVal')[0].innerHTML = user.discord;
                $('#levelVal')[0].innerHTML = user.level;
            });
        })
    }, 1);

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

    crossroads.parse(document.location.pathname);
});

var CheckFormInput = function(email, password, confirmation, username){
    var securePassword = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,99}$/;
    var validEmail = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/
    var errors = []

    if (!email.match(validEmail)) errors.push('Email should be a valid email')
    if (!password.match(securePassword)) errors.push('Password should have a minimum length of 6')
    if (!password.match(securePassword)) errors.push('Password should contain one lower and one uppercase letter')
    if (!password.match(securePassword)) errors.push('Password should contain one digit')
    if (password !== confirmation) errors.push('Password and confirmation should match')

    axios.get('/users?username=' + username).then(res => { if (res.data.length > 0) $('#register-errors')[0].innerHTML += '<li><img src="img/error.png" /><p> A user with username \'' + username +'\' already exists'})

    var parsedErrors = errors.reduce(function (prev, current) {
        if (prev) prev += "</p></li>"
        return prev += current
    }, "")

    return errors;
}








