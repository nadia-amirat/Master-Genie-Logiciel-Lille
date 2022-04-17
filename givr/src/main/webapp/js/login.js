const buttonSubmit = getButtonSubmit()
buttonSubmit.on('click', () => {
    $.ajax({
        method: 'POST',
        url:  `${context}/api/authenticate`,
        dataType: "json",
        contentType: 'application/json',
        data: JSON.stringify({
            email: $("#email").val(),
            password: $("#password").val()
        })
    }).done(() => {
        document.location.href=`${context}/`;
    }).fail((xhr) => {
        const error = xhr.responseText
        const errorJson = JSON.parse(error);
        const message = errorJson.messages

        if(hasMessages(message)) {
            const boxContainer = getContainerAlert();
            setErrorVisibility(boxContainer)

            getInfoElement().html(buildMessagesToList(message));
        }
    })
})