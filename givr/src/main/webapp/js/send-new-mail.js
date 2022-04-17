const buttonSubmit = getButtonSubmit()

buttonSubmit.on('click', () => {
    $.ajax({
        method: 'GET',
        url: `${context}/api/register?email=${$("#email").val()}`
    }).done((response) => {
        const messagesText = response.messages

        if(messagesText) {
            getInfoElement().html(buildMessagesToList(messagesText))
            const boxContainer = getContainerAlert();
            setSuccessVisibility(boxContainer);
        }
    }).fail((xhr) => {
        const error = xhr.responseText
        const errorJson = JSON.parse(error);
        const messagesText = errorJson.messages

        if(messagesText) {
            getInfoElement().html(buildMessagesToList(messagesText))
            const boxContainer = getContainerAlert();
            setErrorVisibility(boxContainer);
        }
    })
})