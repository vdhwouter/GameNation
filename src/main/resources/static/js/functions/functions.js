var CheckFormInputRegister = function(email, password, confirmation, username){
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





var CheckFormInputSettings = function(email, password, username, id){
	var securePassword = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,99}$/;
	var validEmail = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	var errors = []

	if (!email.match(validEmail)) errors.push('Email should be a valid email');

	axios.get('/users?username=' + username).then(res => { if (res.data.length > 0) $('.error-list').append('<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle" aria-hidden="true"></i><p class="error-list__item__text"> A user with username \'' + username +'\' already exists</p></li>')});

	var parsedErrors = errors.reduce(function (prev, current) {
		if (prev) prev += "</p></li>"
		return prev += current
	}, "")

	return errors;
}

