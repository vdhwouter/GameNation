$(document).ready(function() {
  var refreshInterval = 2000
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

  function updateFriends() {
    var ul = $('.friendbar').children('#friends').children('ul')
    if (!session.authenticated || !session.id) {
      return ul.empty()
    }
    
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
        if (newUl.html() != ul.html()) {
          ul.html(newUl.html())
        }
      } else {
        friendBar.children('#friends').children('ul').text('you have no friends.')
      }
      
    })
  }


    var test = false
  function updateRequests() {
    updateSentrequests()

    var ul = $('.friendbar').children('#requests').children('ul')
    if (!session.authenticated || !session.id) {
      return ul.empty()
    }

    axios.get('/users/' + session.id + '/friendrequests?direction=to').then(response => {
      var requests = response.data

      if (requests.length > 0) {


        // render new list
        friendBar.children('#requests').show()
        var newUl = $('<ul></ul>')
        requests.forEach(request => {
          parseFriendrequest(request).appendTo(newUl)
        })

        //compare old list vs new list, only render if they differ
        if (newUl.html() != ul.html()) {
          ul.html(newUl.html())

          // Send desktop notifications
          requests.forEach(request => { GenerateNotification('You have a pending friendrequest from ' + request.username, request.avatar); })
        }




      } else {
        friendBar.children('#requests').hide()
      }
    })
  }
  
  function updateSentrequests() {
    var ul = $('.friendbar').children('#sent-requests').children('ul')
    if (!session.authenticated || !session.id) {
      return ul.empty()
    }

    axios.get('/users/' + session.id + '/friendrequests?direction=from').then(response => {
      var requests = response.data

      if (requests.length > 0) {
        // render new list
        friendBar.children('#sent-requests').show()
        var newUl = $('<ul></ul>')
        requests.forEach(request => {
          parseOutgoingRequest(request).appendTo(newUl)
        })

        //compare old list vs new list, only render if they differ
        if (newUl.html() != ul.html()) {
          ul.html(newUl.html())
        }
      } else {
        friendBar.children('#sent-requests').hide()
      }
    })
  }

  function parseFriend(friend) {
    return $(
      '<li class="person" data-user-id="' + friend.id + '">' +
                        '<div class="entry">' +
						// '<a href="/' + friend.username + '">' +
							'<div class="image">' +
								'<img src="' + friend.avatar +'" alt="">' +
							'</div>' +
							'<div class="name">' +
								'<span>' + friend.username + '</span>' +
							'</div>' +
						// '</a>' +
                        '</div>' +
					'</li>'
    )
  }

  function parseFriendrequest(friend) {
    return $(
      '<li class="person" data-user-id="' + friend.id + '">' +
            // '<a href="/' + friend.username + '">' +
            '<div class="entry">' +
              '<div class="actions">' +
                '<i class="fa fa-times decline" aria-hidden="true" data-user-id="' + friend.id + '" id="decline"></i>' +
                '<i class="fa fa-check accept" aria-hidden="true" data-user-id="' + friend.id + '" id="accept"></i>' +
              '</div>' +
              '<div class="image">' +
                '<img src="' + friend.avatar +'" alt="">' +
              '</div>' +
              '<div class="name">' +
                '<span>' + friend.username + '</span>' +
              '</div>' +
            // '</a>' +
            '</div>' +
          '</li>'
    )
  }

    function parseOutgoingRequest(friend) {
    return $(
      '<li class="person" data-user-id="' + friend.id +  '">' +
            // '<a href="/' + friend.username + '">' +
            '<div class="entry">' +
              '<div class="actions">' +
                '<i class="fa fa-times decline" aria-hidden="true" data-user-id="' + friend.id + '" id="cancel"></i>' +
              '</div>' +
              '<div class="image">' +
                '<img src="' + friend.avatar +'" alt="">' +
              '</div>' +
              '<div class="name">' +
                '<span>' + friend.username + '</span>' +
              '</div>' +
            // '</a>' +
            '</div>' +
          '</li>'
    )
  }

  // bind events to request accept
  $('.friendbar').on('click', '#requests #accept', function(e) {
    e.stopPropagation();
    var id = $(this).attr('data-user-id')
    console.log('accept', id)

    axios.post('/users/' + session.id + '/friends', {user: id})
      .then(function (res) {
          // try to update friend button if on profile page..
          updateFriendButton(res.data);
          updateRequests();
          updateFriends();
      })
  })

  // bind events to request decline
  $('.friendbar').on('click', '#requests #decline', function(e) {
    e.stopPropagation();
    var id = $(this).attr('data-user-id')
    console.log('decline', id)

    axios.delete('/users/' + session.id + '/friends/' + id)
      .then(function (res) {
          // try to update friend button if on profile page..
          updateFriendButton(res.data);
          updateRequests();
      })
  })

  // bind events to request cancel
  $('.friendbar').on('click', '#sent-requests #cancel', function(e) {
    e.stopPropagation();
    var id = $(this).attr('data-user-id')
    console.log('cancel', id)

    axios.delete('/users/' + session.id + '/friends/' + id)
      .then(function (res) {
          updateRequests();
      })
  })

  $('.friendbar').on('click', '.person', function (e) {
    var id = $(this).attr('data-user-id');
    axios.get('/users/' + id).then(function (response) {
        addChat(response.data);
    });
  });

  // if initial event was missed:
  session.authenticated ? show() : hide();
});