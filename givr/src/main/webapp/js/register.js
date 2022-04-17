const buttonSubmit = getButtonSubmit()

buttonSubmit.on('click', () => {
    $.ajax({
        method: 'POST',
        url: `${context}/api/register`,
        dataType: "json",
        contentType: 'application/json',
        data: JSON.stringify({
            email: $("#email").val(),
            password: $("#password").val(),
            firstname: $("#firstname").val(),
            lastname: $("#lastname").val(),
            address: $("#address").val()
        })
    }).done((response) => {
        const messagesArray = response.messages

        if(hasMessages(messagesArray)) {
            getInfoElement().html(buildMessagesToList(messagesArray))
            const boxContainer = getContainerAlert();
            setSuccessVisibility(boxContainer);
        }
    }).fail((xhr) => {
        const error = xhr.responseText
        const errorJson = JSON.parse(error);
        const messagesText = errorJson.messages

        if(hasMessages(messagesText)) {
            getInfoElement().html(buildMessagesToList(messagesText))
            const boxContainer = getContainerAlert();
            setErrorVisibility(boxContainer);
        }
    })
})