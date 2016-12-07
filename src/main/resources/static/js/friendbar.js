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
    console.log('login')
    show()
  })

  $(window).on('logout', function() {
    console.log('logout')
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

  function updateFriends() {
    axios.get('/users/' + session.id + '/friends').then(response => {
      var friends = response.data
      // console.log('friends:', friends)
      
      // clear list
      var ul = $('.friendbar').children('#friends').children('ul')
      ul.empty()

      if (friends.length > 0) {
         friends.forEach(friend => {
           parseFriend(friend).appendTo(ul)
         })
      } else {
        friendBar.children('#friends').children('ul').text('you have no friends.')
      }
      
    })
  }
  
  function updateRequests() {
    axios.get('/users/' + session.id + '/friendrequests?direction=to').then(response => {
      var requests = response.data
      // console.log('requests:', requests)
      
      // clear list
      var ul = $('.friendbar').children('#requests').children('ul')
      ul.empty()

      if (requests.length > 0) {
        friendBar.children('#requests').show()
        requests.forEach(request => {
          parseFriend(request).appendTo(ul)
        })
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
  console.log(session.authenticated)
  console.log(friendBar)
  session.authenticated ? show() : hide();
});