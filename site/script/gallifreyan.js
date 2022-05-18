let tick = 0;
let canvas = document.getElementById("bg-canvas");
let ctx = canvas.getContext("2d");

//get DPI
let dpi = window.devicePixelRatio || 1;
ctx.scale(dpi, dpi);
console.log(dpi);

fix_dpi();
let centerX = canvas.width / 2;
let centerY = canvas.height / 2;

let outerRad = centerY * 0.9;
let innerRad = centerY * 0.7;

let chunkDist = Math.sin(19 * Math.PI / 36) * innerRad / Math.sin(13 * Math.PI / 36);
let chunkRad = centerY * 0.7 * Math.sin(Math.PI / 9) / Math.sin(13 * Math.PI / 36);

let smallCircRad = innerRad + chunkRad - chunkDist;

let tinyCircRad = smallCircRad / 4;

/**
 * https://codepen.io/jacquelinclem/pen/mdJONg
 */
function fix_dpi() {
    //get CSS height
    //the + prefix casts it to an integer
    //the slice method gets rid of "px"
    let style_height = +getComputedStyle(canvas).getPropertyValue("height").slice(0, -2);
    let style_width = +getComputedStyle(canvas).getPropertyValue("width").slice(0, -2);

    //scale the canvas
    canvas.setAttribute('height', style_height * dpi);
    canvas.setAttribute('width', style_width * dpi);
}


function drawGallifreyan() {
    // based on https://stackoverflow.com/questions/36558928/timing-in-html5-canvas
    //console.log("frame");
    fix_dpi();
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.strokeStyle = "#ffffff"
    ctx.fillStyle = "#ffffff";
    ctx.lineWidth = 5;
    ctx.beginPath();

    // draw outer ring
    ctx.arc(centerX, centerY, outerRad, 0, Math.PI * 2);
    ctx.stroke();

    ctx.beginPath();

    // draw inner ring (with missing 40 deg / 2pi/9 rad chunk)
    ctx.arc(centerX, centerY, innerRad, (tick + 200) * Math.PI / 180, (tick + 160) * Math.PI / 180);

    ctx.stroke();
    ctx.beginPath();

    // draw inner ring chunk (46pi/36 chunk
    let chunkAngle = tick * Math.PI / 180;
    let chunkCenterX = centerX - chunkDist * Math.cos(chunkAngle);
    let chunkCenterY = centerY - chunkDist * Math.sin(chunkAngle);
    ctx.arc(chunkCenterX, chunkCenterY, chunkRad, (tick + 295) * Math.PI / 180, (tick + 65) * Math.PI / 180);

    ctx.stroke();
    ctx.beginPath();

    // draw circle 1 (top)
    let circ1Angle = (90 - tick) * Math.PI / 180
    let circ1CenterX = centerX - ((innerRad - smallCircRad) * Math.cos(circ1Angle));
    let circ1CenterY = centerY - ((innerRad - smallCircRad) * Math.sin(circ1Angle));
    ctx.arc(circ1CenterX, circ1CenterY, smallCircRad, -Math.PI * circ1Angle, -Math.PI * circ1Angle + Math.PI * 2);

    // draw circle 2 (right)
    let circ2Angle = (tick - 270) * Math.PI / 270;
    ctx.arc(centerX - ((innerRad - smallCircRad) * Math.cos(circ2Angle)), centerY - ((innerRad - smallCircRad) * Math.sin(circ2Angle)), smallCircRad, -circ2Angle * Math.PI, -circ2Angle * Math.PI + 2 * Math.PI);
    ctx.stroke();
    ctx.beginPath();

    // draw circle 3 (bottom)
    let circ3Angle = (180 - tick) * Math.PI / 120;
    let circ3CenterX = centerX - ((innerRad - smallCircRad) * Math.cos(circ3Angle));
    let circ3CenterY = centerY - ((innerRad - smallCircRad) * Math.sin(circ3Angle));
    ctx.arc(circ3CenterX, circ3CenterY, smallCircRad, 0, 2 * Math.PI);

    ctx.stroke();
    ctx.beginPath();
    // draw tiny ornament on circle 1
    ctx.arc(circ1CenterX + smallCircRad * Math.cos(-Math.PI * circ1Angle), circ1CenterY + smallCircRad * Math.sin(-Math.PI * circ1Angle), tinyCircRad, 0, 2 * Math.PI);


    ctx.stroke();
    ctx.beginPath();

    // draw tiny ornament on circle 3
    ctx.arc(circ3CenterX + smallCircRad * Math.cos((Math.PI / 6) - Math.PI * circ3Angle), circ3CenterY + smallCircRad * Math.sin((Math.PI / 6) - Math.PI * circ3Angle), tinyCircRad, 0, 2 * Math.PI);
    ctx.stroke();


    // draw dots in chunk interior
    for (let i = 0; i < 3; i++) {
        ctx.beginPath();
        let angle = chunkAngle - (1 - i) * Math.PI / 6
        ctx.arc(chunkCenterX + chunkRad * 0.875 * Math.cos(angle), chunkCenterY + chunkRad * 0.875 * Math.sin(angle), 6, 0, 2 * Math.PI);
        ctx.fill();
        ctx.stroke();
    }
    // draw dots in circle3 interior
    for (let i = 0; i < 2; i++) {
        ctx.beginPath();
        let angle = -Math.PI * circ3Angle - (1 - i) * Math.PI / 6
        ctx.arc(circ3CenterX + smallCircRad * 0.875 * Math.cos(angle), circ3CenterY + smallCircRad * 0.875 * Math.sin(angle), 6, 0, 2 * Math.PI);
        ctx.fill();
        ctx.stroke();
    }
    tick = (tick + 0.25) % 2160;
    window.requestAnimationFrame(drawGallifreyan);
}


window.requestAnimationFrame(drawGallifreyan);