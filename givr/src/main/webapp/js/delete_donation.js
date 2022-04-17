$(function(){
  $(".supprimer").click(function (e) {
    let donationId = $(this).data("ref");
    $.ajax({
      method: 'delete',
      url: "/api/donations/"+donationId,
      dataType: 'json'
    }).done(()=> {
      location.reload();
    })
      .fail((xhr) => {
          const error = xhr.responseText
          const errorJson = JSON.parse(error);
          const messagesText = errorJson.messages
          if(hasMessages(messagesText)) {
            getInfoElement().html(buildMessagesToList(messagesText))
            const boxContainer = getContainerAlert();
            setErrorVisibility(boxContainer);
          }
        }
      );
  });
});
