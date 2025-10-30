const msgerForm = get(".msger-inputarea");
const msgerInput = get(".msger-input");
const msgerChat = get(".msger-chat");

// const socketServer = "http://at-surgeon.gl.at.ply.gg:36199/chat-websocket";
const socketServer = "http://localhost:8080/chat-websocket";

const BOT_IMG =
  "https://img.freepik.com/free-photo/designer-working-3d-model_23-2149371896.jpg?semt=ais_hybrid&w=740&q=80";
const PERSON_IMG = "https://cdn-icons-png.flaticon.com/512/36/36183.png";
const BOT_NAME = "";
const PERSON_NAME = "";

msgerForm.addEventListener("submit", (event) => {
  event.preventDefault();

  const msgText = msgerInput.value;
  if (!msgText) return;

  sendMessage(msgText);
  appendMessage(username, PERSON_IMG, "right", msgText);
  msgerInput.value = "";
});

function appendMessage(name, img, side, text) {
  //   Simple solution for small apps
  const msgHTML = `
    <div class="msg ${side}-msg">
      <div class="msg-img" style="background-image: url(${img})"></div>

      <div class="msg-bubble">
        <div class="msg-info">
          <div class="msg-info-name">${name}</div>
          <div class="msg-info-time">${formatDate(new Date())}</div>
        </div>

        <div class="msg-text">${text}</div>
      </div>
    </div>
  `;

  msgerChat.insertAdjacentHTML("beforeend", msgHTML);
  msgerChat.scrollTop += 500;
}

function get(selector, root = document) {
  return root.querySelector(selector);
}

function formatDate(date) {
  const h = "0" + date.getHours();
  const m = "0" + date.getMinutes();

  return `${h.slice(-2)}:${m.slice(-2)}`;
}

function random(min, max) {
  return Math.floor(Math.random() * (max - min) + min);
}

let stompClient = null;
let username = null;

// Ask for username when page loads
window.onload = function () {
  username = prompt("Enter your username:");
  if (!username || username.trim() === "") {
    alert("Username is required!");
    window.location.reload();
  }
  connect();
};

function connect() {
  var socket = new SockJS(socketServer);
  stompClient = Stomp.over(socket);

  stompClient.connect({ username: "" + username }, function (frame) {
    showLog("Connected as " + username);

    stompClient.subscribe("/allChat/messages", function (messageOutput) {
      var message = JSON.parse(messageOutput.body);
      showLog(message);
      handleMsg(message);
    });
  });
}

function sendMessage(messageText) {
  if (messageText !== "") {
    stompClient.send(
      "/app/sendMessage",
      {},
      JSON.stringify({
        from: username,
        text: messageText,
      })
    );
  }
}

function handleMsg(message) {
  if (message.broadcast) {
    //msg is a broadcast from server
    appendBroadcastMessage(message.text);
  } else if (message.confettie) {
    //msg is a confettie or animation
  } else if (message.from != username) {
    appendMessage(message.from, BOT_IMG, "left", message.text);
  }
}

function appendBroadcastMessage(text) {
  const chatArea = document.querySelector(".msger-chat");

  const html = `
    <div class="msg broadcast-msg">
      ${text}
    </div>
  `;

  chatArea.insertAdjacentHTML("beforeend", html);
  chatArea.scrollTop = chatArea.scrollHeight; // Auto-scroll
}

function showLog(message) {
  console.log(message);
}
