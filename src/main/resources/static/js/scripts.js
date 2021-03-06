$(document).ready(function () {
    'use strict';

    /***************************************/
    /***             ROUTES              ***/
    /***************************************/



        /*********************/
        /**     Root        **/
        /*********************/

    crossroads.addRoute('/', function () {
        navigateTo('/login', 'Login')
    }, 100);



        /*********************/
        /**     Me          **/
        /*********************/

    crossroads.addRoute('/me', function() {
        crossroads.parse(session.user.username);
    }, 100);



        /*********************/
        /**     Logout      **/
        /*********************/

    crossroads.addRoute('/logout', function() {
        session.logout();
        navigateTo('login', 'Login');
    }, 100);



        /*********************/
        /**     Login       **/
        /*********************/

    crossroads.addRoute('/login', function () {
        $('.sidebar').load('sidebar/not_logged_in.html');
        $('.content').load('content/login.html', function () {
            if (session.authenticated) {
                console.info('already authenticated!, redirecting to profile...')
                return navigateTo(session.user.username, session.user.username)
            }

            if (window.loginMessage) {
                $('.error-list').append('<li class="error-list__item"><i class="error-list__item__icon fa fa-info-circle"></i><p class="error-list__item__text">' + window.loginMessage + '</p></li>');
                window.loginMessage = null
            }

            $('.form--login').on('submit', function (e) {
                e.preventDefault()
                var username = $('.form__input[type=text]').val()
                var password = $('.form__input[type=password]').val()
                session.login(username, password)
                    .then(function (user) {
                        console.info('successfully authenticated, redirecting to profile...')
                        axios.get('/users?username=' + username).then(function (data) { navigateTo(data.data[0].username, data.data[0].username) })
                    })
                    .catch(function (error) {
                        console.warn('bad credentials!')
                        $('.error-list').append('<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle"></i><p class="error-list__item__text">Bad Credentials!</p></li>');
                    })
            })
        });
    }, 100);



        /*********************/
        /**     Register    **/
        /*********************/

    crossroads.addRoute('/register', function () {
        $('.sidebar').load('sidebar/not_logged_in.html');
        $('.content').load('content/register.html', function () {
            if (session.authenticated) {
                console.info('already authenticated! no need to register - redirecting to profile...')
                return navigateTo(session.user.username, session.user.username)
            }

            $('.form--register').on('submit', function (e) {
                e.preventDefault()
                var username = $('.form__input[name=username]').val()
                var email = $('.form__input[name=email]').val()
                var password = $('.form__input[name=password]').val()
                var confirmation = $('.form__input[name=confirmation]').val()

                // Return error array as list items
                var errorArray = CheckFormInputRegister(email, password, confirmation, username);
                $('.error-list').empty();
                $(errorArray).each(function(index, value){ $('.error-list').append('<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle" aria-hidden="true"></i><p class="error-list__item__text">' + value + '</p></li>') });
                $('.error-list').slideDown();

                if (errorArray.length == 0) {
                    axios.post('/users', { username: username, password: password, email: email })
                        .then((res) => {
                            console.log(res);
                            window.loginMessage = "success, you can now login!"
                            navigateTo('login', 'Login')
                        }).catch((err) => {
                            console.log(err)
                            $('.error-list')[0].innerHTML = '<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle"></i><p class="error-list__item__text">' + err.response.data.message + '</p></li>';
                        })
                }

            })
        });
    }, 100);



        /*********************/
        /**     Settings    **/
        /*********************/

    crossroads.addRoute('/settings', function () {
        axios.get('/users/' + session.id)
            .then(function (data) {
                var user = data.data
                $('.sidebar').load('sidebar/logged_in.html');
                $('.content').load('content/settings.html', function () {
                    if (!session.authenticated) {
                        console.info('How dare you access this page without authenticating, i will have to redirect you!');
                        return  navigateTo('login', 'Login');
                    }

                    $('#first_name')[0].value = user.firstname;
                    $('#last_name')[0].value = user.lastname;
                    $('#username')[0].value = user.username;
                    $('#email')[0].value = user.email;
                    $('#teamspeak')[0].value = user.teamspeak;
                    $('#discord')[0].value = user.discord;
                    $('#description')[0].value = user.description;
                    $('#user_id')[0].value = user.id;
                    $("#previewAvatar").attr("src", user.avatar);
                    $("#avatar")[0].value = user.avatar;
                });
            })
    }, 100);



        /*********************/
        /**     Games       **/
        /*********************/

    crossroads.addRoute('/games', function () {
        $('.sidebar').load(session.authenticated ? 'sidebar/logged_in.html' : 'sidebar/not_logged_in.html');
        $('.content').load('content/games.html');
    }, 100);



        /*********************/
        /**     Users       **/
        /*********************/

    crossroads.addRoute('/users', function () {
        $('.sidebar').load(session.authenticated ? 'sidebar/logged_in.html' : 'sidebar/not_logged_in.html');
        $('.content').load('content/users.html');
    }, 100);



        /*********************/
        /**     Username    **/
        /*********************/

    crossroads.addRoute('/{username}', function (username) {
        axios.get('/users?username=' + username).then(function (data) {
            var user = data.data[0]

            // HACK : THIS SHOULD CHANGE...
            window.profileUser = user

            $('.sidebar').load(session.authenticated ? 'sidebar/logged_in.html' : 'sidebar/not_logged_in.html');
            $('.content').load('content/profile.html', function () {
                $('#username').text(user.username);
                $('#fullname').text(user.firstname) + ' ' + user.lastname;
                $('#email').text(user.email);
                $('#teamspeak').text(user.teamspeak);
                $('#discord').text(user.discord);
                $('#description').text(user.description);
                $('#level').text(user.level);
                $("#previewAvatar").attr("src", user.avatar);
            });
        })
    }, 1);



    /***************************************/
    /***             Notifications       ***/
    /***************************************/


    // Ask for permisison
    if (Notification.permission !== "granted") Notification.requestPermission();


    /***************************************/
    /***             Events              ***/
    /***************************************/

    $('body').on('click', 'a', function (e) {
        var urlPath = $(this).attr('href');
        e.preventDefault();
        var title = $(this).text();
        return History.pushState({ urlPath: urlPath }, title, urlPath);
    });



    /***************************************/
    /***             History             ***/
    /***************************************/

    var History, State;
    History = window.History;

    if (History.enabled) {
        State = History.getState();
        History.pushState({ urlPath: window.location.pathname }, $('title').text(), State.urlPath);
    }

    History.Adapter.bind(window, 'statechange', function () {
        return crossroads.parse(document.location.pathname);
    })

    window.navigateTo = function navigateTo(urlPath, title = 'GameNation') {
        if (title) title = title + ' | GameNation'
         History.pushState({ urlPath: urlPath }, title, urlPath)
    }

    crossroads.parse(document.location.pathname);
});





