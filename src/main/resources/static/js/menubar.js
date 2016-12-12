$(document).ready(function () {
    var max_users = 5;

    var users;
    var scores = [];

    axios.get('/users').then(response => {users = response.data;})

    $('#menu_search').on('keyup', function() {
        scores.length = 0;
        var q = $(this).val();

        $.each(users, function() {
            console.log(this.username);
            var score = LiquidMetal.score(this.username, q);

            if (score > 0) {
                scores.push([score, this]);
            }
        });

        if (scores.length) {
            var ul = $('.search-list')[0];
            $(ul).empty();

            $.each(scores.sort(function(a, b) {return b[0] - a[0];}), function(i) {
                if (i < max_users) {
                    parseUser(this[1]).appendTo(ul);
                }
            });
        }
    });

    function parseUser(user) {
        return $(
            '<li class="search-list__item">' +
            '<a class="search-list__item__link" href="' + user.username + '">' +
            '<img class="search-list__item__image" src="' + user.avatar + '">' +
            '<span class="search-list__item__text">' + user.username + '</span>' +
            '</a>' +
            '</li>'
        );
    }
});

