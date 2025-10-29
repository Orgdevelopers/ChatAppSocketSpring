const msgerForm = get(".msger-inputarea");
const msgerInput = get(".msger-input");
const msgerChat = get(".msger-chat");

const socketServer = "http://at-surgeon.gl.at.ply.gg:36199/chat-websocket";

const BOT_MSGS = [
  "Hi, how are you?",
  "Ohh... I can't understand what you trying to say. Sorry!",
  "I like to play games... But I don't know how to play!",
  "Sorry if my answers are not relevant. :))",
  "I feel sleepy! :(",
];

// Icons made by Freepik from www.flaticon.com
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

// function botResponse() {
//   const r = random(0, BOT_MSGS.length - 1);
//   const msgText = BOT_MSGS[r];
//   const delay = msgText.split(" ").length * 100;

//   setTimeout(() => {
//     appendMessage(BOT_NAME, BOT_IMG, "left", msgText);
//   }, delay);
// }

// Utils
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

// ✅ Ask for username when page loads
window.onload = function () {
  username = prompt("Enter your username:");
  if (!username || username.trim() === "") {
    alert("Username is required!");
    window.location.reload();
  }
  connect();
};

// ✅ Function to connect to WebSocket endpoint
function connect() {
  // Replace IP address with your PC IP (not localhost when using phone)
  var socket = new SockJS(socketServer);
  stompClient = Stomp.over(socket);

  stompClient.connect({}, function (frame) {
    showMessage("✅ Connected as " + username);

    // Subscribe to /topic/messages to receive messages broadcast from server
    stompClient.subscribe("/allChat/messages", function (messageOutput) {
      var message = JSON.parse(messageOutput.body);
      console.log(message);

      if (message.from != username) {
        appendMessage(message.from, BOT_IMG, "left", message.text);
      }
      // showMessage(message);
    });
  });
}

// ✅ Function to send message to server
function sendMessage(messageText) {
  //let messageText = document.getElementById("messageInput").value.trim();

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

// ✅ Display message in chat area
function showMessage(message) {
  console.log(message);
}
