var wsUri = "ws://localhost:9000/broadcast";
var grid;
window.addEventListener("load", init, false);

function init() {
	grid = document.getElementById("grid");
	connectToServer();
}

function connectToServer() {
	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
	websocket = new WebSocket(wsUri);
	websocket.onopen = function(evt) {
		onOpen(evt);
	};
	websocket.onclose = function(evt) {
		onClose(evt);
	};
	websocket.onmessage = function(evt) {
		onMessage(evt);
	};
	websocket.onerror = function(evt) {
		onError(evt);
	};
}

function onOpen(evt) {
	websocket.send("getGameData");
}

function onClose(evt) {
}

function onError(evt) {
	alert("ERROR");
}

function onMessage(evt) {
	try {
		var msg = JSON.parse(evt.data);
		switch (msg.cmd) {
		case "error":
			displayError(msg.message);
			break;

		case "all":
			renewGrid(msg.gameData.levelHeight, msg.gameData.levelWidth);
			updateView(msg);
			displayInformation("New Game Loaded.");
			//msg.log instead of "NewGame Loaded."
			document.getElementById("gameData").style = "";
			break;
			
		case "updated":
			updateView(msg);
			displayInformation("Turn of player was valid.");
			appendToLog(msg.log);
			break;
		}
	} catch (e) {
		displayError("Received message was no JSON object!");
	}
}

function appendToLog(logList) {
	var logOutput = document.getElementById("log");
	for ( var i = 0; i < logList.length; i++) {
		logOutput.value	+= logList[i];
		logOutput.value	+= "\n";
	}
}


function renewGrid(height, width) {
	var contentTable = document.getElementById("grid");
	changeElement("grid", "", "")

	for ( var r = 0; r < height; r++) {
		var tRow = document.createElement("tr");
		for ( var c = 0; c < width; c++) {
			var tDesk = document.createElement("td");
			tDesk.id = r.toString() + "," + c.toString();
			tDesk.className = "empty";
			tRow.appendChild(tDesk);
		}
		grid.appendChild(tRow);
	}
}


function displayError(msg) {
	changeElement("message1", "color: red", msg);
}

function displayInformation(msg) {
	changeElement("message1", "", msg);
}

function updateView(data) {
	if (data.gameData != null) {
    	switch (data.gameData.currentPlayer) {
    	case "H":
    		changeElement("cPlayer", "", "Human");
    		break;
    
    	case "Z":
    		changeElement("cPlayer", "", "Zombie");
    		break;
    	}
    
    	propagateState(data.gameData.gameState, data.gameData.score);
    
    	changeElement("cLifes", "", data.gameData.lifes);
    
    	changeElement("cMoves", "", data.gameData.movesRemaining);
    
    	changeElement("cCoins", "", data.gameData.coins);
    
    	changeElement("cScore", "", data.gameData.score);
    
    	switch (data.gameData.powerUp) {
    	case true:
    		changeElement("cZKiller", "", "YES");
    		break;
    
    	case false:
    		changeElement("cZKiller", "", "NO");
    		break;
    	}
    }
	
	for ( var i in data.cells) {
		updateCell(data.cells[i]);
	}
	

}


function propagateState(state, score) {
	switch (state) {
	case "won":
		alert("Congratualtion!! You won!!\n" + "Loading next Map");
		websocket.send("getGameData");
		break;

	case "gameOver":
		alert("GameOver!!\n" + "Your final score: " + score);
		break;
	}
}


function changeElement(id, style, content) {
	var toChange = document.getElementById(id);
	toChange.style = style;
	toChange.innerHTML = content;
}


function updateCell(cell) {
	var cellId = (cell.x + "," + cell.y).toString();
	var cellNode = document.getElementById(cellId);

	switch (cell.token) {
	case 'H':
		if (cell.isHiglighted == true)
			cellNode.className = "hHuman";
		else
			cellNode.className = "human";
		break;

	case 'Z':
		if (cell.isHiglighted == true)
			cellNode.className = "hZombie";
		else
			cellNode.className = "zombie";

		break;

	case 'C':
		if (cell.isHiglighted == true)
			cellNode.className = "hCoin";
		else
			cellNode.className = "coin";
		break;

	case 'P':
		if (cell.isHiglighted == true)
			cellNode.className = "hPowerup";
		else
			cellNode.className = "powerup";
		break;

	case 'W':
		cellNode.className = "wall";
		break;

	case 'N':
		if (cell.isHiglighted == true)
			cellNode.className = "hEmpty";
		else
			cellNode.className = "empty";
		break;
	}
}

function handleKeyEvent(evt)
{
	var switchToken = 9; // tab
	var nextPlayer = 13; // enter|return
	var leftArrow = 37;
	var upArrow = 38;
	var rightArrow = 39;
	var downArrow = 40;
	var nextGame = 78; // n

	var textBox = document.getElementById("userInput");
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	switch (charCode) {
	case leftArrow:
		doSend("moveLeft");
		textBox.value = "left";
		break;

	case upArrow:
		doSend("moveUp");
		textBox.value = "up";
		break;

	case rightArrow:
		doSend("moveRight");
		textBox.value = "right";
		break;

	case downArrow:
		doSend("moveDown");
		textBox.value = "down";
		break;

	case nextPlayer:
		doSend("nextPlayer");
		textBox.value = "nextPlayer";
		break;

	case switchToken:
		doSend("switchToken");
		textBox.value = "switchToken";
		break;

	case nextGame:
		doSend("nextGame");
		textBox.value = "nextGame";
		break;
	}
}

function doSend(command) 
{
	websocket.send(command);
}
