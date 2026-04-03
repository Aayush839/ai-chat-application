import React, { useState, useEffect } from "react";
import ChatBox from "../components/ChatBox";

const Dashboard = () => {
const BASE_URL = import.meta.env.VITE_API_URL;
  // ✅ FIX 1: use correct type (string, not String)
  const [conversationId, setConversationId] = useState<string | null>(null);

  useEffect(() => {

    const fetchConversation = async () => {
      try {
        const token = localStorage.getItem("jwtToken");
        console.log("token:", token);

        // const res = await fetch("http://localhost:8080/api/conversation", {
        const res = await fetch(`${BASE_URL}/api/conversation`, {
        
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!res.ok) {
          throw new Error("Failed to fetch conversation");
        }

        const data = await res.json();

        console.log("Conversation -> :", data);

        // ✅ FIX 2: always convert to string
        setConversationId(data.id.toString());

      } catch (err) {
        console.error("Error fetching conversation", err);
      }
    };

    fetchConversation();

  }, []);

  // ✅ FIX 3: prevent early render (VERY IMPORTANT)
  if (!conversationId) {
    return <p className="text-white text-center mt-10">Loading...</p>;
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 to-gray-700 flex flex-col items-center justify-center">
      
      <h1 className="text-2xl font-bold mb-4 text-white text-center">
        Welcome to SupportAI Dashboard
      </h1>

      <p className="mb-4 text-center text-gray-300">
        Chat with our AI assistant in real-time!
      </p>

      {/* ✅ NOW conversationId is always correct */}
      <ChatBox conversationId={conversationId} />
    </div>
  );
};

export default Dashboard;