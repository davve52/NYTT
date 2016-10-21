$(document).ready(function(){
    $.ajax({
          url:"http://localhost:4567/image" ,
          })
          .done(function (data) {
            console.log(data);
            var res = data.substring(10,100);
            console.log(res);
            $('#blurredIMG').attr('src', res);
        });
});
