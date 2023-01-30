function validateRegForm(){

	var firstName = $("#firstName").val();
	var lastName = $("#lastName").val();
	var email = $("#email").val();
	var password = $("#password").val();
	var dateOfBirthStr = $("#dateOfBirth").val();
	var address = $("#address").val();
	var city = $("#city").val();
	var state = $("#state").val();
	var zipcode = $("#zipcode").val();
	var country = $("#country").val();
	
	var dateOfBirth = new Date(dateOfBirthStr);
	
	
	
	var regEmail=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;
	var date_regex = /^(0[1-9]|1[0-2])\/(0[1-9]|1\d|2\d|3[01])\/(19|20)\d{2}$/g;
	
	
	
	var returnValue = true;

	if (firstName === ""){
		$("#firstNameError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#firstNameError").css({"visibility": "hidden"});
	};

	if (lastName === ""){
		$("#lastNameError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#lastNameError").css({"visibility": "hidden"});
	};

	if (email === "" || !regEmail.test(email)){
		$("#emailError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#emailError").css({"visibility": "hidden"});
	};

	if (password === ""){
		$("#passwordError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#passwordError").css({"visibility": "hidden"});
	};
	
	if (address === ""){
		$("#addressError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#addressError").css({"visibility": "hidden"});
	};
	
	if (city === ""){
		$("#cityError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#cityError").css({"visibility": "hidden"});
	};
	
	if (state === ""){
		$("#stateError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#stateError").css({"visibility": "hidden"});
	};
	
	if (zipcode === ""){
		$("#zipcodeError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#zipcodeError").css({"visibility": "hidden"});
	};
	
	if (country === ""){
		$("#countryError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#countryError").css({"visibility": "hidden"});
	};
	
	
	
	var today = new Date();
	var validMinDate = new Date(
		today.getFullYear()-18,
		today.getMonth(),
		today.getDate(),
		today.getHours(),
		today.getMinutes()
	);
	
	 if(dateOfBirth>validMinDate){
		$("#birthdayError").css({"visibility": "visible"});
		returnValue = false
	}else{
		$("#birthdayError").css({"visibility": "hidden"});
	}
	 
	 

	
	
	return returnValue;
};