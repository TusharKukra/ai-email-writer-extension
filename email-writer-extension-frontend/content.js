function injectButton() {
    const existingButton = document.querySelector('.ai-reply-button');
    if (existingButton) {
        existingButton.remove();
    }

    const toolbar = findComposeToolbar();
    if (!toolbar) {
        console.log("Toolbar not found!");
        return;
    }

    console.log("Toolbar found!");

    const button = createAIButton();
    button.classList.add('ai-reply-button');

    button.addEventListener('click', async () => {
        try {
            button.innerHTML = "Generating AI response...";
            button.disabled = true;

            const emailContent = getEmailContent();
            if (!emailContent) {
                alert("Could not detect email content.");
                return;
            }

            // ✅ CALL BACKGROUND SERVICE WORKER (NO CORS)
            const generatedReply = await new Promise((resolve, reject) => {
                chrome.runtime.sendMessage(
                    {
                        type: "GENERATE_EMAIL",
                        payload: {
                            emailContent: emailContent,
                            emailTone: "Professional"
                        }
                    },
                    (response) => {
                        if (chrome.runtime.lastError) {
                            reject(chrome.runtime.lastError.message);
                        } else if (!response || !response.success) {
                            reject("API request failed");
                        } else {
                            resolve(response.data);
                        }
                    }
                );
            });

            const composeBox = document.querySelector(
                '[role="textbox"][g_editable="true"]'
            );

            if (composeBox) {
                composeBox.focus();
                document.execCommand('insertText', false, generatedReply);
            }

        } catch (error) {
            console.error("AI Reply error:", error);
            alert("Failed to generate AI reply");
        } finally {
            button.innerHTML = "AI Reply";
            button.disabled = false;
        }
    });

    toolbar.insertBefore(button, toolbar.firstChild);
}

function getEmailContent() {
    const selectors = [
        '.h7',
        '.a3s.aiL',
        '.gmail_quote',
        '[role="presentation"]'
    ];

    for (const selector of selectors) {
        const content = document.querySelector(selector);
        if (content) {
            return content.innerText.trim();
        }
    }
    return '';
}

function createAIButton() {
    const button = document.createElement('div');
    button.className = 'T-I J-J5-Ji aoO v7 T-I-atl L3';
    button.style.marginRight = '8px';
    button.innerHTML = 'AI Reply';
    button.setAttribute('role', 'button');
    button.setAttribute('data-tooltip', 'Generate AI Reply');
    return button;
}

function findComposeToolbar() {
    const selectors = [
        '.btC',
        '.aDh',
        '[role="dialog"]',
        '.gU.Up'
    ];

    for (const selector of selectors) {
        const toolbar = document.querySelector(selector);
        if (toolbar) {
            return toolbar;
        }
    }
    return null;
}

const observer = new MutationObserver((mutations) => {
    for (const mutation of mutations) {
        const addedNodes = Array.from(mutation.addedNodes);

        const hasComposeElements = addedNodes.some(node =>
            node.nodeType === Node.ELEMENT_NODE &&
            (
                node.matches('.aDh, .btC, [role="dialog"]') ||
                node.querySelector?.('.aDh, .btC, [role="dialog"]')
            )
        );

        if (hasComposeElements) {
            console.log("Compose email window detected!");
            setTimeout(injectButton, 500);
        }
    }
});

observer.observe(document.body, {
    childList: true,
    subtree: true
});
