const languages = {
    "en-US": {
        "NAME": "Connor Bell",
        "LINKS": "My Links",
        "YT-CHANNEL": "Japanese Translation",
        "PROJECTS": "Projects",
        "JUGA-BOT": "JUGA Bot",
        "JUGA-BOT-DESC": "A Discord bot for the Japanese UGA server.",
        "FNAF-ADVANCE": "FNaF Advance",
        "FNAF-ADVANCE-DESC": "A Game Boy Advance port of the game Five Nights at Freddy's.",
        "TALL-INFINITY-HD": "Tall Infinity HD",
        "TALL-INFINITY-HD-DESC": "A Unity remake of the PlayStation 1 puzzle game \"Tall Infinity\".",
    },
    "ja-JP": {
        "NAME": "コナー ・ ベル",
        "LINKS": "リンク",
        "YT-CHANNEL": "翻訳チャンネル",
        "PROJECTS": "プロジェクト",
        "JUGA-BOT": "JUGAボット",
        "JUGA-BOT-DESC": "ジョージア大学の日本語サークルのDiscordサーバーのためのボット。",
        "FNAF-ADVANCE": "FNaFアドバンス",
        "FNAF-ADVANCE-DESC": "「Five Nights at Freddy's」というゲームのゲームボーイアドバンス移植版。",
        "TALL-INFINITY-HD": "TALL∞HD",
        "TALL-INFINITY-HD-DESC": "「TALL∞（トール・アンリミテッド）」というPlayStation1のパズルゲームのUnityリメイク。",
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