import React, { useState, useEffect } from "react";
import ChatService from "../services/ChatService";
import "./ChatComponent.css";

const ChatComponent = ({ projectId, userId }) => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const chatService = new ChatService(projectId, handleMessageReceived);

  // Hàm xử lý tin nhắn mới nhận được
  function handleMessageReceived(chatMessage) {
    setMessages((prevMessages) => [...prevMessages, chatMessage]);
  }

  // Kết nối WebSocket khi component được mount
  useEffect(() => {
    chatService.connect();
    return () => chatService.disconnect(); // Ngắt kết nối khi component bị unmount
  }, []);

  // Gửi tin nhắn
  const sendMessage = () => {
    if (newMessage.trim() !== "") {
      chatService.sendMessage(userId, newMessage);
      setNewMessage("");
    }
  };

  return (
    <div className="chat-container">
      <h2>Chat Room for Project {projectId}</h2>
      <div className="chat-box">
        {messages.map((msg, index) => (
          <div key={index} className="chat-message">
            <strong>{msg.senderId}:</strong> {msg.content}
          </div>
        ))}
      </div>
      <div className="chat-input">
        <input
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          placeholder="Type a message..."
        />
        <button onClick={sendMessage}>Send</button>
      </div>
    </div>
  );
};

export default ChatComponent;
