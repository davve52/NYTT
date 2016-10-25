$(document).ready(function(){
  $.ajax({
          url: "http://api-server-mah.herokuapp.com/image",
             cache: false,
            success: function(data) {
              console.log(data);
              /*
                var firstString = data.substring(10);
                var finalPath = firstString.substring(0,firstString.indexOf("'"));
                console.log(firstString + " ------------------ " + finalPath);
                $('#blurredIMG').attr('src', finalPath);
                $('input[name="image"]').val(finalPath);
                */
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
