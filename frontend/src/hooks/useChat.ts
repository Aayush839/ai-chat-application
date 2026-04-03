// src/hooks/useChat.ts

import { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export const useChat = (conversationId: string, token: string) => {
  const [messages, setMessages] = useState<any[]>([]);
  const [isTyping, setIsTyping] = useState(false);

  const stompClient = useRef<Client | null>(null);

  const [page,setPage]=useState(0);

  const BASE_URL = import.meta.env.VITE_API_URL;

  useEffect(() => {
    if (!token || !conversationId) return;
    console.log("conversationId:", conversationId);
    //const socket = new SockJS("http://localhost:8080/chat");
	const socket = new SockJS(
    `${BASE_URL}/chat?token=${token}`
  	// `http://localhost:8080/chat?token=${token}`
	);
    stompClient.current = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      debug: (str) => console.log("STOMP:", str),

      onConnect: () => {
        console.log("✅ Connected to STOMP!");

        stompClient.current?.subscribe(
          `/topic/conversations/${conversationId}`,
          (msg) => {
            const message = JSON.parse(msg.body);

            console.log("WS MESSAGE:", message);

            // ✅ PROCESSING → show typing immediately
            if (message.status === "PROCESSING") {
              setIsTyping(true);

              // 👇 FORCE MINIMUM TYPING TIME (IMPORTANT)
              setTimeout(() => {
                setIsTyping(false);
              }, 1200); // 👈 1.2 sec visible

              return;
            }

            // ✅ Add message (USER + AI)
            setMessages((prev) => [...prev, message]);
          }
        );
      },

      onStompError: (err) => {
        console.error("STOMP error:", err);
      },
    });

    stompClient.current.activate();

    return () => {
      stompClient.current?.deactivate();
    };
  }, [conversationId, token]);

  // ✅ Send message
  const sendMessage = (content: string) => {
    if (!stompClient.current || !stompClient.current.connected) return;

    stompClient.current.publish({
      destination: "/app/chat",
      body: JSON.stringify({
        type:"USER",
        content
      }),
    });
  };

  const loadOldMessages = async () => {
  const res = await fetch(
    `http://localhost:8080/messages/${conversationId}?page=${page}&size=10`
  );

  const data = await res.json();

  // reverse because backend sends desc
  setMessages(prev => [...data.reverse(), ...prev]);

  setPage(prev => prev + 1);
};
  return { messages, sendMessage, isTyping ,loadOldMessages};
};