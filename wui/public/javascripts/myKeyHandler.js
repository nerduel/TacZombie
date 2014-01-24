function handleKeyEvent(evt) {
	var textBox = document.getElementById("userInput");
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	switch (charCode) {
	case leftArrow:
		doSend("moveLeft");
		textBox.value = "left"
		break;

	case upArrow:
		doSend("moveUp");
		textBox.value = "up"
		break;

	case rightArrow:
		doSend("moveRight");
		textBox.value = "right"
		break;

	case downArrow:
		doSend("moveDown");
		textBox.value = "down"
		break;

	case nextPlayer:
		doSend("nextPlayer");
		textBox.value = "nextPlayer"
		break;

	case switchToken:
		doSend("switchToken");
		textBox.value = "switchToken"
		break;

	case nextGame:
		doSend("nextGame");
		textBox.value = "nextGame"
		break;
}

function doSend(command) {
	websocket.send(command);
}