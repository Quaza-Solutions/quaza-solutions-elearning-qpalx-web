$('.floor').blur(function() {
	$("#floor").empty();
	$("#ceiling").empty();
	var inp1 = $(".floor").val();
	var inp2 = $(".ceiling").val();
	if (inp1 >= inp2){
		var txt3 = "Floor must be less than ceiling";
		var newDiv3 = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt3);
		
		$('#floor').append(newDiv3);
	}
	});
$('.ceiling').blur(function() {
	$("#floor").empty();
	$("#ceiling").empty();
	var inp1 = $(".floor").val();
	var inp2 = $(".ceiling").val();
	if (inp1 >= inp2){
		var txt3 = "Floor must be less than ceiling";
		var newDiv3 = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt3);
		
		$('#ceiling').append(newDiv3);
	}
	});
$('#first').blur(function() {
	$("#first-name").empty();
	var inp = $("#first").val();
if ( $.trim(inp).length == 0 )
{
   $(this).removeClass("blur-2").addClass("focus-2");
   		var txt3 = "Please don't leave the field blank";
		var newDiv3 = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt3);
		
		$('#first-name').append(newDiv3);
	  // Prevent form submission
      event.preventDefault();
         } else {
		$(this).removeClass("focus-2").addClass("blur-2");
         }
							   });

$('.length').blur(function() {
	$(".course-length").empty();
	var inp = $(".length").val();
if ( $.trim(inp).length == 0 )
{
   $(this).removeClass("blur-2").addClass("focus-2");
   		var txt3 = "Please don't leave the field blank";
		var newDiv3 = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt3);
		
		$('.course-length').append(newDiv3);
	  // Prevent form submission
      event.preventDefault();
         } else {
		//$("#first-name").empty();
		$(this).removeClass("focus-2").addClass("blur-2");
         }
							   });

$('#name').blur(function() {
	$(".name").empty();
	var inp = $("#name").val();
if ( $.trim(inp).length == 0 )
{
   $(this).removeClass("blur-2").addClass("focus-2");
   		var txt3 = "Please don't leave the field blank";
		var newDiv3 = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt3);
		
		$('.name').append(newDiv3);
	  // Prevent form submission
      event.preventDefault();
         } else {
		//$("#first-name").empty();
		$(this).removeClass("focus-2").addClass("blur-2");
         }
							   });
$('#duration').blur(function() {
	$(".minutes").empty();
	var inp = $("#duration").val();
if ( $.trim(inp).length == 0 )
{
   $(this).removeClass("blur-2").addClass("focus-2");
   		var txt3 = "Please don't leave the field blank";
		var newDiv3 = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt3);
		
		$('.minutes').append(newDiv3);
	  // Prevent form submission
      event.preventDefault();
         } else {
		//$("#first-name").empty();
		$(this).removeClass("focus-2").addClass("blur-2");
         }
							   });

$('#zip').blur(function() {
	$("#zip-code").empty();
	var inp = $("#zip").val();
if ( $.trim(inp).length == 0 )
{
   $(this).removeClass("blur-2").addClass("focus-2");
   		var txt3 = "Please don't leave the field blank";
		var newDiv3 = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt3);
		
		$('#zip-code').append(newDiv3);
	  // Prevent form submission
      event.preventDefault();
         } else {
		//$("#zip-code").empty();
		$(this).removeClass("focus-2").addClass("blur-2");
         }
							   });

$('#phone').blur(function() {
	$("#phone-number").empty();
	var inp = $("#phone").val();
if ( $.trim(inp).length == 0 )
{
   $(this).removeClass("blur-2").addClass("focus-2");
   		var txt3 = "Please don't leave the field blank";
		var newDiv3 = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt3);
		
		$('#phone-number').append(newDiv3);
	  // Prevent form submission
      event.preventDefault();
         } else {
		//$("#phone-number").empty();
		$(this).removeClass("focus-2").addClass("blur-2");
         }
							   });

$('#last').blur(function() {
	$("#last-name").empty();
	var inp = $("#last").val();
if ( $.trim(inp).length == 0 )
{
   $(this).removeClass("blur-2").addClass("focus-2");
   		var txt4 = "Please don't leave the field blank";
		var newDiv4 = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt4);
		
		$('#last-name').append(newDiv4);
	  // Prevent form submission
      event.preventDefault();
         } else {
		//$('#last-name').empty();
		$(this).removeClass("focus-2").addClass("blur-2");
         }
		   });

$('#description').blur(function() {
	$(".description").empty();
	var inp = $("#description").val();
if ( $.trim(inp).length == 0 )
{
   $(this).removeClass("blur-2").addClass("focus-2");
   		var txt4 = "Please don't leave the field blank";
		var newDiv4 = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt4);
		
		$('.description').append(newDiv4);
	  // Prevent form submission
      event.preventDefault();
         } else {
		$(this).removeClass("focus-2").addClass("blur-2");
         }
							 							   });

   $('.email').blur(function() {
	$("#tooltip-1").empty();
	var input = $(".email").val();
  if (!validateEmail($(this).val()) || ( $.trim(input).length == 0 )) {
	  $(this).removeClass("blur").addClass("focus");
	  var txt = "Please enter a valid email address.";
		var newDiv = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt);
		
		$('#tooltip-1').append(newDiv);
	  // Prevent form submission
      event.preventDefault();
         } else {
			 	//$("#tooltip-1").empty();
				 $(this).removeClass("focus").addClass("blur");
         }
})
   $('.email-2').blur(function() {
	$(".tooltip-2").empty();
	var input = $(".email-2").val();
  if (!validateEmail($(this).val()) || ( $.trim(input).length == 0 )) {
	  $(this).removeClass("blur").addClass("focus");
	  var txt = "Please enter a valid email address.";
		var newDiv = $('<div style=\"position: absolute; top: 0px; right: 0px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt);
		
		$('.tooltip-2').append(newDiv);
	  // Prevent form submission
      event.preventDefault();
         } else {
				 $(this).removeClass("focus").addClass("blur");
				 //$(".tooltip-2").empty();
         }
})
   
  $('.email-3').blur(function() {
	var input2 = $(".email-3").val();
  if (!validateEmail($(this).val()) || ( $.trim(input2).length == 0 )) {
	  $(this).removeClass("blur-3").addClass("focus-3");
	  alert("Please enter a valid email address");
	  // Prevent form submission
      event.preventDefault();		
         } else {
				 $(this).removeClass("focus-3").addClass("blur-3");
         }
})
  
  $('.email-4').blur(function() {
	var input2 = $(".email-4").val();
  if (!validateEmail($(this).val()) || ( $.trim(input2).length == 0 )) {
	  $(this).removeClass("blur-3").addClass("focus-3");
	  alert("Please enter a valid email address");
	  // Prevent form submission
      event.preventDefault();		
         } else {
				 $(this).removeClass("focus-3").addClass("blur-3");
         }
})
  
  
 $('.not-blank').blur(function() {
	var input4 = $(".not-blank").val();
if ( $.trim(input4).length == 0 )
{
   $(this).removeClass("blur-3").addClass("focus-3");
   	alert("Please do not leave this field blank.");
	// Prevent form submission
    event.preventDefault();
         } else {
$(this).removeClass("focus-3").addClass("blur-3");
         }
							   }); 
  function validateEmail(emailaddress) {
     var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
     if (emailReg.test(emailaddress) == false) {
         return false;
     } else {
         return true;
     }
   }
   
$('#password-2').blur(function() {
		$("#password-confirm").empty();
		var txt2 = "Please enter the same password";
		var newDiv2 = $('<div style=\"position: absolute; top: 0px; right: -300px; background-color: #444; padding: 6px; width: 180px; height: 60px; color: #fff;\"></div>').text(txt2);
		
		   if($('#password-1').val() != $('#password-2').val()) {
		  $(this).removeClass("blur").addClass("focus");
		
		$('#password-confirm').append(newDiv2);
            // Prevent form submission
            event.preventDefault();
        } else {
			
			 $(this).removeClass("focus").addClass("blur");
			  	//$("#password-confirm").empty();						  
				}
							   })
				
  