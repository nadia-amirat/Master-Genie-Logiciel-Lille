const buttonSubmit = getButtonSubmit()

buttonSubmit.on('click', (event) => {
    event.preventDefault();

    const formData = new FormData();
    $.each($("input[type=file]"), (i, obj) => {
        $.each(obj.files, (i, file) => {
            formData.append(`images[${i}]`, file)
        })
    });

    formData.append("title", $("#title").val())
    formData.append("description", $("#description").val())

    $.ajax({
        method: 'PUT',
        url: `${context}/api/donations/`+$("#donation-id").val(),
        contentType: false,
        processData: false,
        cache: false,
        enctype: 'multipart/form-data',
        data: formData
    }).done((response) => {
        const messagesArray = response.messages || ["Votre donation est mis à jour !"]
        const count = 3;

        getInfoElement().html(buildMessagesToList([messagesArray, `Redirection dans ${count} secondes ..`]))
        const boxContainer = getContainerAlert();
        setSuccessVisibility(boxContainer);

        setTimeout(() => {
            document.location.href = `${context}/donations/mydonations`
        }, count * 1000)
    }).fail((xhr) => {
        const error = xhr.responseText;
        const errorJson = JSON.parse(error);
        const messagesText = errorJson.messages;

        if (hasMessages(messagesText)) {
            getInfoElement().html(buildMessagesToList(messagesText))
            const boxContainer = getContainerAlert();
            setErrorVisibility(boxContainer);
        }
    })
})