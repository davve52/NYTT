$(document).ready(function(){
  $.ajax({
          url: "http://api-server-mah.herokuapp.com/image",
             cache: false,
             dataType: "json",
            success: function(data) {
              console.log(data);
              if (data.imageData) {
                var resp = data.imageData;
                var image;
                var bid;
                for ( var i in resp) {
    		            image = resp[i].image;
    		            bid = resp[i].highBid;
                }
                  var firstString = image.substring(10);
                  var finalPath = firstString.substring(0,firstString.indexOf("'"));

                  $('#blurredIMG').attr('src', finalPath);
                  $('input[name="image"]').val(finalPath);
                  $('#highBid').text("HÖGSTA BUD: 0");
              }else if (data.responseData) {
                var resp = data.responseData;
                var image;
                var bid;
                for ( var i in resp) {
    		            image = resp[i].image;
    		            bid = resp[i].highBid;
                }
                  var firstString = image.substring(10);
                  var finalPath = firstString.substring(0,firstString.indexOf("'"));

                  $('#blurredIMG').attr('src', finalPath);
                  $('input[name="image"]').val(finalPath);
                  $('#highBid').text("HÖGSTA BUD: " +bid);
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
      Location.reload();  
      console.log(data);
    });
});
