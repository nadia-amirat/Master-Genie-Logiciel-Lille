
$(function() {

  $(".supprimer").click(function (e) {

    var ref = $(this).data("ref");

     $.ajax({
      method: 'post',
      url: "./" + ref + "/delete"
     }).done(function(){
       location.reload();
    });

     });


});
