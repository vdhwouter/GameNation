logInWithFacebook = function(link) {
    var message = "Hi there! Come and join me on gamenamtion!";

    // Facebook login api call (with permission for publish action
    FB.login(function(response) {
        if (response.authResponse) console.info('Facebook login: successfull')
        else console.error('Facebook login: failed')
    }, {scope: 'publish_actions'});

    // Get access token and post message to facebook
    FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
            accesstoken = response.authResponse.accessToken;

            FB.api(
                '/me/feed',
                'POST',
                {"message": message, 'link': link},
                function(response) { console.log('Facebook post: ' + response) }
            );
        }
    });

    return false;
};

// Facebook init
window.fbAsyncInit = function() {
    FB.init({
        appId: '1813825935563940',
        cookie: true, // This is important, it's not enabled by default
        version: 'v2.2'
    });
};

// Facebook API
(function(d, s, id){
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));