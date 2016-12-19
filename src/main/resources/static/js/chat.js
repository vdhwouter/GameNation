$(document).ready(function() {
    var chat = $('.chat-list');

    if (session.authenticated && window.ws == null)
        window.ws = new WebSocket('ws://' + document.location.host + '/chat?access_token=' + localStorage.getItem('__token'));

    $(window).on('login', function () {
        initWS();
    });

    $(window).on('logout', function () {
        destroyWS();
    });

    function initWS() {
        if (window.ws == null)
            window.ws = new WebSocket('ws://' + document.location.host + '/chat?access_token=' + localStorage.getItem('__token'));

        window.ws.onmessage = function (e) {
            var msg = JSON.parse(e.data);
            var found = false;

            chat.children('.chat-list__item').each(function () {
                if ($(this).data('user-id') == msg.sender) {
                    found = true;
                    $(this).children('.message-list').append($(
                        '<li class="message-list__item">' + msg.message + '</li>'
                    ));
                }
            });

            if (!found) {
                axios.get('/users/' + msg.sender).then(function(response) {
                    addChatAndMessage(response.data, msg.message);
                });
            }
        }
    }

    function destroyWS() {
        window.ws.close();
        chat.empty();
    }
});

function addChat(user) {
    $('.chat-list').append($(
        '<li class="chat-list__item" data-user-id="' + user.id + '">' +
        '<div class="chat-list__item__header">' +
        '<a href="' + user.username + '" class="chat-list__item__header__link">' + user.username + '</a>' +
        '<i class="fa fa-close fa-pull-right" id="close-chat"></i>' +
        '</div>' +
        '<ul class="message-list"></ul>' +
        '<input class="chat-list__item__input" type="text" id="message-input">' +
        '</li>'
    ).on('click', '#close-chat', function () {
        $(this).parentsUntil('.chat-list').empty();
    }).on('keyup', '#message-input', function (e) {
        if (e.key == "Enter") {
            window.ws.send(JSON.stringify({
                op: "chat",
                d: JSON.stringify({
                    sender: window.session.id,
                    receiver: $(this).parentsUntil('.chat-list').data('user-id'),
                    message: $(this).val()
                })
            }));

            $(this).parentsUntil('.chat-list').children('.message-list').append($(
                '<li class="message-list__item message-list__item--me">' + $(this).val() + '</li>'
            ));

            $(this).val('');
        }
    }));
}

function addChatAndMessage(user, message) {
    $('.chat-list').append($(
        '<li class="chat-list__item" data-user-id="' + user.id + '">' +
            '<div class="chat-list__item__header">' +
                '<a href="' + user.username + '" class="chat-list__item__header__link">' + user.username + '</a>' +
                '<i class="fa fa-close fa-pull-right" id="close-chat"></i>' +
            '</div>' +
            '<ul class="message-list">' +
                '<li class="message-list__item">' + message + '</li>' +
            '</ul>' +
            '<input class="chat-list__item__input" type="text" id="message-input">' +
        '</li>'
    ).on('click', '#close-chat', function () {
        $(this).parentsUntil('.chat-list').empty();
    }).on('keyup', '#message-input', function (e) {
        if (e.key == "Enter") {
            window.ws.send(JSON.stringify({
                op: "chat",
                d: JSON.stringify({
                    sender: window.session.id,
                    receiver: $(this).parentsUntil('.chat-list').data('user-id'),
                    message: $(this).val()
                })
            }));

            $(this).parentsUntil('.chat-list').children('.message-list').append($(
                '<li class="message-list__item message-list__item--me">' + $(this).val() + '</li>'
            ));

            $(this).val('');
        }
    }));
}
