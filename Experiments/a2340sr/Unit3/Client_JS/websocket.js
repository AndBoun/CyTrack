
let socket;

function startWebSocketConnection() {
    const serverUrl = "ws://localhost:8080/echo"; // replace with your actual WebSocket URL
    socket = new WebSocket(serverUrl);

    // WebSocket event listeners
    socket.onopen = function(event) {
        console.log("Connected to WebSocket server.");
        document.getElementById("status").textContent = "Connected";
    };

    socket.onmessage = function(event) {
        console.log("Message from server:", event.data);
        const messageList = document.getElementById("messages");
        const newMessage = document.createElement("li");
        newMessage.textContent = event.data;
        messageList.appendChild(newMessage);
    };

    socket.onclose = function(event) {
        console.log("Disconnected from WebSocket server.");
        document.getElementById("status").textContent = "Disconnected";
    };

    socket.onerror = function(error) {
        console.error("WebSocket error:", error);
    };
}

function sendMessage() {
    const message = document.getElementById("messageInput").value;
    if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(message);
        console.log("Message sent:", message);
    } else {
        console.log("WebSocket is not open.");
    }
}
