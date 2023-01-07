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

let language = getCompatibleLanguage(navigator.language);
document.documentElement.setAttribute('lang', language);

function getCompatibleLanguage(langString) {
    return langString.startsWith("ja") ? "ja-JP" : "en-US";
}

function updateText() {
    document.querySelectorAll("[data-text]").forEach(function (element) {
        element.innerHTML = languages[language][element.dataset.text];
    });
}

function setLanguage(lang) {
    language = getCompatibleLanguage(lang);
    document.documentElement.setAttribute('lang', language);
    updateText();
}

{
    let checkbox = document.getElementById("lang-checkbox");
    checkbox.checked = language === "ja-JP";
}

updateText();