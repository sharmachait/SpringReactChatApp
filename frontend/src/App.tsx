import "./App.css";
import SockJS from "sockjs-client"; // Import SockJS client
import { Client as StompClient, Stomp } from "@stomp/stompjs"; // Import StompJS
import React, { useState } from "react";
// import axios from "axios";
function App() {
  // const socket = new SockJS("/ws"); // Establish connection with the server
  // const stompClient = Stomp.over(socket); // Use STOMP over WebSocket
  let [username, setUsername] = useState<string>("");
  const [stompClient, setStompClient] = useState<StompClient | null>();

  async function connect(event: React.MouseEvent<HTMLButtonElement>) {
    event.preventDefault();
    const sock = new SockJS("http://localhost:5455/ws");
    const stomp = Stomp.over(sock);

    // const res = await axios.get("http://localhost:5455/messages/1/2");
    console.log(username);
  }
  return (
    <div>
      <h2>Chat App</h2>
      <div className="flex flex-col items-center gap-2">
        <div className="w-screen flex flex-col items-center">
          <h2>Enter Chatroom</h2>
          <form id="usernameForm" className="flex mt-2 gap-2">
            <label htmlFor="username" className="mt-2">
              username:
            </label>
            <input
              type="text"
              id="username"
              name="username"
              required
              onChange={(e) => setUsername(e.target.value)}
            />
            <button
              onClick={(e) => {
                connect(e);
              }}
            >
              join
            </button>
          </form>
        </div>
        <div className="flex gap-2 w-10/12 ">
          <div className="h-screen w-96 border">
            <div className="flex flex-col">
              <h2>Online Users</h2>
              <div className="min-h-96 flex-grow">
                <ul>
                  <li>User 1</li>
                </ul>
              </div>
              <div>
                <h3>{username}</h3>
                <button>Logout</button>
              </div>
            </div>
          </div>
          <div className="h-screen flex-grow border">
            <div className="flex flex-col">
              <h3>Chat Window</h3>
              <div className="min-h-96 max-h-96"></div>
              <form id="messageForm" name="messageForm" className="flex mt-5">
                <input type="text" className=" ml-5 w-9/12" />
                <button className="w-2/12 ml-5 mr-5">send</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
