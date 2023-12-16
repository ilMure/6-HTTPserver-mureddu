//Matteo Mureddu

let tagEsito = document.getElementById("esito")
let tagPuntiComp = document.getElementById("puntiComp")
let tagPuntiUtente = document.getElementById("puntiUtente")
let tagImgComputer = document.getElementById("imgComputer")
let tagImgUtente = document.getElementById("imgUtente")

let path = "images/"
let immagini = ["sasso.jpg", "carta.jpg", "forbice.jpg"]

let punteggioComp = 0
let punteggioUtente = 0

let sceltaComp
let sceltaUtente

function gioca(){
    sceltaComp = Math.floor(Math.random() * 3)

    if(sceltaUtente == 0 && sceltaComp == 2 || sceltaUtente == 1 && sceltaComp == 0 || sceltaUtente == 2 && sceltaComp == 1){
        punteggioUtente++;
        tagEsito.innerHTML = "Utente"
    }else if(sceltaUtente == sceltaComp){
        tagEsito.innerHTML = "Pareggio"
    }else{
        punteggioComp++;
        tagEsito.innerHTML = "Computer"
    }

    render()
}

function sasso(){
    sceltaUtente = 0
    gioca()
}

function carta(){
    sceltaUtente = 1
    gioca()
}

function forbice(){
    sceltaUtente = 2
    gioca()
}

function render(){
    tagImgUtente.src = path + immagini[sceltaUtente]
    tagImgComputer.src = path + immagini[sceltaComp]

    tagPuntiComp.innerHTML = punteggioComp
    tagPuntiUtente.innerHTML = punteggioUtente
}
