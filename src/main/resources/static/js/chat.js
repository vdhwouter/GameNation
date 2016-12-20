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

        window.ws.onerror = function(e) {
            initWS();
        }

        window.ws.onclose = function(e) {
            initWS();
        }

        window.ws.onmessage = function (e) {
            var messages = JSON.parse(e.data);

            $.each(messages, function(i, msg) {
                var found = false;

                chat.children('.chat-list__item').each(function () {
                    if ($(this).data('user-id') == msg.sender) {
                        found = true;
                        $(this).children('.message-list').append($(
                            '<li class="message-list__item"><div class="message">' + msg.message + '</div></li>'
                        ));
                    } else if ($(this).data('user-id') == msg.receiver) {
                        found = true;
                        $(this).children('.message-list').append($(
                            '<li class="message-list__item"><div class="message message--me">' + msg.message + '</div></li>'
                        ));
                    }

                    var x = $(this).children('.message-list');
                    x.scrollTop(x[0].scrollHeight);
                });

                if (!found && msg.sender != window.session.id) {
                    axios.get('/users/' + msg.sender).then(function(response) {
                        addChatAndMessage(response.data, msg.message);
                    });
                }
            });
        }
    }

    function destroyWS() {
        window.ws.close();
        chat.empty();
    }
});

function addChat(user) {
    var exists = false;

    $('.chat-list__item').each(function() {
        if ($(this).data('user-id') == user.id) {
            exists = true;
        }
    });

    if (!exists) {
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
            $(this).parentsUntil('.chat-list').remove();
        }).on('keyup', '#message-input', function (e) {
            if (e.key == "Enter" && $(this).val() != '') {
                window.ws.send(JSON.stringify({
                    op: "chat",
                    d: JSON.stringify({
                        sender: window.session.id,
                        receiver: $(this).parentsUntil('.chat-list').data('user-id'),
                        message: $(this).val()
                    })
                }));

                $(this).parentsUntil('.chat-list').children('.message-list').append($(
                    '<li class="message-list__item message-list__item--me"><div class="message message--me">' + $(this).val() + '</div></li>'
                ));

                $(this).val('');
                var x = $(this).parentsUntil('.chat-list').children('.message-list');
                x.scrollTop(x[0].scrollHeight);
            }
        }));
    }
}

function addChatAndMessage(user, message) {
    $('.chat-list').append($(
        '<li class="chat-list__item" data-user-id="' + user.id + '">' +
            '<div class="chat-list__item__header">' +
                '<a href="' + user.username + '" class="chat-list__item__header__link">' + user.username + '</a>' +
                '<i class="fa fa-close fa-pull-right" id="close-chat"></i>' +
            '</div>' +
            '<ul class="message-list">' +
                '<li class="message-list__item"><div class="message">' + message + '</div></li>' +
            '</ul>' +
            '<input class="chat-list__item__input" type="text" id="message-input">' +
        '</li>'
    ).on('click', '#close-chat', function () {
        $(this).parentsUntil('.chat-list').remove();
    }).on('keyup', '#message-input', function (e) {
        if (e.key == "Enter" && $(this).val() != '') {
            window.ws.send(JSON.stringify({
                op: "chat",
                d: JSON.stringify({
                    sender: window.session.id,
                    receiver: $(this).parentsUntil('.chat-list').data('user-id'),
                    message: $(this).val()
                })
            }));

            $(this).parentsUntil('.chat-list').children('.message-list').append($(
                '<li class="message-list__item"><div class="message message--me">' + $(this).val() + '</div></li>'
            ));

            $(this).val('');
            var x = $(this).parentsUntil('.chat-list').children('.message-list');
            x.scrollTop(x[0].scrollHeight);
        }
    }));
}
