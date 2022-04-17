const buttonSubmit = getButtonSubmit()

buttonSubmit.on('click', () => {
    $.ajax({
        method: 'PATCH',
        url: `${context}/api/account/modify`,
        dataType: "json",
        contentType: 'application/json',
        data: JSON.stringify({
            email: $("#email").val(),
            firstname: $("#firstname").val(),
            lastname: $("#lastname").val(),
            address: $("#address").val()
        })
    }).done((response, _, xhr) => {
        const status = xhr.status;
        if (status === 200) {
            const messagesText = response.messages;
            if (hasMessages(messagesText)) {
                getInfoElement().html(buildMessagesToList(messagesText))
                const boxContainer = getContainerAlert();
                setSuccessVisibility(boxContainer);
            }
        } else if (status === 304) {
            getInfoElement().html(buildMessagesToList(["Aucune donnée n'a été modifiée"]))
            const boxContainer = getContainerAlert();
            setErrorVisibility(boxContainer);
        }
    }).fail((xhr) => {
        const error = xhr.responseText
        const errorJson = JSON.parse(error);
        const messagesText = errorJson.messages
        if (hasMessages(messagesText)) {
            getInfoElement().html(buildMessagesToList(messagesText))
            const boxContainer = getContainerAlert();
            setErrorVisibility(boxContainer);
        }
    });
})

$("#disconnect").on('click', () => {
    $.ajax({
        method: 'POST',
        url: `${context}/api/authenticate/disconnect`,
        dataType: "json",
        contentType: 'application/json'
    }).done(() => {
        document.location.href = `${context}/`;
    }).fail((xhr) => {
        const error = xhr.responseText
        const errorJson = JSON.parse(error);
        const messagesText = errorJson.messages || ["Impossible de vous déconnecter"]
        if (hasMessages(messagesText)) {
            getInfoElement().html(buildMessagesToList(messagesText))
            const boxContainer = getContainerAlert();
            setErrorVisibility(boxContainer);
        }
    });
});

function deletionClick() {
    document.getElementById('id01').style.display = 'block';
}

function closePopup() {
    document.getElementById('id01').style.display = 'none';
}

$("#delete").on('click', () => {
    deletionClick();
});

$("#deleteAccountBtn").on('click', (event) => {
    event.preventDefault();
    let $form = $(this),
        emailAccount = $("#emailConfirmation").val();
    $.ajax({
        method: 'DELETE',
        url: `${context}/api/account`,
        contentType: 'application/json',
        dataType: "json",
        data: JSON.stringify({
            email: emailAccount
        })
    }).done(() => {
        document.location.href = `${context}/`;
    }).fail((xhr) => {
        const error = xhr.responseText
        const errorJson = JSON.parse(error);
        const messagesText = errorJson.messages
        if (hasMessages(messagesText)) {
            getInfoElement().html(buildMessagesToList(messagesText))
            const boxContainer = getContainerAlert();
            setErrorVisibility(boxContainer);
        }
    });

});