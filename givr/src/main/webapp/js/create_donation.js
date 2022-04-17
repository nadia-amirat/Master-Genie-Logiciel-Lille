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
        method: 'POST',
        url: `${context}/api/donations`,
        contentType: false,
        processData: false,
        cache: false,
        enctype: 'multipart/form-data',
        data: formData
    }).done((response) => {
        const messagesArray = response.messages || ["Votre donation est créée !"]
        const count = 3;

        getInfoElement().html(buildMessagesToList([messagesArray, `Redirection dans ${count} secondes ..`]))
        const boxContainer = getContainerAlert();
        setSuccessVisibility(boxContainer);

        setTimeout(() => {
            document.location.href = `${context}/`
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