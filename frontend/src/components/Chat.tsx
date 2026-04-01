import { useEffect, useState,useRef } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

export default function Chat() {

  const [messages, setMessages] = useState<any[]>([]);
  const [text, setText] = useState("");
  const stompClient = useRef<Client | null>(null);

  useEffect(() => {

    const socket = new SockJS("http://localhost:8080/chat");

    const client = new Client({
      webSocketFactory: () => socket
    });

    client.onConnect = () => {

      console.log("Connected to WebSocket");

      const conversation = localStorage.getItem("conversationId");
      client.subscribe(`/topic/conversation/${conversation}`,
         (msg) => {
          const body = JSON.parse(msg.body);
          setMessages(prev => [...prev, body]);
      });

    };
    client.activate();
    stompClient.current = client;
    // stompClient.activate();

  }, []);

  const sendMessage = () => {

    const message = {
      // sender: "user1",
      content: text,
      type: "USER"
    };

    stompClient.current?.publish({
      destination: "/app/chat",
      body: JSON.stringify(message)
    });

    setText("");

  };

  return (
    <div>

      <h2>Chat</h2>

      <div>
        {messages.map((m, i) => (
          <p key={i}>{m.content}</p>
        ))}
      </div>

      <input
        value={text}
        onChange={(e) => setText(e.target.value)}
      />

      <button onClick={sendMessage}>Send</button>

    </div>
  );
}