let tick = 0;
let canvas = document.getElementById("bg-canvas");
let ctx = canvas.getContext("2d");
let leftBox = document.getElementById("left-box");
let rightBox = document.getElementById("right-box");

//get DPI

fix_dpi();
let canvasCenterX;
let canvasCenterY;

let outerRad;
let innerRad;

let chunkDist;
let chunkRad;
let chunkSpeed;

let smallCircRad;

let tinyCircRad;
let dotRad;
let strokeWidth;

function recalculate() {
    canvasCenterX = canvas.width / 2;
    canvasCenterY = canvas.height / 2;

    // outer diam should be at max 50% of the width
    // 60w =
    // inner rad = 7/9 outer rad
    if (canvas.width > canvas.height) {
        leftBox.style.width = "20vw";
        rightBox.style.width = "20vw";
        leftBox.style.height = "80vh";
        rightBox.style.height = "80vh";
        leftBox.style.left = "2.5vw";
        rightBox.style.left = "77.5vw";
        leftBox.style.top = "10vh";
        rightBox.style.top = "10vh";
        outerRad = Math.min(canvasCenterY * 0.9, canvas.width * 0.25);
    } else {
        leftBox.style.width = "80vw";
        rightBox.style.width = "80vw";
        leftBox.style.height = "20vh";
        rightBox.style.height = "20vh";
        leftBox.style.left = "10vw";
        rightBox.style.left = "10vw";
        leftBox.style.top = "2.5vh";
        rightBox.style.top = "77.5vh";

        outerRad = Math.min(canvasCenterX * 0.9, canvas.height * 0.25);
    }
    // innerRad = canvasCenterY * 0.7;
    innerRad = outerRad * 7 / 9;
    chunkDist = Math.sin(19 * Math.PI / 36) * innerRad / Math.sin(13 * Math.PI / 36);
    chunkRad = innerRad * Math.sin(Math.PI / 9) / Math.sin(13 * Math.PI / 36);
    chunkSpeed = 1 / 200;
    smallCircRad = innerRad + chunkRad - chunkDist;
    tinyCircRad = smallCircRad / 4;
    dotRad = tinyCircRad * 5 / 22
    strokeWidth = tinyCircRad * 5 / 22;
}

/**
 * https://codepen.io/jacquelinclem/pen/mdJONg
 */
function fix_dpi() {
    let dpi = window.devicePixelRatio || 1;
    ctx.scale(dpi, dpi);
    //get CSS height
    //the + prefix casts it to an integer?
    //the slice method gets rid of "px"
    let style_height = +getComputedStyle(canvas).getPropertyValue("height").slice(0, -2);
    let style_width = +getComputedStyle(canvas).getPropertyValue("width").slice(0, -2);

    //scale the canvas
    canvas.setAttribute('height', style_height / dpi);
    canvas.setAttribute('width', style_width / dpi);
}

class OrbitingCircle {
    constructor(initialRot, initialOrbit, speed) {
        this.initialRot = initialRot;
        this.initialOrbit = initialOrbit;
        this.speed = speed;
        this.rotation = initialRot;
        this.orbit = initialOrbit;
        this.centerX = 0;
        this.centerY = 0;
    }

    update(tick) {
        this.rotation = (this.initialRot + tick * this.speed) * Math.PI;
        this.orbit = (this.rotation * smallCircRad / innerRad) + this.initialOrbit * Math.PI;
        this.centerX = canvasCenterX + ((innerRad - smallCircRad) * Math.cos(this.orbit));
        this.centerY = canvasCenterY - ((innerRad - smallCircRad) * Math.sin(this.orbit));
    }

    arc() {
        ctx.arc(this.centerX, this.centerY, smallCircRad, this.rotation, this.rotation + Math.PI * 2);
    }
}

let circ1 = new OrbitingCircle(0, 0.5, 1 / 160);
let circ2 = new OrbitingCircle(0, 0, -1 / 120);
let circ3 = new OrbitingCircle(0, 1.5, 1 / 180);

function gcd(a, b) {
    if (b == 0) {
        return a;
    }
    return gcd(b, a % b);
}

function lcm(a, b) {
    return a * b / gcd(a, b);
}

// NOTE should always be a whole number
// last *7 part derived from 2*bigRad/smallRad expanded to remove decimal numbers
//let loopTicks = lcm(lcm(Math.abs(1/circ1.speed), Math.abs(1/circ2.speed)), Math.abs(1/circ3.speed)) * canvas.height*7;

let loopTicks = lcm(lcm(Math.abs(1 / circ1.speed), Math.abs(1 / circ2.speed)), Math.abs(1 / circ3.speed)) * canvas.height * 7;
loopTicks = Math.round(loopTicks);
console.log(loopTicks);

function drawGallifreyan() {
    // based on https://stackoverflow.com/questions/36558928/timing-in-html5-canvas
    //console.log("frame");

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.strokeStyle = "#ffffff"
    ctx.fillStyle = "#ffffff";
    ctx.lineWidth = strokeWidth;
    ctx.beginPath();

    // draw outer ring
    ctx.arc(canvasCenterX, canvasCenterY, outerRad, 0, Math.PI * 2);
    ctx.stroke();

    ctx.beginPath();

    // draw inner ring (with missing 40 deg / 2pi/9 rad chunk)
    ctx.arc(canvasCenterX, canvasCenterY, innerRad, Math.PI * (tick * chunkSpeed + 10 / 9), Math.PI * (tick * chunkSpeed + 8 / 9));

    ctx.stroke();
    ctx.beginPath();

    // draw inner ring chunk (46pi/36 chunk
    let chunkAngle = tick * Math.PI * chunkSpeed;
    let chunkCenterX = canvasCenterX - chunkDist * Math.cos(chunkAngle);
    let chunkCenterY = canvasCenterY - chunkDist * Math.sin(chunkAngle);
    ctx.arc(chunkCenterX, chunkCenterY, chunkRad, (tick * chunkSpeed + 59 / 36) * Math.PI, (tick * chunkSpeed + 13 / 36) * Math.PI);

    ctx.stroke();
    ctx.beginPath();

    // draw circle 1 (top)
    circ1.update(tick);
    circ1.arc();

    // draw circle 2 (right)
    circ2.update(tick);
    circ2.arc();
    ctx.stroke();
    ctx.beginPath();

    // draw circle 3 (bottom)
    circ3.update(tick);
    circ3.arc();

    ctx.stroke();
    ctx.beginPath();
    // draw tiny ornament on circle 1
    ctx.arc(circ1.centerX + smallCircRad * Math.cos(circ1.rotation), circ1.centerY + smallCircRad * Math.sin(circ1.rotation), tinyCircRad, 0, 2 * Math.PI);


    ctx.stroke();
    ctx.beginPath();

    // draw tiny ornament on circle 3
    ctx.arc(circ3.centerX + smallCircRad * Math.cos(circ3.rotation + (Math.PI / 6)), circ3.centerY + smallCircRad * Math.sin(circ3.rotation + (Math.PI / 6)), tinyCircRad, 0, 2 * Math.PI);
    ctx.stroke();


    // draw dots in chunk interior
    for (let i = 0; i < 3; i++) {
        ctx.beginPath();
        let angle = chunkAngle - (1 - i) * Math.PI / 6;
        ctx.arc(chunkCenterX + chunkRad * 0.875 * Math.cos(angle), chunkCenterY + chunkRad * 0.875 * Math.sin(angle), dotRad, 0, 2 * Math.PI);
        ctx.fill();
        ctx.stroke();
    }
    // draw dots in circle3 interior
    for (let i = 0; i < 2; i++) {
        ctx.beginPath();
        let angle = circ3.rotation - (1 - i) * Math.PI / 6;
        ctx.arc(circ3.centerX + smallCircRad * 0.875 * Math.cos(angle), circ3.centerY + smallCircRad * 0.875 * Math.sin(angle), dotRad, 0, 2 * Math.PI);
        ctx.fill();
        ctx.stroke();
    }
    tick = (tick + 1) % loopTicks; // TODO need to check loop
    window.requestAnimationFrame(drawGallifreyan);
}

window.addEventListener('resize', resizeCanvas, false);

function resizeCanvas() {
    canvas.width = window.visualViewport.width;
    canvas.height = window.innerHeight;
    recalculate();
}

resizeCanvas();
window.requestAnimationFrame(drawGallifreyan);
resizeCanvas();