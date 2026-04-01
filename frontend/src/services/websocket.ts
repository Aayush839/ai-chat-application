import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient: Client | null = null;

export function connectWebSocket(conversationId: number, onMessage: (msg: any) => void) {

  const socket = new SockJS("http://localhost:8080/chat");

  stompClient = new Client({
    webSocketFactory: () => socket,
    reconnectDelay: 5000,
    debug: (str) => console.log(str),

    onConnect: () => {
      console.log("STOMP Connected");

      stompClient!.subscribe(`/topic/conversations/${conversationId}`, (message) => {
        const body = JSON.parse(message.body);
        console.log("Received:", body);
        onMessage(body);
      });
    }
  });

  stompClient.activate();
}

export function sendMessage(conversationId: number, message: any) {

  if (!stompClient || !stompClient.connected) {
    console.error("STOMP not connected yet");
    return;
  }

  stompClient.publish({
    destination: `/app/chat/${conversationId}`,
    body: JSON.stringify(message)
  });

  console.log("Message sent:", message);
}