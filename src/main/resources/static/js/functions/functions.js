

var validEmail = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;


var CheckFormInputRegister = function(email, password, confirmation, username){
    var errors = []

    if (!email.match(validEmail)) errors.push('Email should be a valid email')
    if (password.length < 6) errors.push('Password should have a minimum length of 6')
    if (!password.match(/[a-z]/)) errors.push('Password should contain one lowercase letter')
    if (!password.match(/[A-Z]/)) errors.push('Password should contain one uppercase letter')
    if (!password.match(/\d/)) errors.push('Password should contain one digit')
    if (password !== confirmation) errors.push('Password and confirmation should match')

    axios.get('/users?username=' + username).then(res => { if (res.data.length > 0) $('.error-list').append('<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle" aria-hidden="true"></i><p class="error-list__item__text"> A user with username \'' + username +'\' already exists</p></li>')});

    return errors;
}


var CheckFormInputSettings = function(email, password, username){
    var errors = []

	if (!email.match(validEmail)) errors.push('Email should be a valid email');
	if (password != null){
	    if (password.length < 6) errors.push('Password should have a minimum length of 6')
        if (!password.match(/[a-z]/)) errors.push('Password should contain one lowercase letter')
        if (!password.match(/[A-Z]/)) errors.push('Password should contain one uppercase letter')
        if (!password.match(/\d/)) errors.push('Password should contain one digit')
	}


	axios.get('/users?username=' + username).then(res => { if (res.data.length > 0) $('.error-list').append('<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle" aria-hidden="true"></i><p class="error-list__item__text"> A user with username \'' + username +'\' already exists</p></li>')});

	return errors;
}

