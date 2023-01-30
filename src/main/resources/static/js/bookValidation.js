function validateBook(){
	
	var name = $("#name").val();
	var publisher = $("#publisher").val();
	var author = $("#author").val();
	var description = $("#description").val();
	var language = $("#language").val();
	var publishedYear = $("#publishedYear").val();
	var pageNumber = $("#pageNumber").val();
	var price = $("#price").val();
	var cover = $("#cover").val();
	var letter = $("#letter").val();
	var genreId = $("#genreId").val();
	
	var returnValue = true;
	var publishedYearNum = Number(publishedYear);
	var pageNumberNum = Number(pageNumber);
	var priceNum = Number(price);
	
	console.log(genreId);

	if (name === ""){
		$("#nameError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#nameError").css({"visibility": "hidden"});
	};
	
	if (publisher === ""){
		$("#publisherError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#publisherError").css({"visibility": "hidden"});
	};
	
	if (author === ""){
		$("#authorError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#authorError").css({"visibility": "hidden"});
	};
	
	if (description === ""){
		$("#descriptionError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#descriptionError").css({"visibility": "hidden"});
	};
	
	if (language === ""){
		$("#languageError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#languageError").css({"visibility": "hidden"});
	};
	
	
	var d = new Date();
	var currentYear = d.getFullYear();
	if(publishedYear === "" || publishedYearNum > currentYear){
		$("#publishedYearError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#publishedYearError").css({"visibility": "hidden"});
	};
	
	
	if(pageNumber === "" || pageNumberNum < 10){
		$("#pageNumberError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#pageNumberError").css({"visibility": "hidden"});
	};
	
	if(price === "" || priceNum < 5){
		$("#priceError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#priceError").css({"visibility": "hidden"});
	};
	
	if(cover === ""){
		$("#coverError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#coverError").css({"visibility": "hidden"});
	};
	
	if(letter === ""){
		$("#letterError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#letterError").css({"visibility": "hidden"});
	};
	
	if(genreId === ""){
		$("#genreError").css({"visibility": "visible"});
		returnValue = false;
	}else{
		$("#genreError").css({"visibility": "hidden"});
	};
	
	return returnValue;
	
}