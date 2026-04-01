import { useEffect, useRef, useState } from "react"
import { useChat } from "../hooks/useChat";
import "../styles/chat.css";

const ChatBox = ({ conversationId }: { conversationId: string }) => {
  const token = localStorage.getItem("jwtToken");
  const { messages, sendMessage, isTyping } = useChat(conversationId, token!);

  const [input, setInput] = useState("");
  const messageEndRef = useRef<HTMLDivElement | null>(null);

  const handleSend = () => {
    if (!input.trim()) return;
    sendMessage(input);
    setInput("");
  };

  useEffect(() => {
    messageEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // const currentUserId = 10; // TODO: replace with JWT decoded ID

  // return (
  //   // <div className="flex flex-col h-[500px] max-w-3xl mx-auto bg-white rounded-xl shadow-lg p-4">
  //   <div className="chat-container">
  //     {/* CHAT AREA */}
  //     <div className="chat-box">
  //       {messages.map((msg, idx) => {
  //         const isUser = msg.senderId === currentUserId;

  //         return (
  //           <div
  //             key={idx}
  //             className={``}
  //           >
  //             <div
  //               className={`max-w-xs px-4 py-2 rounded-lg text-sm shadow ${
  //                 isUser
  //                   ? "bg-blue-500 text-white rounded-br-none"
  //                   : "bg-gray-300 text-black rounded-bl-none"
  //               }`}
  //             >
  //               {msg.content}

  //               {/* STATUS / TIME */}
  //               <div className="text-[10px] mt-1 text-right opacity-70">
  //                 {msg.timestamp
  //                   ? new Date(msg.timestamp).toLocaleTimeString()
  //                   : ""}

  //                 {" "}

  //                 {msg.status === "PROCESSING" && "⏳"}
  //                 {msg.status === "SENT" && "✅"}
  //               </div>
  //             </div>
  //           </div>
  //         );
  //       })}

  //       {/* Typing indicator */}
  //       {isTyping && (
  //         <div className="text-gray-500 text-sm italic">
  //           🤖 AI is typing...
  //         </div>
  //       )}

  //       <div ref={messageEndRef} />
  //     </div>

  //     {/* INPUT */}
  //     <div className="flex gap-2 mt-3">
  //       <input
  //         className="flex-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
  //         value={input}
  //         onChange={(e) => setInput(e.target.value)}
  //         placeholder="Type your message..."
  //         onKeyDown={(e) => e.key === "Enter" && handleSend()}
  //       />

  //       <button
  //         className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition"
  //         onClick={handleSend}
  //       >
  //         Send
  //       </button>
  //     </div>
  //   </div>
  // );
  return (
  <div className="chat-container">

    {/* CHAT AREA */}
    <div className="chat-box">
      {messages.map((msg, idx) => {
        const isUser = msg.type  !== "AI";

        return (
          <div
            key={idx}
            className={`message-row ${isUser ? "user" : "ai"}`}
          >
            <div className={`message-bubble ${isUser ? "user" : "ai"}`}>
              {msg.content}

              <div className="message-meta">
                {msg.timestamp
                  ? new Date(msg.timestamp).toLocaleTimeString()
                  : ""}

                {" "}

                {msg.status === "PROCESSING" && "⏳"}
                {msg.status === "SENT" }
              </div>
            </div>
          </div>
        );
      })}

      {isTyping && (
        <div className="message-row ai">
          <div className="message-bubble ai">
            ...
          </div>
        </div>
      )}

      <div ref={messageEndRef}></div>
    </div>

    {/* INPUT */}
    <div className="input-box">
      <input
        value={input}
        onChange={(e) => setInput(e.target.value)}
        placeholder="Type a message..."
        onKeyDown={(e) => e.key === "Enter" && handleSend()}
      />

      <button onClick={handleSend}>Send</button>
    </div>

  </div>
);
};

export default ChatBox;