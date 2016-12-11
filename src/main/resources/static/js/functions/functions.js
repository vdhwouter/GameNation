
// Regular Expressions
var validEmail = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;


// Method called from register route
var CheckFormInputRegister = function(email, password, confirmation, username){
    var errors = [];
    if (password !== confirmation) errors.push('Password and confirmation should match');
    return CommonChecks(email, password, username, errors);
}


// Method called from settings route
var CheckFormInputSettings = function(email, password, username){
    var errors = [];
	return CommonChecks(email, password, username, errors);
}


// Commen checks betwee two formchecks
var CommonChecks = function(email, password, username, errors){

    // Checks on password
	if (password != null){
	    if (password.length < 6) errors.push('Password should have a minimum length of 6');
        if (!password.match(/[a-z]/)) errors.push('Password should contain one lowercase letter');
        if (!password.match(/[A-Z]/)) errors.push('Password should contain one uppercase letter');
        if (!password.match(/\d/)) errors.push('Password should contain one digit');
	}

	// Check on email
    if (!email.match(validEmail)) errors.push('Email should be a valid email');

    // Check on password
    axios.get('/users?username=' + username).then(res => { if (res.data.length > 0) errors.push('<li class="error-list__item"><i class="error-list__item__icon fa fa-times-circle" aria-hidden="true"></i><p class="error-list__item__text"> A userrrrrrrrrdqfsfsdf with username \'' + username +'\' already exists</p></li>')});

    // Return all errors
    return errors;
}
