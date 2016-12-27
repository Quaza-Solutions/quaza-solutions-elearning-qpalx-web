    $(document).ready(function() {

      $('.progressbar').each(function() {
        var progressbar = $(this),
          progressbarValue = progressbar.next(),
          value = progressbar.data('min') || 0,
          max = progressbar.data('max'),
          //time = (1000 / max) * 5,
		  time = (500 / max) * 5,
          value = progressbar.val(),
          animate = setInterval(loading, time);

	function loading() {
          progressbar.val(value);
          progressbarValue.html(value + '%');
          if (value >= max) {
            clearInterval(animate);
          } else {
            value += 1;
          }
        };
      })
	  
    });