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
	alert("Connection to " + myUrl + " was closed.");
}

function onError(evt) {
	alert("Something went wrong with the connection to " + myUrl + ". " + evt.data);
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
			changeElement("log", "", "");
			displayInformation(msg.gameMessage);
			document.getElementById("gameData").style = "";
			break;

		case "updated":
			updateView(msg);
			displayInformation(msg.gameMessage);
			appendToLog(msg.log);
			break;
		}
	} catch (e) {
		displayError(e);
	}
}

function appendToLog(logList) {
	var logOutput = document.getElementById("log");
	
	var text = logOutput.value;
	
	for ( var i = 0; i < logList.length; i++) {
		text += logList[i];
		text += "\n";
	}
	
	var total = text.split("\n");

	if (total.length > 25) {
		total = total.slice(total.length - 25);
	}
	
	logOutput.value = total.join("\n");
	
	logOutput.scrollTop = logOutput.scrollHeight
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
		var gameData = document.getElementById("gState");
		clearGameData(gameData)

		switch (data.gameData.currentPlayer) {
		case "H":
			changeElement("cPlayer", "", "Human");

			var tRow = document.createElement("tr");
			var tDesk1 = document.createElement("td");
			var tDesk2 = document.createElement("td");
			tDesk2.className = "scnd";
			tDesk1.innerHTML = "Lifes:";
			tDesk2.innerHTML = data.gameData.lifes;
			tRow.appendChild(tDesk1);
			tRow.appendChild(tDesk2);
			gameData.appendChild(tRow);

			tRow = document.createElement("tr");
			tDesk1 = document.createElement("td");
			tDesk2 = document.createElement("td");
			tDesk2.className = "scnd";
			tDesk1.innerHTML = "Coins collected:";
			tDesk2.innerHTML = data.gameData.coins;
			tRow.appendChild(tDesk1);
			tRow.appendChild(tDesk2);
			gameData.appendChild(tRow);

			tRow = document.createElement("tr");
			tDesk1 = document.createElement("td");
			tDesk2 = document.createElement("td");
			tDesk2.className = "scnd";
			tDesk1.innerHTML = "Score:";
			tDesk2.innerHTML = data.gameData.score;
			tRow.appendChild(tDesk1);
			tRow.appendChild(tDesk2);
			gameData.appendChild(tRow);

			tRow = document.createElement("tr");
			tDesk1 = document.createElement("td");
			tDesk2 = document.createElement("td");
			tDesk2.className = "scnd";
			tDesk1.innerHTML = "Powerup time:";
			tDesk2.innerHTML = data.gameData.powerUp;
			tRow.appendChild(tDesk1);
			tRow.appendChild(tDesk2);
			gameData.appendChild(tRow);

			tRow = document.createElement("tr");
			tDesk1 = document.createElement("td");
			tDesk2 = document.createElement("td");
			tDesk2.className = "scnd";
			tDesk1.innerHTML = "Frozen time";
			tDesk2.innerHTML = data.gameData.frozenTime;
			tRow.appendChild(tDesk1);
			tRow.appendChild(tDesk2);
			gameData.appendChild(tRow);
			break;

		case "Z":
			changeElement("cPlayer", "", "Zombie");
			var tRow = document.createElement("tr");
			var tDesk1 = document.createElement("td");
			var tDesk2 = document.createElement("td");
			tDesk2.className = "scnd";
			tDesk1.innerHTML = "Frozen time";
			tDesk2.innerHTML = data.gameData.frozenTime;
			tRow.appendChild(tDesk1);
			tRow.appendChild(tDesk2);
			gameData.appendChild(tRow);
			break;
		}

		changeElement("cMoves", "", data.gameData.movesRemaining);
		changeElement("cDTokens", "", data.gameData.deadTokens);
		changeElement("cTTokens", "", data.gameData.totalTokens);
		propagateState(data.gameData.gameState, data.gameData.score);
	}

	for ( var i in data.cells) {
		updateCell(data.cells[i]);
	}

	for ( var i in data.humanTokens) {
		changeHuman(data.humanTokens[i])
	}

}

function clearGameData(obj) {
	var tableRows = obj.getElementsByTagName("tr");
	var rowCount = tableRows.length;

	for ( var x = rowCount - 1; x > 3; x--) {
		obj.removeChild(tableRows[x]);
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

function changeHuman(hToken) {
	var cellId = (hToken.x + "," + hToken.y).toString();
	var cellNode = document.getElementById(cellId);

	switch (hToken.powerUp) {
	case true:
		if (cellNode.className == "human")
			cellNode.className = "humanPowerup"
		else
			cellNode.className = "hHumanPowerup"
		break;

	case false:
		if (cellNode.className == "hHuman")
			cellNode.className = "hHuman"
		break;
	}

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

function handleKeyEvent(evt) {
	var left = 37; // left key
	var up = 38; // up key
	var right = 39; // right key
	var down = 40; // down key
	var respanToken = 70; // f
	var switchToken = 71; // g
	var newGame = 77; // m
	var nextPlayer = 78; // n
	var restartGame = 82; // r

	var textBox = document.getElementById("userInput");
	var evt = window.event || evt;
	var charCode = (evt.which) ? evt.which : evt.keyCode;

	switch (charCode) {
	case left:
		doSend("moveLeft");
		textBox.value = "left";
		break;

	case up:
		doSend("moveUp");
		textBox.value = "up";
		break;

	case right:
		doSend("moveRight");
		textBox.value = "right";
		break;

	case down:
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

	case newGame:
		doSend("newGame");
		textBox.value = "newGame";
		break;

	case restartGame:
		doSend("restartGame");
		textBox.value = "restartGame";
		break;

	case respanToken:
		doSend("respawnToken");
		textBox.value = "respawnToken";
		break;
	}
}

function doSend(command) {
	websocket.send(command);
}

var myUrl = document.URL.replace("http://", "");
var wsUri = "ws://" + myUrl + "broadcast";
var grid;
window.addEventListener("load", init, false);

if (window.addEventListener) {
	window.addEventListener("keydown", function(evt) {
		if ([ 37, 38, 39, 40 ].indexOf(evt.keyCode) > -1) {
			evt.preventDefault();
		}
		handleKeyEvent(evt);
	});
}

if (document.body && document.body.attachEvent) {
	document.body.attachEvent("onkeydown", function(evt) {
		if ([ 37, 38, 39, 40 ].indexOf(evt.keyCode) > -1) {
			evt.preventDefault();
		}
		handleKeyEvent(evt);
	});// IE
}
