import { useState, useEffect, useRef } from "react";
import { useChat } from "../hooks/useChat";

export default function ChatPage() {
  const conversationId = "1";
  const token = localStorage.getItem("token") || "";

  const { messages, sendMessage, isTyping, loadOldMessages } =
    useChat(conversationId, token);

  const [text, setText] = useState("");

  const chatBoxRef = useRef<HTMLDivElement | null>(null);
  const bottomRef = useRef<HTMLDivElement | null>(null);

  // ✅ Load old messages on scroll top
  const handleScroll = async () => {
    if (!chatBoxRef.current) return;

    if (chatBoxRef.current.scrollTop === 0) {
      await loadOldMessages();
    }
  };

  // ✅ Auto scroll to bottom
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const handleSend = () => {
    if (!text.trim()) return;
    sendMessage(text);
    setText("");
  };

  return (
    <div
      style={{
        height: "100vh",
        background: "linear-gradient(135deg, #1e1e2f, #2c2c54)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      <div
        style={{
          width: "400px",
          background: "white",
          padding: "15px",
          borderRadius: "12px",
          boxShadow: "0 5px 20px rgba(0,0,0,0.3)",
        }}
      >
        <h2 style={{ textAlign: "center" }}>Chat</h2>

        {/* ✅ Chat Box */}
        <div
          ref={chatBoxRef}
          onScroll={handleScroll}
          style={{
            height: "400px",
            overflowY: "auto",
            padding: "10px",
            background: "#f5f5f5",
            borderRadius: "10px",
            marginBottom: "10px",
          }}
        >
          {messages.map((msg, index) => {
            const isAI = msg.type === "AI";

            return (
              <div
                key={index}
                style={{
                  display: "flex",
                  justifyContent: isAI ? "flex-start" : "flex-end",
                  marginBottom: "10px",
                }}
              >
                <div
                  style={{
                    maxWidth: "65%",
                    padding: "10px",
                    borderRadius: "12px",
                    backgroundColor: isAI ? "#e4e6eb" : "#4CAF50",
                    color: isAI ? "black" : "white",
                    boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
                  }}
                >
                  {/* MESSAGE */}
                  <div>{msg.content}</div>

                  {/* TIME + STATUS */}
                  <div
                    style={{
                      fontSize: "10px",
                      marginTop: "5px",
                      textAlign: "right",
                      opacity: 0.7,
                    }}
                  >
                    {msg.timestamp
                      ? new Date(msg.timestamp).toLocaleTimeString()
                      : ""}{" "}
                    {msg.status === "PROCESSING" && "⏳"}
                    {msg.status === "SENT" && "✅"}
                  </div>
                </div>
              </div>
            );
          })}

          {/* ✅ Typing Indicator */}
          {isTyping && (
            <div style={{ color: "gray", fontStyle: "italic" }}>
              🤖 AI is typing...
            </div>
          )}

          {/* ✅ Auto Scroll Anchor */}
          <div ref={bottomRef}></div>
        </div>

        {/* ✅ Input Section */}
        <div style={{ display: "flex" }}>
          <input
            value={text}
            onChange={(e) => setText(e.target.value)}
            placeholder="Type a message..."
            style={{
              flex: 1,
              padding: "10px",
              borderRadius: "8px",
              border: "1px solid #ccc",
              marginRight: "8px",
            }}
          />

          <button
            onClick={handleSend}
            style={{
              padding: "10px 15px",
              borderRadius: "8px",
              background: "#4CAF50",
              color: "white",
              border: "none",
              cursor: "pointer",
            }}
          >
            Send
          </button>
        </div>
      </div>
    </div>
  );
}