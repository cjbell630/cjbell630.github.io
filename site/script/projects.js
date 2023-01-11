Languages = {
    JavaScript: "https://upload.wikimedia.org/wikipedia/commons/9/99/Unofficial_JavaScript_logo_2.svg",
    CPP: "https://upload.wikimedia.org/wikipedia/commons/1/18/ISO_C%2B%2B_Logo.svg",
    C: "https://upload.wikimedia.org/wikipedia/commons/1/18/C_Programming_Language.svg",
    Lua: "https://upload.wikimedia.org/wikipedia/commons/c/cf/Lua-Logo.svg",
    Java: "https://upload.wikimedia.org/wikipedia/commons/3/30/Java_programming_language_logo.svg",
    HTML: "https://upload.wikimedia.org/wikipedia/commons/6/61/HTML5_logo_and_wordmark.svg",
    CSS: "https://upload.wikimedia.org/wikipedia/commons/d/d5/CSS3_logo_and_wordmark.svg",
    PHP: "https://upload.wikimedia.org/wikipedia/commons/2/27/PHP-logo.svg",
    CS: "https://upload.wikimedia.org/wikipedia/commons/b/bd/Logo_C_sharp.svg",
    Ruby: "https://upload.wikimedia.org/wikipedia/commons/f/f1/Ruby_logo.svg",
    Rust: "https://upload.wikimedia.org/wikipedia/commons/d/d5/Rust_programming_language_black_logo.svg",
    Unity: "https://upload.wikimedia.org/wikipedia/commons/c/c4/Unity_2021.svg\" class=\"invert",
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

    getLanguagesHTML(){
        let html = "";
        this.languages.forEach(function(language){
            html += `<img style=" height:2em; padding-right: 0.5em;margin-left: auto;" src="${language}">`;
        });
        return html;
    }


    generateHTML() {
        let html = `<div class="project">
<div class="row">
<h2 style="display: inline;margin-top:0;padding-right: 0.75em;padding-left: 0.75em;margin-bottom:0.5em;" data-text="${this.nameID}"></h2><div style="margin-left: auto;">${this.getLanguagesHTML()}</div>
</div>

<div class="row">
<img style="padding-left: 1em; height:4em;" src="${this.image}" alt="${this.nameID} image">
<div>
<p style="padding-right: 1em; padding-left: 1em;margin-top:0;margin-bottom:0;" data-text="${this.descriptionID}"></p></div>
</div></div><hr>`;
        return html;
    }
}

const projects = [
    new Project("JUGA-BOT", "JUGA-BOT-DESC",
        [Languages.Python], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("FNAF-ADVANCE", "FNAF-ADVANCE-DESC",
        [Languages.C], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("TALL-INFINITY-HD", "TALL-INFINITY-HD-DESC",
        [Languages.CS, Languages.Blender, Languages.Unity ], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
    new Project("YOSHI", "YOSHI-DESC",
        [Languages.Lua], "google.com", "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg"
    ),
];