chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (message.type === "GENERATE_EMAIL") {

        fetch("http://localhost:8080/api/v1/email/generate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(message.payload)
        })
        .then(res => res.text())
        .then(data => sendResponse({ success: true, data }))
        .catch(err => sendResponse({ success: false, error: err.message }));

        return true; // REQUIRED for async response
    }
});
