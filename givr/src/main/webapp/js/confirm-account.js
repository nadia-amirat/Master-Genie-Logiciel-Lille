let count = 3;

const textCooldown = $("#cooldown")

/**
 * Set the text about the redirection with the counter value.
 */
function updateTimer() {
    textCooldown.text(`Redirection dans ${count}s`)
}

updateTimer();

setInterval(() => {
    count--;
    if(count <= 0) {
        document.location.href=`${context}/login`;
    }
    updateTimer();
}, 1000)