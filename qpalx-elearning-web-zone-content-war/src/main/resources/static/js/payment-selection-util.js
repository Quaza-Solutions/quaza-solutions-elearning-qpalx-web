$(document).ready(function(){
$("#radio-1").click(function(){
$("#form-text").prop('disabled', true);
$("#form-text-1").prop('disabled', true);
$("#form-text-2").prop('disabled', false);
});
$("#radio-2").click(function(){
$("#form-text").prop('disabled', false);
$("#form-text-1").prop('disabled', false);
$("#form-text-2").prop('disabled', true);
});
});

function displayCheckmark1(){
	$("#two").empty();

if (document.getElementById('check-box-one').checked) {

	var fontAwe = $('<i class=\"fa fa-check-circle fa-2x checkmark1\" aria-hidden=\"true\"></i>')
	$('#one').append(fontAwe);
}
}
function displayCheckmark2(){
		$("#one").empty();

if (document.getElementById('check-box-two').checked) {

	var fontAwe = $('<i class=\"fa fa-check-circle fa-2x checkmark1\" aria-hidden=\"true\"></i>')
	$('#two').append(fontAwe);
}
}


$('.radio-btn').click(function() {
		$('.row').removeClass("hvr");
		$(this).filter(':checked').closest('.row').addClass("hvr");
		});
$('.radio-btn-1').click(function() {
		$(".red-2").empty();
		var txt1 = "Credit card coming soon!";
		var newDiv1 = $('<div style=\"color: red;\"></div>').text(txt1);
		$('.red').append(newDiv1);
		});
$('.radio-btn-2').click(function() {
		$(".red").empty();
		var txt2 = "PayPal coming soon!";
		var newDiv2 = $('<div style=\"color: red;\"></div>').text(txt2);
		$('.red-2').append(newDiv2);
		});