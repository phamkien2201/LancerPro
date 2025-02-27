import { Client } from "@stomp/stompjs";

class ChatService {
  constructor(projectId, onMessageReceived) {
    this.projectId = projectId;
    this.onMessageReceived = onMessageReceived;
    this.socketUrl = "ws://localhost:8083/chats/ws-chat"; // WebSocket Server URL
    this.stompClient = null;
  }

  // Kết nối WebSocket
  connect() {
    this.stompClient = new Client({
      brokerURL: this.socketUrl,
      reconnectDelay: 5000, // Tự động reconnect sau 5 giây nếu mất kết nối
      onConnect: () => {
        console.log("✅ Connected to WebSocket");

        // Subscribe để nhận tin nhắn theo projectId
        this.stompClient.subscribe(
          `/topic/chat/${this.projectId}`,
          (message) => {
            const chatMessage = JSON.parse(message.body);
            this.onMessageReceived(chatMessage);
          }
        );
      },
      onDisconnect: () => console.log("❌ Disconnected from WebSocket"),
      onStompError: (frame) => console.error("⚠️ WebSocket Error:", frame),
    });

    this.stompClient.activate(); // Kích hoạt WebSocket
  }

  // Gửi tin nhắn
  sendMessage(senderId, content) {
    if (this.stompClient && this.stompClient.connected) {
      const message = {
        projectId: this.projectId,
        senderId: senderId,
        content: content,
      };

      this.stompClient.publish({
        destination: "/app/chat/send",
        body: JSON.stringify(message),
      });
    }
  }

  // Ngắt kết nối
  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
      console.log("🛑 WebSocket disconnected");
    }
  }
}

export default ChatService;
