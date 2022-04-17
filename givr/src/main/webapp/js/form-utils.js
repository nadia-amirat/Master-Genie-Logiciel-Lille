/**
 * Get the alert container.
 * @returns {*|jQuery|HTMLElement}
 */
function getContainerAlert() {
    return $(".alert");
}

/**
 * Get the element to display alert message.
 * @returns {*|jQuery|HTMLElement}
 */
function getInfoElement() {
    return $("#message")
}

/**
 * Get the button to submit form.
 * @returns {*|jQuery|HTMLElement}
 */
function getButtonSubmit() {
    return $("#submit-form");
}

/**
 * Check if the messages are not null and not empty.
 * @param messages {string[] | null} Messages.
 * @returns {boolean} true if there is at least one message, false otherwhise.
 */
function hasMessages(messages) {
    return messages != null && messages.length > 0;
}

/**
 * From the message in the array string, build a HTML list to display them.
 * @param messages {string[]} Array of message.
 * @returns {string} The HTML list built in string format.
 */
function buildMessagesToList(messages) {
    let builder = "<ul>";

    messages.forEach((msg) => {
        builder += `<li>${msg}</li>`;
    });
    builder += "</ul>"

    return builder;
}

/**
 * Display the element and set it has an success element.
 * @param element {jQuery} JQuery element.
 */
function setSuccessVisibility(element) {
    element.removeClass("d-none");
    element.removeClass("alert-danger");
    element.addClass("alert-success");
}

/**
 * Display the element and set it has an error element.
 * @param element {jQuery} JQuery element.
 */
function setErrorVisibility(element) {
    element.removeClass("d-none");
    element.removeClass("alert-success");
    element.addClass("alert-danger");
}