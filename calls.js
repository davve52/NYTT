$(document).ready(function(){
  $.ajax({
          url: "http://api-server-mah.herokuapp.com/image",
             cache: false,
             dataType: "json",
            success: function(data) {
              console.log(data);



            var resp = data.responseData;

            for ( var i in resp) {
		      var image = resp[i].image;
		      var bid = resp[i].highBid;
		          console.log(image);
		          console.log(bid);
                var firstString = image.substring(10);
                var finalPath = firstString.substring(0,firstString.indexOf("'"));
                $('#blurredIMG').attr('src', finalPath);
                $('input[name="image"]').val(finalPath);

                $('#highBid').text("HÖGSTA BUD:" +bid);

	}

            },
               error: function (request, status, error) {
                alert(status + ", " + error);

            }
        });
});

$("#sendBid").on("click", function(){
    var bid = $('#bidValue').val();
    var email = $('#email').val();
    var image = $('#image').val();
    $.ajax({
        url: "http://api-server-mah.herokuapp.com/bid?bidValue=" + bid +"&email="+ email + "&image="+image,
          cache: false,
    }).done(function(data){
      console.log(data);
    });
});
