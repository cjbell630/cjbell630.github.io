const Languages = {
    JS: "https://upload.wikimedia.org/wikipedia/commons/9/99/Unofficial_JavaScript_logo_2.svg",
    CPP: "https://upload.wikimedia.org/wikipedia/commons/1/18/ISO_C%2B%2B_Logo.svg",
    C: "https://upload.wikimedia.org/wikipedia/commons/1/18/C_Programming_Language.svg",
    Lua: "https://upload.wikimedia.org/wikipedia/commons/c/cf/Lua-Logo.svg",
    Java: "https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg",
    HTML: "https://upload.wikimedia.org/wikipedia/commons/6/61/HTML5_logo_and_wordmark.svg",
    CSS: "https://upload.wikimedia.org/wikipedia/commons/d/d5/CSS3_logo_and_wordmark.svg",
    PHP: "https://upload.wikimedia.org/wikipedia/commons/2/27/PHP-logo.svg",
    CS: "https://upload.wikimedia.org/wikipedia/commons/b/bd/Logo_C_sharp.svg",
    Ruby: "https://upload.wikimedia.org/wikipedia/commons/f/f1/Ruby_logo.svg",
    Rust: "https://upload.wikimedia.org/wikipedia/commons/d/d5/Rust_programming_language_black_logo.svg",
    Unity: "https://static.wikia.nocookie.net/logopedia/images/c/ce/Unity_%28Icon%29.svg", // TODO fix
    Unreal: "https://upload.wikimedia.org/wikipedia/commons/1/1b/Unreal_Engine_4_logo.svg",
    Blender: "https://upload.wikimedia.org/wikipedia/commons/0/0c/Blender_logo_no_text.svg",
    Python: "https://upload.wikimedia.org/wikipedia/commons/c/c3/Python-logo-notext.svg",
    Kotlin: "https://upload.wikimedia.org/wikipedia/commons/7/74/Kotlin-logo.svg",
}

class Project {


    constructor(nameID, descriptionID, languages, link, image) {
        this.nameID = nameID;
        this.descriptionID = descriptionID;
        this.languages = languages;
        this.link = link;
        this.image = image;
    }
}

export const projects = [
    new Project("JUGA-BOT", "JUGA-BOT-DESC",
        [Languages.Python], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("FNAF-ADVANCE", "FNAF-ADVANCE-DESC",
        [Languages.C], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("TALL-INFINITY-HD", "TALL-INFINITY-HD-DESC",
        [Languages.CS, Languages.Blender, Languages.Unity], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("YOSHI", "YOSHI-DESC",
        [Languages.Lua], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("KAREL", "KAREL-DESC",
        [Languages.Python], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("PISOUND-GAMEPAD", "PISOUND-GAMEPAD-DESC",
        [Languages.CPP], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("ANKI", "ANKI-DESC",
        [Languages.JS], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("JAVA-FX-TETRIS", "JAVA-FX-TETRIS-DESC",
        [Languages.Java], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("THIS-SITE", "THIS-SITE-DESC",
        [Languages.HTML, Languages.CSS, Languages.JS], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    // TODO Kotlin
];