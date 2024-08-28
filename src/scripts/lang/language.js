const languages = {
    "en-US": {
        "NAME": "Connor Bell",
        "LINKS": "My Links",
        "YT-CHANNEL": "Japanese Translation",
        "GITHUB": "GitHub",
        "PROJECTS": "Projects",
        /* JUGA BOT */
        "JUGA-BOT": "JUGA Bot",
        "JUGA-BOT-DESC": "A Discord bot for the Japanese UGA server.",
        "JUGA-INTRO": "JUGA is a social/study group at UGA for students who are learning Japanese, in order to help " +
            "each  other learn and to make friends. The group's central focus is the Discord server, which as of " +
            "January 2023 has 275 members. Discord is a social messaging service which allows large groups of " +
            "people to communicate in an organized manner. Users are allowed to create bot accounts, which are run " +
            "using HTTP requests, in order to automate tasks for server moderators. I created and maintain a bot " +
            "for the JUGA server using a Python wrapper API. The repository is private since it deals with " +
            "semi-personal information and is designed specifically for the JUGA server, but I'll show some of the " +
            "code here.",
        "FEATURES": "Features",
        "ROLE-SELECTION": "Role Selection",
        "POLL-CREATION": "Poll Creation",

        /* FNAF */
        "FNAF-ADVANCE": "FNaF Advance",
        "FNAF-ADVANCE-DESC": "A Game Boy Advance port of the game Five Nights at Freddy's.",
        "TALL-INFINITY-HD": "Tall Infinity HD",
        "TALL-INFINITY-HD-DESC": "A Unity remake of the PlayStation 1 puzzle game \"Tall Infinity\".",
        "YOSHI": "Yoshi NES Bot",
        "YOSHI-DESC": "A bot that plays the NES game \"Yoshi\".",
        "KAREL": "Karel the Robot - Python",
        "KAREL-DESC": "A Python implementation of Karel the Robot, a Java program designed to help learn programming.",
        "PISOUND-GAMEPAD": "PiSound Gamepad",
        "PISOUND-GAMEPAD-DESC": "A program that allows you to use a Wii U gamepad to control PiSound software.",
        "ANKI": "Anki Cards + Tools",
        "ANKI-DESC": "A collection of cards and tools for learning Japanese with the Anki flashcard program.",
        "JAVA-FX-TETRIS": "JavaFX Tetris",
        "JAVA-FX-TETRIS-DESC": "A JavaFX implementation of the classic game Tetris.",
        "THIS-SITE": "This Site",
        "THIS-SITE-DESC": "The website you're on right now.",
    },
    "ja-JP": {
        "NAME": "コナー ・ ベル",
        "LINKS": "リンク",
        "YT-CHANNEL": "翻訳チャンネル",
        "GITHUB": "GitHub",
        "PROJECTS": "プロジェクト",

        /* JUGA BOT */
        "JUGA-BOT": "JUGAボット",
        "JUGA-BOT-DESC": "ジョージア大学の日本語サークルのDiscordサーバー用に作ったボット。",
        "JUGA-INTRO": "JUGAとは、ジョージア大学の日本語を勉強したり友達を作ったりする研究会です。" +
            "中心は2023年10月現在、275人の入っているDiscordサーバーです。" +
            "Discordとは、大規模なグループがきれいにコミュニケーションができるSNSです。" +
            "HTTPリクエストでモデレーターの代わりにいろいろなことを自動化するボットを作成することができます。" +
            "私はPythonラッパーAPIを使用して、JUGA用のボットを作成し、保守しています。" +
            "個人情報があり、JUGAの専用でありますので、リポジトリはプライベートです。しかし、コードの一部を示します。",
        "FEATURES": "機能",
        "ROLE-SELECTION": "ロール選択",
        "POLL-CREATION": "アンケート取り",


        /* FNAF */
        "FNAF-ADVANCE": "FNaFアドバンス",
        "FNAF-ADVANCE-DESC": "「Five Nights at Freddy's」というゲームのゲームボーイアドバンス移植版。",
        "TALL-INFINITY-HD": "TALL∞HD",
        "TALL-INFINITY-HD-DESC": "「TALL∞（トール・アンリミテッド）」というPlayStation1のパズルゲームのUnityリメイク。",
        "YOSHI": "ヨッシーのたまごボット",
        "YOSHI-DESC": "「ヨッシーのたまご」というファミコムゲームができるボット。",
        "KAREL": "PythonのKarel the Robot",
        "KAREL-DESC": "「Karel the Robot」というコードを学ぶJavaのプログラムのPython実装。",
        "PISOUND-GAMEPAD": "PiSound Gamepad",
        "PISOUND-GAMEPAD-DESC": "「PiSound」というソフトウェアをWii U GamePadで操作できるプログラム。",
        "ANKI": "Ankiカードとツール",
        "ANKI-DESC": "「Anki」というフラッシュカードプログラムで日本語を学ぶためのカードとツールの集まり。",
        "JAVA-FX-TETRIS": "JavaFXテトリス",
        "JAVA-FX-TETRIS-DESC": "JavaFXで作られたテトリスの実装。",
        "THIS-SITE": "このサイト",
        "THIS-SITE-DESC": "今見ているサイト。",
    }
}

let language = getCompatibleLanguage(navigator.language);
document.documentElement.setAttribute('lang', language);

function getCompatibleLanguage(langString) {
    return langString.startsWith("ja") ? "ja-JP" : "en-US";
}

function updateText(documentToUse = document) {
    // update text for elements in this page
    documentToUse.querySelectorAll("[data-text]").forEach(function (element) {
        element.innerHTML = languages[language][element.dataset.text];
    });

    // update text for all elements in all CUSTOM iframes in this page
    documentToUse.querySelectorAll(".same-origin-iframe").forEach(function (element) {
        updateText(element.contentWindow.document);
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