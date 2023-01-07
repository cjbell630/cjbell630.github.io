const languages = {
    "en-US": {
        "NAME": "Connor Bell",
        "LINKS": "My Links",
        "YT-CHANNEL": "Japanese Translation"
    },
    "ja-JP": {
        "NAME": "コナー ・ ベル",
        "LINKS": "リンク",
        "YT-CHANNEL": "翻訳チャンネル"
    }
}
let language = "en-US";

function updateText() {
    document.querySelectorAll("[data-text]").forEach( function (element) {
        element.innerHTML = languages[language][element.dataset.text];
    });
}

function setLanguage(lang) {
    language = lang;
    updateText();
}

updateText();