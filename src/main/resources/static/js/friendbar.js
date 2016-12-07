$(document).ready(function() {
  var refreshInterval = 1000
  var friendBar = $('.friendbar');
  friendBar.hide()

  // load content into friendbar
  friendBar.load('content/friendbar/friendbar.html')


  friendBar.show = function() {
    friendBar.css('display', 'initial')
    friendBar.width("12rem")
  }

  friendBar.hide = function() {
    friendBar.width("0rem")
  }

  $(window).on('login', function() {
    show()
  })

  $(window).on('logout', function() {
    hide()
  })

  // functions to show/hide
  function show() {
    clearIntervals()

    // initial "show"
    updateFriends();
    updateRequests();

    setupIntervals();
    friendBar.show()
  }

  function hide() {
    clearIntervals()
    friendBar.hide()
  }

  var intervals = [];

  function clearIntervals() {
    intervals.forEach(interval => clearInterval(interval))
  }

  function setupIntervals() {
    var friendsInterval = setInterval(function() {
      updateFriends()
    }, refreshInterval);

    var requestsInterval = setInterval(function() {
      updateRequests()
    }, refreshInterval);

    intervals.push(friendsInterval);
    intervals.push(requestsInterval);
  }

  var friendsUlCache = null
  function updateFriends() {
    axios.get('/users/' + session.id + '/friends').then(response => {
      var friends = response.data
      
      var ul = $('.friendbar').children('#friends').children('ul')

      if (friends.length > 0) {
        // render new list
        var newUl = $('<ul></ul>')
         friends.forEach(friend => {
           parseFriend(friend).appendTo(newUl)
         })

        //compare old list vs new list, only render if they differ
         if (newUl.html() != friendsUlCache) {
           friendsUlCache = newUl.html()
           ul.html(friendsUlCache)
         }
      } else {
        friendBar.children('#friends').children('ul').text('you have no friends.')
      }
      
    })
  }
  
  var requestUlCache = null
  function updateRequests() {
    axios.get('/users/' + session.id + '/friendrequests?direction=to').then(response => {
      var requests = response.data

      var ul = $('.friendbar').children('#requests').children('ul')

      if (requests.length > 0) {
        // render new list
        friendBar.children('#requests').show()
        var newUl = $('<ul></ul>')
        requests.forEach(request => {
          parseFriend(request).appendTo(newUl)
        })

        //compare old list vs new list, only render if they differ
        if (newUl.html() != requestUlCache) {
          requestUlCache = newUl.html()
          ul.html(requestUlCache)
        }
      } else {
        friendBar.children('#requests').hide()
      }
    })
  }

  function parseFriend(friend) {
    return $(
      '<li class="person">' +
						'<a href="/' + friend.username + '">' +
							'<div class="image">' +
								'<img src="' + friend.avatar +'" alt="">' +
							'</div>' +
							'<div class="name">' +
								'<span>' + friend.username + '</span>' +
							'</div>' +
						'</a>' +
					'</li>'
    )
  }

  // if initial event was missed:
  session.authenticated ? show() : hide();
});