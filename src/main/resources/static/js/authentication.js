class Authentication2 {

    constructor() {
        // just to set axios headers
        this.token = this.token

        // trigger initial events
        this.triggerEvents()

        // check if token is still valid on page refresh and similar events
        this.checkAuthentication()
    }

    /* GETTERS */

    get id() {
        return this.user ? this.user.id : null
    }

    get user() {
        return JSON.parse(localStorage.getItem("__user"))
    }

    get token() {
        return localStorage.getItem("__token")
    }

    get refreshToken() {
        return localStorage.getItem("__refresh_token")
    }

    get authenticated() {
        return (this.token && !this.expired) ? true : false
    }

    get expired() {
        // bug..
        // return parseInt(localStorage.getItem("__expires")) <= new Date().getTime()
        return false;
    }

    /* SETTERS */

    set user(value) {
        localStorage.setItem("__user", JSON.stringify(value))
    }

    set token(value) {
        axios.defaults.headers.common['Authorization'] = value ? "Bearer " + value : null;
        localStorage.setItem("__token", value)

        // trigger login/logout events
        this.triggerEvents()
    }

    set refreshToken(value) {
        localStorage.setItem("__refresh_token", value)
    }

    set expires(value) {
        localStorage.setItem("__expires", new Date().getTime() + value)
    }

    set authenticated(value) {
        if (!value) {
            this.user = ""
            this.token = ""
            this.refreshToken = ""
        }
    }


    triggerEvents() {
        console.log('triggering events...')
        if (this.authenticated) {
            window.dispatchEvent(new Event('login'))
        } else {
            window.dispatchEvent(new Event('logout'))
        }
    }

    /* FUNCTIONS */

    setAuthenticationData(data) {
        this.token = data.access_token
        this.refreshToken = data.refresh_token
        this.expires = data.expires_in
    }

    login(username, password) {
        var params = new URLSearchParams();
        params.append('username', username);
        params.append('password', password);
        params.append('grant_type', 'password');

        return axios.post('/oauth/token', params, {
            baseURL: axios.defaults.baseURL.split('/api')[0],
            auth: {
                username: 'web-gamenation',
                password: '123456'
            }
        }).then((response) => {
            if (response.status === 200) {
                // on success: return user data
                console.info('LOGIN SUCCEEDED', response)
                this.setAuthenticationData(response.data)

                return this.fetchUser()

            } else {
                // on failure: return false
                console.warn('LOGIN FAILED', response)

                return Promise.reject(this.authenticated)
            }
        }).catch((error) => {
            console.warn('LOGIN FAILED WITH ERROR', error)

            return Promise.reject(false)
        })
    }

    fetchUser() {
        return axios.get('/users/me')
            .then(response => {
                return this.user = response.data
            })
            .catch(error => {
                console.log('fetching user failed, probably invalid token', error)
                return Promise.reject(this.authenticated = false)
            })
    }

    // TODO: post to server to deauth
    logout() {
        this.authenticated = false
    }

    // check Authentication
    checkAuthentication() {
        // check token by fetching user
        if (this.token) {
          return this.fetchUser().catch(error => this.loginWithRefreshToken())
        }

        if (this.refreshToken) {
          return this.loginWithRefreshToken().catch(error => false)
        }

        return Promise.resolve(false);
    }

    loginWithRefreshToken() {
        var params = new URLSearchParams();
        params.append('refresh_token', this.refreshToken);
        params.append('grant_type', 'refresh_token');

        return axios.post('/oauth/token', params, {
            baseURL: '',
            auth: {
                username: 'web-gamenation',
                password: '123456'
            }
        }).then((response) => {
            if (response.status === 200) {
                // on success: return user data
                console.info('TOKEN REFRESH SUCCEEDED', response)
                this.setAuthenticationData(response.data)

                return this.fetchUser()
            } else {
                // on failure: return false
                console.warn('TOKEN REFRESH FAILED', response)

                return Promise.reject(this.authenticated = false)
            }
        }).catch((error) => {
            console.warn('TOKEN REFRESH FAILED WITH ERROR', error)
            return Promise.reject(this.authenticated = false)
        })
    }
}

window.session = new Authentication2();


//interceptor to react when session is invalid
// Add a response interceptor
axios.interceptors.response.use(function(response) {
    // Do something with response data
    return response;
}, function(error) {


    console.log(this)
    console.log(error);

    // Do something with response error
    // if invalid token, just logout() session
    if (error.response && error.response.data && error.response.data.error == "invalid_token") {
        session.logout()
        error.config.headers.Authorization = null
        if (error.config.url.startsWith('/api')) {
          error.config.baseURL = ""
        }
        return Promise.resolve(axios.request(error.config))
    }

    return Promise.reject(error);
});
